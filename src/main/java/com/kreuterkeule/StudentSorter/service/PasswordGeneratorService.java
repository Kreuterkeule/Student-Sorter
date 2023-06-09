package com.kreuterkeule.StudentSorter.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordGeneratorService {

    public static String generatePassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10; // does not have to be very secure
        Random random = new Random();

        String password = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(password);

        return password;
    }

}
