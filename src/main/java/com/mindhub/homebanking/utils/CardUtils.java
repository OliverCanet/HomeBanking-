package com.mindhub.homebanking.utils;

public final class CardUtils {

    private CardUtils() {
    }

    public static String getCardNumber()
    {
        String cardNumber = getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000);
        return cardNumber;
    }

    public static int getCardCVV(){
        int cardCVV = getRandomNumber(1000, 1000);
        return cardCVV;
    }
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
