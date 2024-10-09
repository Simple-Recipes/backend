package com.recipes.service;

// Using Aws for user forget password
public interface CognitoService {
    void sendForgotPasswordCode(String email);
    void confirmForgotPassword(String confirmationCode, String newPassword);
}
