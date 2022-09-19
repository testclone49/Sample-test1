/**
 * Created by: nuwan_r
 * Created on: 4/23/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class PaymentErrorDto {

    private String userId;
    private String status;
    private String description;
}
