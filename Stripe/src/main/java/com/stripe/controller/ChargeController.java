package com.stripe.controller;

import com.stripe.service.ChargeProcessService;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Log
@RestController
public class ChargeController {


    @Autowired
    private ChargeProcessService chargeProcessService;

    private Logger logger = LogManager.getLogger(ChargeController.class);

    @PostMapping("/charge")
    public ModelAndView charge(ChargeRequest chargeRequest, Model model)  {

        String stripeEmail = chargeRequest.getStripeEmail();
        Integer amount = chargeRequest.getAmount();
        String stripToken = chargeRequest.getStripeToken();
        String userId = chargeRequest.getSystemUserId();

        return this.chargeProcessService.doInitialChargeProcess(model, stripeEmail, amount, stripToken, userId);

    }

}
