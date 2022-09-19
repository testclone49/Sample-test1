/**
 * Created by: nuwan_r
 * Created on: 4/22/2021
 **/
package com.stripe.service;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.controller.ChargeRequest;
import com.stripe.dto.*;
import com.stripe.entity.ChargingRecordDocument;
import com.stripe.entity.StripeCustomer;
import com.stripe.repository.ChargingRecordDocumentRepository;
import com.stripe.repository.StripeCustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChargeProcessService {

    public static final String END_VIEW_DESCRIPTION_V1 = "More Information Need For Initiate Charging Process ";
    public static final String END_VIEW_DESCRIPTION_V2 ="Stripe Customer Id Missed Match";
    public static final String PAYMENT_FAILED = "failed";
    public static final String PAYMENT_SUCCESS = "succeeded";
    public static final String PAYMENT_ACTIVE = "ACTIVE";
    public static final String PAYMENT_DE_ACTIVE = "De-ACTIVE";
    public static final String PAY_FOR_FIRST_MONTH = "First Month Payment";
    public static final String PAY_RECURSIVELY = "RECURSIVE Payment";
    public static final String PAYMENT_EXPIRING_AFTER = "1 MONTH"; 
    private static final String END_VIEW_DESCRIPTION_V3 = "Isssue With  Internal Data Saving ";
    private static final String END_VIEW_DESCRIPTION_V4 = "Stripe Charging Response Not Success";
    private static final String END_VIEW_DESCRIPTION_V5 = "Issue When Creating Stripe Customer";
    private static final String END_VIEW_DESCRIPTION_V6 = "Internal Payment Service Error";
    private static final String ERROR_VIEW_CARD_ISSUE = "Issue with merchant account or credit card processing";
    private static final String ERROR_VIEW_TOO_MANY_REQUEST = "Too many request made to the api";
    private static final String ERROR_VIEW_INVALID_PARAMETER = "Invalid parameters were supplied";
    private static final String ERROR_VIEW_AUTHENTICATION_FAILED = "Authentication with Api failed";
    private static final String ERROR_VIEW_SERVICE_ERROR = "Service Error";
    private static final String TRANSACTION_NOT_EXPIRED = "NOT_EXPIRED";
    private static final String TRANSACTION_EXPIRED = "EXPIRED";
    @Autowired
    private StripeService stripeService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StripeCustomerRepository stripedCustomerRepository;
    @Autowired
    private ChargingRecordDocumentRepository chargingRecordDocumentRepository;

    private Logger logger = LogManager.getLogger(ChargeProcessService.class);


    public ModelAndView doInitialChargeProcess(Model model, String stripeEmail, Integer amount, String  stripToken, String userId) {

        if(stripToken!=null && amount!=null && stripToken!=null && userId!=null) {

            logger.info("Charging process initiated : userId :"+userId+" Amount : "+amount +" stripe email :"+stripeEmail);

            Optional<ChargingRecordDocument> dbChargingRecord =
                    this.chargingRecordDocumentRepository.getChargingRecordBySystemUserIdAndActiveStatus(userId, PAYMENT_ACTIVE);

            if(dbChargingRecord.isPresent()) {
                logger.info("Charging Record Exist given system user");
                ChargingRecordDocument statusUpdatedRecod = dbChargingRecord.map(val -> {
                    val.getSystemPaymentInfo().setActiveStatus(PAYMENT_DE_ACTIVE);
                    val.setLastModifiedDate(this.getCurrentDate());
                    return val;
                }).orElse(null);

                this.chargingRecordDocumentRepository.save(statusUpdatedRecod);
                logger.info("current system user de activated "+userId);
            }


            Map<String, Object> customerParameter = createCustomerObj(stripeEmail,stripToken);

            try {

                Customer customer = Customer.create(customerParameter);

                if(Optional.ofNullable(customer).isPresent()) {

                    logger.info("Stripe Customer created! ");

                    StripeCustomer stripeCustomer = saveStripedCustomerResponse(userId, customer);

                    Customer customerFromStrped = getCustomerByCustomerId(stripeCustomer.getStripeCustomerId());
                    if(stripeCustomer.getStripeCustomerId().equals(customerFromStrped.getId())) {

                        logger.info("Stripe Customer Id Matched!");
                        logger.info("Stripe Customer Id : "+customerFromStrped.getId());
                        return chargingProcessStarter(model, amount, userId, customerFromStrped);

                    } else {

                       logger.warn("Stripe Customer Id Missed Match!");
                       return createErrorEndResultModel(userId,END_VIEW_DESCRIPTION_V2);

                    }

                } else {
                    logger.warn(END_VIEW_DESCRIPTION_V5);
                    return createErrorEndResultModel(userId,END_VIEW_DESCRIPTION_V5);
                }
            } catch (CardException e) {
                logger.error("Status is: " + e.getCode());
                logger.error("Message is: " + e.getMessage());
                return createErrorEndResultModel(userId,END_VIEW_DESCRIPTION_V1);

            } catch (RateLimitException e) {
                logger.error("Status is: " + e.getCode());
                logger.error("Message is: " + e.getMessage());
                return createErrorEndResultModel(userId,ERROR_VIEW_TOO_MANY_REQUEST);
            } catch (InvalidRequestException e) {
                // Invalid parameters were supplied to Stripe's API
                logger.error("Status is: " + e.getCode());
                logger.error("Message is: " + e.getMessage());
                return createErrorEndResultModel(userId,ERROR_VIEW_INVALID_PARAMETER);
            } catch (AuthenticationException e) {
                logger.error("Status is: " + e.getCode());
                logger.error("Message is: " + e.getMessage());
                return createErrorEndResultModel(userId,ERROR_VIEW_TOO_MANY_REQUEST);
            } catch (StripeException e) {
                logger.error("Status is: " + e.getCode());
                logger.error("Message is: " + e.getMessage());
                return createErrorEndResultModel(userId,ERROR_VIEW_AUTHENTICATION_FAILED);
            } catch (Exception e) {
                logger.error("Message is: " + e.getMessage());
                return createErrorEndResultModel(userId,ERROR_VIEW_SERVICE_ERROR);
            }

        } else {
            logger.info(END_VIEW_DESCRIPTION_V1);
            return createErrorEndResultModel(userId,END_VIEW_DESCRIPTION_V1);
        }
    }

    @Transactional
    private ModelAndView chargingProcessStarter(Model model, Integer amount, String userId, Customer customerFromStrped) throws StripeException , Exception {

        logger.info("inside the chargingProcessStarter");
        Map<String, Object> chargePrams = createChargeParams(amount, customerFromStrped);
        Charge firstMOnthChargeResponse   = Charge.create(chargePrams);

        if(firstMOnthChargeResponse.getStatus().equals(PAYMENT_SUCCESS)) {
            logger.info("Stripe Payment has succeeded from stripe end!");
            ChargingRecordDocument crd = createChargingRecordDoc(userId, customerFromStrped, firstMOnthChargeResponse);

                ChargingRecordDocument chargingrecordsavedObj = chargingRecordDocumentRepository.save(crd);
                return Optional.ofNullable(chargingrecordsavedObj).map(rec -> {

                    if(rec.getChargingResponseCustom()!=null && rec.getSystemPaymentInfo()!=null) {
                        logger.info("First Time Payment Success "+firstMOnthChargeResponse.getId());
                        ModelAndView view = new ModelAndView();

                        PaymentSuccessDto psd = new PaymentSuccessDto();
                        psd.setAmount(rec.getChargingResponseCustom().getAmount()/100);
                        psd.setDescription(rec.getChargingResponseCustom().getDescription());
                        psd.setReceiptEmail(rec.getChargingResponseCustom().getSource().getName());
                        psd.setStatus(rec.getChargingResponseCustom().getStatus());
                        psd.setSystemUserId(userId);
                        psd.setChargingResId(rec.getChargingResponseCustom().getId());
                        view.setViewName("result");
                        view.addObject("response", psd);

                        return view;
                    }else {

                        logger.error(END_VIEW_DESCRIPTION_V3);
                        return createErrorEndResultModel(userId,END_VIEW_DESCRIPTION_V3);

                    }
                }).orElseThrow(() -> new Exception("Error From Persiting charging record"));

        } else{

            logger.error("Stripe Charging Respose status not success!");
            return createErrorEndResultModel(userId,ERROR_VIEW_SERVICE_ERROR);
        }
    }

    public ChargingRecordDocument createChargingRecordDoc(String userId, Customer customerFromStrped, Charge firstMOnthChargeResponse) throws ParseException {
        logger.info("creating charging record document");
        ChargingRecordDocument crd = new ChargingRecordDocument();
        final Calendar cal = Calendar.getInstance();
        SystemPaymentInfo spi = new SystemPaymentInfo();

        spi.setCustomerId(customerFromStrped.getId());
        spi.setSystemUserId(userId);
        spi.setSubscriptionStatus(PAYMENT_SUCCESS);
        spi.setActiveStatus(PAYMENT_ACTIVE);
        spi.setSubscriptionDuration(PAYMENT_EXPIRING_AFTER);
        spi.setTransactionExpiringStatus(TRANSACTION_NOT_EXPIRED);
        Date paymentWillExpireOn = calculatePaymentExpiredDate(PAYMENT_EXPIRING_AFTER);
        logger.info("Subscription Ending Date "+paymentWillExpireOn);
        spi.setSubscriptionEndDate(paymentWillExpireOn);

        spi.setSubscriptionStartDate(cal.getTime());
        crd.setSystemPaymentInfo(spi);
        crd.setChargingResponseCustom(this.modelMapper.map(firstMOnthChargeResponse, ChargingResponseCustom.class));
        crd.getChargingResponseCustom().setSource(this.modelMapper.map(firstMOnthChargeResponse.getSource(),ChargeResponseSource.class));
        crd.setCreatedDate(this.getCurrentDate());
        crd.setLastModifiedDate(this.getCurrentDate());
        return crd;
    }

    private Map<String, Object> createChargeParams(Integer amount, Customer customerFromStrped) {
        Map<String,Object> chargePrams = new HashMap<String,Object>();

        chargePrams.put("amount", amount);
        chargePrams.put("currency", ChargeRequest.Currency.USD);
        chargePrams.put("description",PAY_FOR_FIRST_MONTH);

        chargePrams.put("customer",customerFromStrped.getId());
        return chargePrams;
    }

    private StripeCustomer saveStripedCustomerResponse(String userId, Customer customer) {
        logger.info("Stripe Customer Saving! ");
        StripeCustomer stripeCustomerMapped =  this.modelMapper.map(customer,StripeCustomer.class);
        stripeCustomerMapped.setStripeCustomerId(customer.getId());
        stripeCustomerMapped.setDefaultSource(customer.getDefaultSource());
        stripeCustomerMapped.setSystemUserId(userId);
        stripeCustomerMapped.setCreatedDate(getCurrentDate());
        stripeCustomerMapped.setLastModifiedDate(getCurrentDate());
        return this.stripedCustomerRepository.save(stripeCustomerMapped);
    }

    private ModelAndView createErrorEndResultModel(String userId, String description) {
        ModelAndView view = new ModelAndView();

        PaymentErrorDto ped = new PaymentErrorDto();
        ped.setDescription(description);
        ped.setStatus(PAYMENT_FAILED);
        ped.setSystemUserId(userId);

        view.setViewName("paymentError");
        view.addObject("response", ped);
        return view;
    }

    private ExternalAccount addCardToCustomer(String stripToken, Customer customer) throws Exception {
        logger.info("adding card to the customer");
        Customer retrivedCustomer =  Customer.retrieve(customer.getId());
        //   return Optional.ofNullable(retrivedCustomer).map(retri -> {
        Map<String,Object> source = new HashMap<String, Object>();
        source.put("source",stripToken);
        ExternalAccount externalAccount = null;
        try {

            externalAccount = retrivedCustomer.getSources().create(source);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return externalAccount;
    }

    private Map<String, Object> createCustomerObj(String stripeEmail,String stripToken) {
        String customer_id = "cus_"+UUID.randomUUID().toString();
        Map<String,Object> customerParameter = new HashMap<>();
        customerParameter.put("email",stripeEmail);
        customerParameter.put("id",customer_id);
        customerParameter.put("source",stripToken);
        logger.info("customer info "+stripeEmail+" , "+ customer_id);
        return customerParameter;
    }

    private Date calculatePaymentExpiredDate(final String durationString) throws ParseException {
        Pattern p = Pattern.compile("[1-9+]+\\s+MONTH|[1-9+]+\\s+YEAR");
        Matcher m = p.matcher(durationString);

        if (m.find()) {

            String[] splited = durationString.split("\\s+");
            String duration = splited[0];
            String scale = splited[1];

            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(new Date().getTime());

            if(scale.equals("YEAR")) {

                cal.add(Calendar.YEAR, Integer.parseInt(duration));
                return new Date(cal.getTime().getTime());
            } else if (scale.equals("MONTH")) {
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                cal.add(Calendar.MONTH, Integer.parseInt(duration));
                String str_date = simpleDateFormat.format(cal.getTime().getTime());
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(str_date);
                return date;
            }
        }
        return null;
    }

    public Date getCurrentDate () {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        return new Date(cal.getTime().getTime());
    }

    @GetMapping("/makeMonthlyPayment")
    public ResponseEntity makeMonthlyPayment() {

        String customerId = "cus_c8e24feb-bf0f-4ae4-b42e-765bb25e7226";
        Map<String,Object> chargePrams = new HashMap<String,Object>();

        chargePrams.put("amount", 35*100);
        chargePrams.put("currency", "usd");
        chargePrams.put("description","monthly payment process");
        Customer dbCustomer = null;

        try {
            dbCustomer = getCustomerByCustomerId(customerId);
        } catch (Exception e) {
            logger.error(e);
        }
        chargePrams.put("customer",dbCustomer.getId());
        Charge monthlyChargeResponse = null;
        try {
            monthlyChargeResponse   = Charge.create(chargePrams);
        } catch (StripeException e) {
            logger.error("Error when monthly charging process");
            e.printStackTrace();
        }

        return new ResponseEntity(monthlyChargeResponse,HttpStatus.OK);
    }

    private Customer getCustomerByCustomerId(String customerId) throws Exception {
        return Optional.ofNullable(Customer.retrieve(customerId)).orElseThrow(() -> new Exception("Cannot find Customer"));
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }
}
