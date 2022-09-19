/**
 * Created by: nuwan_r
 * Created on: 4/23/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class ChargeResponseSource {
    private String addressCity;
    private String addressCountry;
    private String addressLine1;
    private String addressLine1Check;
    private String addressLine2;
    private String addressState;
    private String addressZip;
    private String addressZipCheck;
    private String brand;
    private String country;
    private String cvcCheck;
    private String expMonth;
    private String expYear;
    private String fingerprint;
    private String funding;
    private String last4;
    private String name;
    private String id;
    private String customer;
    private String account;
    private String status;
    private String description;
    private String type;
    private String issuer;

}
