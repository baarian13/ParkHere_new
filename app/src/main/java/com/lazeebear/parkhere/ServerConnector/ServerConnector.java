package com.lazeebear.parkhere.ServerConnector;


import android.os.AsyncTask;
import android.util.Base64;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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

    static class GetTokenTask extends AsyncTask<Void,Void,Void>
       {
                String token;
                boolean done = false;
                boolean success = false;

                        public GetTokenTask(){
                    }

                        protected void onPreExecute() {
                        //display progress dialog.

                                    }
                protected Void doInBackground(Void... params) {
                        try {
                                String url = "http://35.160.111.133:8888/get/token";
                                URL obj = new URL(url);
                                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                con.setRequestMethod("GET");
                                con.setRequestProperty("UserAgent", USER_AGENT);
                                setConnCookies(con);
                //			con.connect();
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
                                token = response.toString();
                                //print result
                                        success = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        done = true;
                        return null;
                    }



                                        protected void onPostExecute(Void result) {
                        // dismiss progress dialog and update ui
                            }
            }

                static class SearchSpotTask extends AsyncTask<Void,Void,Void>
        {
                List<SpotDAO> spots;
                String address;
                boolean done = false;
                boolean success = false;

                        public SearchSpotTask(String address){
                        this.address = address;
                    }

                        protected void onPreExecute() {
                        //display progress dialog.

                                    }
                protected Void doInBackground(Void... params) {
                        try {
                                String url = "http://35.160.111.133:8888/search/spot?address="+ address.replace(' ', '+');
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

                            Gson gson = new Gson();
                                Type typeOfT = new TypeToken<List<SpotDAO>>(){}.getType();
                                spots = gson.fromJson(response.toString(), typeOfT);
                                //print result
                                        success = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        done = true;
                        return null;
                    }


            protected void onPostExecute(Void result) {

                        // dismiss progress dialog and update ui
                            }
            }

    public static List<SpotDAO> searchSpot(String address) throws Exception {
                SearchSpotTask s = new SearchSpotTask(address);
                s.execute();
                while(!s.done)
                        ;
                if(s.success)
                        return s.spots;
                return null;
            }

                /*
     Success  200 returned
     Failure  401 returned
      */
                /*
     public static int sigin(String email, String password) {
         String url = Configs.baseURL + Configs.signinEndpoint + "?email=" + email + "&password=" + password;

         RestTemplate restTemplate = new RestTemplate();
         ResponseEntity entity = restTemplate.getForEntity(url, Object.class);

         return entity.getStatusCode().value();
     }
     */

    public static String getToken() {
                GetTokenTask s = new GetTokenTask();
                s.execute();
                while(!s.done)
                        ;
                if(s.success)
                        return s.token;
                return null;
            }

    static class SpotDetailsTask extends AsyncTask<Void,Void,Void>
    {
        String spotID;
        SpotDetailsDAO spot;
        boolean done = false;
        boolean success = false;

        public SpotDetailsTask(String id){
            this.spotID = id;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = "http://35.160.111.133:8888/view/spot?spotid="+spotID;
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

                Gson gson = new Gson();
                Type typeOfT = new TypeToken<SpotDetailsDAO>(){}.getType();
                spot = gson.fromJson(response.toString(), typeOfT);
                //print result
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

    public static SpotDetailsDAO spotDetails(int spotID) throws Exception {
        SpotDetailsTask s = new SpotDetailsTask(spotID+"");
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return s.spot;
        return null;
    }

    static class SignInTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        String password;
        boolean done = false;
        boolean success = false;

        public SignInTask(String email, String password){
            this.email = email;
            this.password = password;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
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

                //print result
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

    public static boolean signin(String email, String password) {
        SignInTask s = new SignInTask(email, password);
        s.execute();
        while(!s.done)
            ;
        return s.success;
    }

    static class SignUpTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        String password;
        String first;
        String last;
        String phone;
        int seeker;
        int owner;
        Base64 profilePic;
        boolean done = false;
        boolean success = false;

        public SignUpTask(String email, String password, String first, String last, String phone, int seeker, int owner, Base64 profilePic){
            this.email = email;
            this.password = password;
            this.first = first;
            this.last = last;
            this.phone = phone;
            this.seeker = seeker;
            this.owner = owner;
            this.profilePic = profilePic;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
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
                con.connect();
                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

//            con.setDoOutput(false);
//            con.setDoInput(true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
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
        SignUpTask s = new SignUpTask(email, password, first, last, phone, seeker, owner, profilePic);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return 200;
        else
            return 401;
    }


    static class BookSpotTask extends AsyncTask<Void,Void,Void>
    {
        String amount;
        String payment_method_nonce;
        String email;
        String spotID;
        boolean done = false;
        boolean success = false;

        public BookSpotTask(String amount, String payment_method_nonce, String email, String spotID){
            this.amount = amount;
            this.payment_method_nonce = payment_method_nonce;
            this.email = email;
            this.spotID = spotID;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = "http://35.160.111.133:8888/book/spot";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "amount=" + amount + "&payment_method_nonce=" + payment_method_nonce + "&email=" + email +
                        "&spotID=" + spotID;

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

//            con.setDoOutput(false);
//            con.setDoInput(true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

    public static int bookSpot(String amount, String payment_method_nonce, String email, String spotID){
        BookSpotTask s = new BookSpotTask(amount, payment_method_nonce, email, spotID);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return 200;
        else
            return 401;
    }

}