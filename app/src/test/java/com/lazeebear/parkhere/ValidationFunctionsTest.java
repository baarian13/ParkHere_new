package com.lazeebear.parkhere;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by palet on 11/10/2016.
 */

public class ValidationFunctionsTest {

    @Test
    public void validEmailAddress(){
        assertEquals(ValidationFunctions.isEmailAddress(""),false);
        assertEquals(ValidationFunctions.isEmailAddress("yahoo"),false);
        assertEquals(ValidationFunctions.isEmailAddress("x.com"),false);
        assertEquals(ValidationFunctions.isEmailAddress("x@com"),false);
        assertEquals(ValidationFunctions.isEmailAddress("x@place.com"),true);
    }
}
