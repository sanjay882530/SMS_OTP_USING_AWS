package com.sanjay.smsotpusingaws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter@Getter
@AllArgsConstructor
public class User {

    private String user_id;
    private String user_name;
    private String email;
    private String password;
    private String phone;

}
