package com.employeemanagementsystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @PostMapping("/github-webhook")
    public String handleWebhook(@RequestBody String payload) {
        System.out.println("Received payload: " + payload);
        return "Webhook received!";
    }
}