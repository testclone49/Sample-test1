/**
 * Created by: nuwan_r
 * Created on: 4/21/2021
 **/
package com.stripe.dto;

import com.stripe.model.Customer;
import lombok.Data;

@Data
public class ChargingResponseCustom {

    private String id;
    private String object;
    private Integer amount;
    private String balanceTransaction;
    private String  created;
    private String currency;
    private String  customerId;
    private String description;
    private String failureCode;
    private String failureMessage;
    private Boolean livemode;
    private Boolean paid;
    private String status;
    private ChargeResponseSource source;
    private PaymentMethodDetails payment_method_details;
    private String receiptEmail;
    private String receiptUrl;
    private String receiptNumber;
}
