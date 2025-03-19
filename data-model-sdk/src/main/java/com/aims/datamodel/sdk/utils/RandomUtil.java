package com.aims.datamodel.sdk.utils;

public class RandomUtil {
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * 62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    public static String getRandomNumber(int length) {
        String str = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * 10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
