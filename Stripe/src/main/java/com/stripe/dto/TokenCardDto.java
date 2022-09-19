/**
 * Created by: nuwan_r
 * Created on: 4/18/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class TokenCardDto {

    private String address_city;
    private String address_country;
    private String address_line1;
    private String address_line1_check;
    private String address_line2;
    private String address_state;
    private String address_zip;
    private String address_zip_check;
    private String brand;
    private String country;
    private String cvc_check;
    private String dynamic_last4;
    private String exp_month;
    private String exp_year;
    private String funding;
    private String id;
    private String last4;
    private String name;
    private String object;
    private String  tokenization_method;
}
