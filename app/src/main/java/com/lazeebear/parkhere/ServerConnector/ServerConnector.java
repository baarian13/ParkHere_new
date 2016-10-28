package com.lazeebear.parkhere.ServerConnector;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotListDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.BookDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.RateDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentSpotDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentUserDAO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rjaso on 10/27/2016.
 */

public class ServerConnector {

    /*
    Success - 200 returned
    Failure - 401 returned
     */
    public static int sigin(String email, String password) {
        String url = Configs.baseURL + Configs.signinEndpoint + "?email=" + email + "&password=" + password;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.getForEntity(url, Object.class);

        return entity.getStatusCode().value();
    }

    /*
    Success - 200 returned
    Partial Success - 206 returned (Profile photo submission unsuccessful
    Failure - 401 returned
     */
    public static int signup(SentUserDAO user) {
        String url = Configs.baseURL + Configs.signupEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.postForEntity(url, user, Object.class);

        return entity.getStatusCode().value();
    }


    /*
    Send:
        String address
        Int longitude
        Int latitude
        Int month
        Int day
        Int year
        TIME startTime
        TIME endTime
    Returns:
        List spots
        String address
        TIME startTime
        TIME endTime
        Int price
    */
    public static SpotListDAO search(String address, int longitude, int latitude, int month, int day, int year, String startTime, String endTime) {
        String url = Configs.baseURL + Configs.searchEndpoint + "?address=" + address + "?longitude=" + longitude + "?month=" + month +
                "?day=" + day + "?year=" + year + "?startTime=" + startTime + "?endTime=" + endTime;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotListDAO> entity = restTemplate.getForEntity(url, SpotListDAO.class);

        return entity.getBody();
    }

    /*
    Send:
        Int spotID
    Return:
        String address
        Int latitude
        Int longitude
        Int ownerRating
        String picture
        String phoneNumber
        String startTime
        String endTime
        String description
        Float price
        Int rating
     */
    public static SpotDAO viewSpot(int spotID) {
        String url = Configs.baseURL + Configs.viewSpotEndpoint + "?spotID=" + spotID;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotDAO> entity = restTemplate.getForEntity(url, SpotDAO.class);

        return entity.getBody();
    }

    /*
    Send:
        String address
        Int latitude
        Int longitude
        Int ownerRating
        String picture
        String phoneNumber
        String startTime
        String endTime
        String description
        String price
    Return:
        Success - 200 returned
        Failure - 401 returned
     */
    public static int postSpot(SentSpotDAO spot) {
        String url = Configs.baseURL + Configs.postSpotEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.postForEntity(url, spot, Object.class);

        return entity.getStatusCode().value();
    }

    /*
    Send:
        String email
    Returns:
        String username
        String email
        Int rating
        String phoneNumber
        String email
    */
    public static ReturnedUserDAO viewUser(String email) {
        String url = Configs.baseURL + Configs.viewUserEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ReturnedUserDAO> entity = restTemplate.getForEntity(url, ReturnedUserDAO.class);

        return entity.getBody();
    }

    /*
    Send:
        Int spotID
        Int rating
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    // TODO, consider put - put returns void
    public static int rateUser(RateDAO rating) {
        String url = Configs.baseURL + Configs.rateUserEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.postForEntity(url, rating, Object.class);

        return entity.getStatusCode().value();
    }

    public static final String bookSpotEndpoint = "/book";
    /*
    Send:
        Int spotID
        String email
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    // TODO, consider put - put returns void
    public static int bookSpot(BookDAO booking) {
        String url = Configs.baseURL + Configs.bookSpotEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.postForEntity(url, booking, Object.class);

        return entity.getStatusCode().value();
    }

    /*
    Send:
        Int spotID
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    // TODO, consider put - put returns void
    public static int deleteSpot(int spotID) {
        String url = Configs.baseURL + Configs.deleteSpotEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.postForEntity(url, spotID, Object.class);

        return entity.getStatusCode().value();
    }

    /*
    Send:
        String email
    Returns:
        List<spot> spots
     */
    public static SpotListDAO viewRentals(String email) {
        String url = Configs.baseURL + Configs.viewRentalsEndpoint + "?email=" + email;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotListDAO> entity = restTemplate.getForEntity(url, SpotListDAO.class);

        return entity.getBody();
    }

    /*
    Send:
        String email
    Returns:
        List<spot> spots
     */
    public static SpotListDAO viewPostings(String email) {
        String url = Configs.baseURL + Configs.viewPostingsEndpoint + "?email=" + email;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotListDAO> entity = restTemplate.getForEntity(url, SpotListDAO.class);

        return entity.getBody();
    }

    /*
    Send:
        String email
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    // TODO, consider put - put returns void
    public static int forgotPassword(String email) {
        String url = Configs.baseURL + Configs.forgotPasswordEndpoint + "?email=" + email;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.getForEntity(url, Object.class);

        return entity.getStatusCode().value();
    }








}
