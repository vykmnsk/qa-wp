package com.tabcorp.qa.wagerplayer.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer {
    public String username;
    public String title;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public LocalDate dob;
    public String telephoneNo;
    public String email;
    public String street;
    public String city;
    public String suburb;
    public String state;
    public String postCode;
    public String country;
    public String weeklyDepositLimit;
    public String securityQuestion;
    public String securityAnswer;
    public String currency;
    public String timezone;
    public String clientIP;
    public String password;
    public String telephonePassword;
    public String internetPassword;

//    public LocalDate parseDob(String dobTxt) {
//        return LocalDate.parse(dobTxt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    }
//
//    public String getDobDay() {
//        int day = parseDob(dateOfBirth).getDayOfMonth();
//        return String.valueOf(day);
//    }
//
//    public String getDobMonth() {
//        int month = parseDob(dateOfBirth).getMonthValue();
//        return String.valueOf(month);
//    }
//
//    public String getDobYear() {
//        int year = parseDob(dateOfBirth).getYear();
//        return String.valueOf(year);
//    }
}
