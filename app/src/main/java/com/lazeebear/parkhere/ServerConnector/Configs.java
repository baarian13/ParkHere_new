package com.lazeebear.parkhere.ServerConnector;

/**
 * Created by rjaso on 10/27/2016.
 */
public class Configs {

    // RESTful Endpoints
    public static final String baseURL = "http://localhost:8080";
    public static final String signinEndpoint = "/signin";
    /*
    Send:
        String email
        String password
    Returns:
        Success - 200 returned
        Failure - 401 returned
    */
    public static final String signupEndpoint = "/signup";
    /*
    Send:
       String username
       String email
       String password
       String firstName
       String lastName
       String phoneNumber
       String photo
       Bool seeker
       Bool owner
    Returns:
        Success - 200 returned
        Partial Success - 206 returned (Profile photo submission unsuccessful
        Failure - 401 returned
    */
    public static final String searchEndpoint = "/search";
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
    public static final String viewSpotEndpoint = "/view/spot";
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
        TIME startTime
        TIME endTime
        String description
        Float price
        Int rating
     */
    public static final String postSpotEndpoint = "/post/spot";
    /*
    Send:
        String address
        Int latitude
        Int longitude
        Int ownerRating
        String picture
        String phoneNumber
        TIME startTime
        TIME endTime
        String description
        String price
    Return:
        Success - 200 returned
        Failure - 401 returned
     */
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
