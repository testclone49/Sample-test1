/**
 * Created by: nuwan_r
 * Created on: 4/21/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class PaymentMethodDetails {

    private Card card;
    private String type;

}
