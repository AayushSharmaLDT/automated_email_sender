package com.email_sender.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import jakarta.mail.internet.InternetAddress;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    public static List<String> parseEmails(String filePath) throws IOException {
        List<String> emails = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {

                String email = record.get(0).trim();
                if (isValidEmail(email)) {
                    emails.add(email);
                } else {
                    System.out.println("Invalid email: " + email);
                }
            }
        }
        return emails;
    }

    private static boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
