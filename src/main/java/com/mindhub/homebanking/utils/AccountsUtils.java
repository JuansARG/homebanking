package com.mindhub.homebanking.utils;

public final class AccountsUtils{
    public static String getNumber4VIN(){
        int num = (int) (Math.random() * (999999 - 100000) + 100000);
        return String.valueOf(num);
    }
}
