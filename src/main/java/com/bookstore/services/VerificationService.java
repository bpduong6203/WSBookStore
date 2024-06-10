package com.bookstore.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class VerificationService {

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Random random = new Random();

    public String generateVerificationCode(String email) {
        String code = String.format("%06d", random.nextInt(1000000));
        verificationCodes.put(email, code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}
