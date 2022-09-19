/**
 * Created by: nuwan_r
 * Created on: 4/18/2021
 **/
package com.stripe.dto;

import lombok.Data;

@Data
public class StripeTokenObjDto {
    private String client_ip;
    private String created;
    private String email;
    private String id;
    private String livemode;
    private String object;
    private String type;
    private String  used;
    private TokenCardDto card;
    private String userId;
}
