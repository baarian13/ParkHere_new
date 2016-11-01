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
    public static int signup(String email, String password, String first, String last, String phone, int seeker, int owner, Base64 profilePic) {

        try {
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
            } else {
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
        } catch (Exception e) {
            e.printStackTrace();
            return 401;
        }
    }

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

}