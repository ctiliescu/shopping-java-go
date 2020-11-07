package com.ctiliescu.shopping.carts.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {
    public boolean checkPayment() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random r = new Random();
        return r.nextInt(2) % 2 == 0;
    }
}
