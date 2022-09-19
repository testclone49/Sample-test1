/**
 * Created by: nuwan_r
 * Created on: 4/21/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class Checks {

    private String address_line1_check;
    private String address_postal_code_check;
    private String cvc_check;
}
