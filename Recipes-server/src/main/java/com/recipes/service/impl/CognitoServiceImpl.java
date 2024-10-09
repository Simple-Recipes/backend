package com.recipes.service.impl;

import com.recipes.service.CognitoService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;

@Service
public class CognitoServiceImpl implements CognitoService {
    private CognitoIdentityProviderClient cognitoClient;
    private static final String CLIENT_ID = "";  // App Client ID

    public CognitoServiceImpl() {
        //"your-access-key-id", "your-secret-access-key
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create();

        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
    @Override
    public void sendForgotPasswordCode(String email) {
        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                .clientId(CLIENT_ID)
                .username(email)
                .build();

        ForgotPasswordResponse response = cognitoClient.forgotPassword(request);
        System.out.println("Password reset code sent successfully.");
    }

    @Override
    public void confirmForgotPassword(String confirmationCode, String newPassword) {
        ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
                .clientId(CLIENT_ID)
                .confirmationCode(confirmationCode)
                .password(newPassword)
                .build();
        cognitoClient.confirmForgotPassword(request);
    }
}
