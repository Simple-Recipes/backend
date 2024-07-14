package com.recipes.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonConversionUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertArrayToJson(String[] array) {
        try {
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert array to JSON string", e);
        }
    }

    public static String[] convertJsonToArray(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new String[0];
        }
        try {
            return objectMapper.readValue(jsonString, String[].class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to array", e);
        }
    }
}
