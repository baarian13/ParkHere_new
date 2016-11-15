package com.lazeebear.parkhere;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentUserDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by rjaso on 10/29/2016.
 */

public class ServerUserFunctionalityTest {

    @Test
    public void search() throws Exception {
        ServerConnector.signin("rjason14@gmail.com", "jerome");

        List<SpotDAO> spots = null;
        ServerConnector.searchSpot("707 West 28th Street, Los Angeles, CA 90007");

        assertTrue(!spots.isEmpty());

    }

    @Test
    public void signup() throws Exception {
        int result = ServerConnector.signup("rjason14@gmail.com", "jerome", "Jason", "Roodman", "3147911267", 1, 1,null,null);
        assertEquals(200, result);
    }

    @Test
    public void signin() throws Exception {
        boolean result = ServerConnector.signin("rjason14@gmail.com", "jerome");
        assertEquals(true, result);
    }

    /*
    @Test
    public void signinSuccess() throws Exception {
        int status = ServerConnector.sigin("rjason14@gmail.com", "jerome");

        System.out.println("Response status code:" + status);

        assertEquals(200, status);
    }

    @Test
    public void signinFailureWrongEmail() throws Exception {
        int status = ServerConnector.sigin("wrong@gmail.com", "jerome");

        System.out.println("Response status code:" + status);

        assertEquals(401, status);
    }

    @Test
    public void signinFailureWrongPassword() throws Exception {
        int status = ServerConnector.sigin("rjason14@gmail.com", "wrong");

        System.out.println("Response status code:" + status);

        assertEquals(401, status);
    }
    */
}
