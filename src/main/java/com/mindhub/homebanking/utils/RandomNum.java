package com.mindhub.homebanking.utils;

public class RandomNum {

    public static String getRandomNumber4Vin(){
        int num = (int) (Math.random() * (999999 - 100000) + 100000);
        return String.valueOf(num);
    }
    public static String getRandonNumber4CardNum(){
        StringBuilder numCard = new StringBuilder();
        for (int i = 1; i <= 4; i++){
            int num = (int) (Math.random() * (9999 - 1000) + 1000);
            numCard.append(String.valueOf(num));
        }
        return String.valueOf(numCard);
    }
    public static Integer getRandomNum4CVV(){
        return (int) (Math.random() * (999 - 100) + 100);
    }
}
