package com.condigence.nsnotificationservice.controller;

import com.condigence.nsnotificationservice.dto.SmsDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
@RequestMapping("neerseva/api/v1/notifications")
public class SmsController {



    @GetMapping(value = "/")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello from NS Notification Service!", HttpStatus.OK);

    }

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {

//        System.out.println(System.getenv("TWILIO_ACCOUNT_SID"));
//        System.out.println(System.getenv("TWILIO_AUTH_TOKEN"));
//        //Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));
//
//        Twilio.init("AC64a9697ac198e08bae29a05e3c149d34","b411bf69a16b2020d4b00f07da16b7a7");
//
//
//        Message.creator(new PhoneNumber("+919742503868"), // <TO number - ie your cellphone>
//                new PhoneNumber("+17175239452"), "Hello from Condigence 📞").create(); //<FROM number - ie your Twilio number

        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }



    @PostMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS(@RequestBody SmsDTO smsDTO) {
        System.out.println(System.getenv("TWILIO_ACCOUNT_SID"));
        System.out.println(System.getenv("TWILIO_AUTH_TOKEN"));
        //Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));
        Twilio.init("AC64a9697ac198e08bae29a05e3c149d34","b411bf69a16b2020d4b00f07da16b7a7");
        Message.creator(new PhoneNumber(smsDTO.getToNumber()), // <TO number - ie your cellphone>
                new PhoneNumber("+17175239452"), smsDTO.getMessage()+" from Condigence 📞. Thanks").create(); //<FROM number - ie your Twilio number
        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }
}
