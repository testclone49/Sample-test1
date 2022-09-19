/**
 * Created by: nuwan_r
 * Created on: 4/21/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class Card {

    private String brand;
    private Checks checks;
    private String country;
    private String exp_month;
    private String exp_year;
    private String fingerprint;
    private String funding;
    private String installments;
    private String last4;
    private String network;
    private String three_d_secure;
    private String wallet;
}
