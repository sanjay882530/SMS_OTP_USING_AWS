package com.sanjay.smsotpusingaws.controller;

import com.sanjay.smsotpusingaws.model.User;
import com.sanjay.smsotpusingaws.model.UserResposne;
import com.sanjay.smsotpusingaws.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    UserService userService;
    // send otp in mobile and api call three times
    @PostMapping(path = "/sendMobileOtp/{action}")
    public UserResposne sendMobileOtp(@PathVariable String action, @RequestBody User user) {
        return userService.sendOtp(user, action);

    }
}
