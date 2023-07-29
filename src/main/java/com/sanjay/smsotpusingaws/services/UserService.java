package com.sanjay.smsotpusingaws.services;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sanjay.smsotpusingaws.model.User;
import com.sanjay.smsotpusingaws.model.UserResposne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
@Service
public class UserService {
    @Autowired
    UtilityService utilityService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserResposne sendOtp(User user, String action) {
        UserResposne userResposne = new UserResposne();
        if (action.equals("loginmobileotp")) {
            if (StringUtils.hasText(user.getEmail()) && StringUtils.hasText(user.getPassword())) {
                String password = utilityService.getEncodedPassword(user.getPassword());
                String sql = "select user_id from users where phone = ? and email=? and password=? ";
                System.out.println(sql);
                Map<String, Object> id = jdbcTemplate.queryForMap(sql, user.getPhone(),
                        user.getEmail(), password);
                String otp = sendmobileOtp(user);
                sql = "update users set phone_otp = ? where email=? and password=?";
                jdbcTemplate.update(sql, otp, user.getEmail(), password);
                int user_id = Integer.parseInt(id.get("customer_id").toString());
                sql = "update users set phone_otp = ? where user_id=?";
                utilityService.updateOtp(sql, user_id);
                userResposne.setStatusCode(0);
                userResposne.setMessage("SMS otp is sent to your registered mobile number.");
            } else {
                userResposne.setStatusCode(1);
                userResposne.setMessage("All fiels must not be empty!");
            }
        }
        return userResposne;
    }

        public String sendmobileOtp (User user){
            String regionarn = "us-west-1";
            String phone = user.getPhone();
            AmazonSNS snsClient = AmazonSNSClient.builder().withRegion(regionarn).build();
            String otp = String.valueOf(utilityService.randomNumberGenerate());
            PublishResult publishResult = snsClient
                    .publish(new PublishRequest().withPhoneNumber(phone).withMessage("[Company Name] Verification code: " + otp
                            + ". You are trying to login to your account. If this is not you, please block your account from setting and contact company Support. Do not share this code with anyone. No one from company will ever ask you for this code."));
            System.out.println("Message sent successfully: " + publishResult.getMessageId());
            System.out.println("OTP is: " + otp);
            return otp;
        }
}

