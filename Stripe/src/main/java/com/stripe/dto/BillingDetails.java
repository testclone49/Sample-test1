package com.stripe.dto;

import com.stripe.model.Address;
import lombok.Data;

@Data
public class BillingDetails {

    private String email;
    private String name;
    private String phone;
    private Address address;
}
