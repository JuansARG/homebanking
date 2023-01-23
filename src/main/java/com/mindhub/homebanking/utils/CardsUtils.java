package com.mindhub.homebanking.utils;

public final class CardsUtils {

    public static String getCardNumber(){
        StringBuilder numCard = new StringBuilder();
        for (int i = 1; i <= 4; i++){
            int num = (int) (Math.random() * (9999 - 1000) + 1000);
            numCard.append(String.valueOf(num));
        }
        return String.valueOf(numCard);
    }
    public static Integer getCVV(){
        return (int) (Math.random() * (999 - 100) + 100);
    }
}
