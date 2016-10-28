package com.lazeebear.parkhere.ServerConnector;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotListDAO;
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




    public static final String viewUserEndpoint = "/view/user";
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
    public static final String rateUserEndpoint = "/rate";
    /*
    Send:
        Int spotID
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    public static final String bookSpotEndpoint = "/book";
    /*
    Send:
        Int spotID
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    public static final String deleteSpotEndpoint = "/delete";
    /*
    Send:
        Int spotID
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    public static final String viewRentalsEndpoint = "/view/rentals";
    /*
    Send:
        String email
    Returns:
        List<spot> spots
     */
    public static final String viewPostingsEndpoint = "/view/postings";
    /*
    Send:
        Int userID
    Returns:
        List<spot> spots
     */
    public static final String forgotPasswordEndpoint = "/password/reset";
    /*
    Send:
        String email
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */








}
