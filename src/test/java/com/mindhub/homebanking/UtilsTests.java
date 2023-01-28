package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.AccountsUtils;
import com.mindhub.homebanking.utils.CardsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
public class UtilsTests {

//    @Test
//    public void accountNumberIsCreated(){
//        String accountNumber = AccountsUtils.getNumber4VIN();
//        assertThat(accountNumber, is(not(emptyOrNullString())));
//
//    }
//
//    @Test
//    public void accountNumberIsAString(){
//        String accountNumber = AccountsUtils.getNumber4VIN();
//        assertThat(accountNumber, isA(String.class));
//    }
//
//    @Test
//    public void accountNumberLenghtValidation(){
//        String accountNumber = AccountsUtils.getNumber4VIN();
//        assertThat(accountNumber.length(), equalTo(6));
//    }
//
//    @Test
//    public void cardNumberIsCreated(){
//        String cardNumber = CardsUtils.getCardNumber();
//        assertThat(cardNumber, is(not(emptyOrNullString())));
//    }
//
//    @Test
//    public void cardNumberIsAString(){
//        String cardNumber = CardsUtils.getCardNumber();
//        assertThat(cardNumber, isA(String.class));
//    }
//
//    @Test
//    public void cardNumberLengthValidation(){
//        String cardNumber = CardsUtils.getCardNumber();
//        assertThat(cardNumber.length(), is(equalTo(16)));
//    }
//
//    @Test
//    public void cardCvvIsCreated(){
//        Integer cvv = CardsUtils.getCVV();
//        assertThat(cvv, is(greaterThan(99)));
//    }
//
//    @Test
//    public void cardCvvIsAInteger(){
//        Integer cvv = CardsUtils.getCVV();
//        assertThat(cvv, isA(Integer.class));
//    }
//
//    @Test
//    public void cardCvvLenghtValidation(){
//        Integer cvv = CardsUtils.getCVV();
//        assertThat(cvv, is(lessThan(1000)));
//    }

}
