package com.sanjay.smsotpusingaws.services;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.util.Base64;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    Logger logger= LoggerFactory.getLogger(UtilityService.class);
    public int randomNumberGenerate() {
        int start = 10000000;
        int end = 99999999;
        Random random = new Random();
        // get the range, casting to long to avoid overflow problems
        long range = (long) end - (long) start + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * random.nextDouble());
        int randomNumber = (int) (fraction + start);
        return randomNumber;
    }

    public String getEncodedPassword(String password) {
        String encoded_password = "";
        try {
            // encrypt password
            String text = password;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            encoded_password = Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded_password;
    }
    @Async
    public void updateOtp(String sql, int user) {
        try {
            Thread.sleep(120000);
            jdbcTemplate.update(sql, 0, user);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
