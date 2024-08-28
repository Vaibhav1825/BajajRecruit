package com.app;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

public class VaibhavTest {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar test.jar <PRN Number> <path to JSON file>");
            return;
        }

        String prnNumber = args[0].toLowerCase().replaceAll("\\s", "");
        String jsonFilePath = args[1];

        try {
            String destinationValue = findDestinationValue(jsonFilePath);
            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                return;
            }

            String randomString = generateRandomString(8);
            String concatenatedString = prnNumber + destinationValue + randomString;
            String md5Hash = generateMD5Hash(concatenatedString);

            System.out.println(md5Hash + ";" + randomString);
        } catch (IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating MD5 hash: " + e.getMessage());
        }
    }

    private static String findDestinationValue(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
        return findDestination(rootNode);
    }

    private static String findDestination(JsonNode node) {
        if (node.has("destination")) {
            return node.get("destination").asText();
        }

        Iterator<JsonNode> elements = node.elements();
        while (elements.hasNext()) {
            JsonNode childNode = elements.next();
            String value = findDestination(childNode);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}