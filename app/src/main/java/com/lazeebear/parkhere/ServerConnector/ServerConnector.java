package com.lazeebear.parkhere.ServerConnector;

import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotListDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.BookDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.RateDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentSpotDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentUserDAO;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by rjaso on 10/27/2016.
 */

public class ServerConnector {

    private static final String USER_AGENT = "Mozilla/5.0";

    static final String COOKIES_HEADER = "Set-Cookie";

    static CookieManager msCookieManager = new CookieManager();

    static void setConnCookies(HttpURLConnection con) {
        if (msCookieManager.getCookieStore().getCookies().size() > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            String c = "";
            for (HttpCookie cookie : msCookieManager.getCookieStore().getCookies()) {
                c += cookie.toString() + ";";
            }
            c = c.substring(0, c.length() - 1);
            System.out.println(c);
            con.setRequestProperty("Cookie", c);
        }
    }

    /*
    Success - 200 returned
    Failure - 401 returned
     */
    /*
    public static int sigin(String email, String password) {
        String url = Configs.baseURL + Configs.signinEndpoint + "?email=" + email + "&password=" + password;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.getForEntity(url, Object.class);

        return entity.getStatusCode().value();
    }
    */
    public static boolean signin(String email, String password) {
        try {
            String url = "http://35.160.111.133:8888/signin";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "email=" + email + "&password=" + password;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            Map<String, List<String>> headerFields = con.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.toString().trim().equals("authentication failed"))
                return false;

            //print result
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
    Success - 200 returned
    Partial Success - 206 returned (Profile photo submission unsuccessful
    Failure - 401 returned
     */
    /*
    public static int signup(SentUserDAO user) {
        String url = Configs.baseURL + Configs.signupEndpoint;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity entity = restTemplate.postForEntity(url, user, Object.class);

        return entity.getStatusCode().value();
    }
    */
    public static int signup(String email, String password, String first, String last, String phone, int seeker, int owner, Base64 profilePic) {

    try
    {
        String url = "http://35.160.111.133:8888/signup";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = null;

        if (profilePic != null) {
            urlParameters = "email=" + email + "&password=" + password + "&first=" + first +
                    "&last=" + last + "&phone=" + phone + "&seeker=" + seeker + "&owner=" + owner + "&profilePic="
                    + profilePic.toString();
        }
        else {
            urlParameters = "email=" + email + "&password=" + password + "&first=" + first +
                    "&last=" + last + "&phone=" + phone + "&seeker=" + seeker + "&owner=" + owner;
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return responseCode;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        return 401;
    }
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

    /*
    public static SpotListDAO search(String address, int longitude, int latitude, int month, int day, int year, String startTime, String endTime) {
        String url = Configs.baseURL + Configs.searchEndpoint + "?address=" + address + "?longitude=" + longitude + "?month=" + month +
                "?day=" + day + "?year=" + year + "?startTime=" + startTime + "?endTime=" + endTime;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotListDAO> entity = restTemplate.getForEntity(url, SpotListDAO.class);

        return entity.getBody();
    }
    */
    public static Object search(String address) throws Exception {
        String url = "http://35.160.111.133:8888/search/spot?address="+address.replace(' ', '+');
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        setConnCookies(con);
        con.connect();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        // TODO
        return 1;
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
