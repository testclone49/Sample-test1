/**
 * Created by: nuwan_r
 * Created on: 4/23/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class PaymentSuccessDto {

    private String userId;
    private String status;
    private Integer amount;
    private String receiptEmail;
    private String description;
    private String chargingResId;
}
