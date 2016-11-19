package com.lazeebear.parkhere.ServerConnector;


import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentSpotDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentUserDAO;

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

    static String formatURL(String command){
        //return "http://35.160.111.133:8888/" + command;
        return "http://35.163.38.167:8888/" + command;
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
                                String url = formatURL("get/token");
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

    public static String getToken() {
        GetTokenTask s = new GetTokenTask();
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return s.token;
        return null;
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
                            String url = formatURL("search/spot?address="+ address.replace(' ', '+'));
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
                String url = formatURL("view/spot?spotID="+spotID);
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

    static class UserDetailsTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        ReturnedUserDAO user;
        boolean done = false;
        boolean success = false;

        public UserDetailsTask(String email){
            this.email = email;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("view/user?email="+email);
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
                Type typeOfT = new TypeToken<ReturnedUserDAO>(){}.getType();
                user = gson.fromJson(response.toString(), typeOfT);
                //print result
                success = true;
                Log.i("STATE","user details success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","user details done = true");
            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

//    static class SpotsOwnedTask extends AsyncTask<Void,Void,Void>
//    {
//        String email;
//        List<Integer> spots;
//        boolean done = false;
//        boolean success = false;
//
//        public SpotsOwnedTask(String email){
//            this.email = email;
//        }
//
//        protected void onPreExecute() {
//            //display progress dialog.
//
//        }
//        protected Void doInBackground(Void... params) {
//            try {
//                String url = formatURL("view/postings?email="+email);
//                URL obj = new URL(url);
//                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//                con.setRequestMethod("GET");
//                con.setRequestProperty("User-Agent", USER_AGENT);
//                setConnCookies(con);
//                con.connect();
//                int responseCode = con.getResponseCode();
//                System.out.println("\nSending 'GET' request to URL : " + url);
//                System.out.println("Response Code : " + responseCode);
//
//
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(con.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                Gson gson = new Gson();
//                Type typeOfT = new TypeToken<List<Integer>>(){}.getType();
//                spots = gson.fromJson(response.toString(), typeOfT);
//                //print result
//                success = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            done = true;
//            return null;
//        }
//
//
//
//        protected void onPostExecute(Void result) {
//            // dismiss progress dialog and update ui
//        }
//    }

    public static ReturnedUserDAO userDetails(String email) throws Exception {
        UserDetailsTask s = new UserDetailsTask(email);
        ReturnedUserDAO user;
        s.execute();
        Log.i("STATE","Waiting for user details");
        while(!s.done)
            Log.i("SPAM","3");
        Log.i("STATE","Finished waiting for user details");
        if(s.success) {
            user = s.user;
//            SpotsOwnedTask o = new SpotsOwnedTask(email);
//            o.execute();
//            while(!o.done)
//                ;
//            if(o.success){
//                user.setSpots(o.spots);
//            }


            return user;
        }
        return null;
    }

    static class CheckUserTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        boolean ret;
        boolean done = false;
        boolean success = false;

        public CheckUserTask(String email){
            this.email = email;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("check/user?email="+email);
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
                int val = Integer.parseInt(response.toString());
                if (val == 1)
                    ret = true;
                else
                    ret = false;
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


    public static boolean checkUser(String email) throws Exception {
        CheckUserTask s = new CheckUserTask(email);
        boolean same;
        s.execute();
        while(!s.done)
            ;
        if(s.success) {
            return s.ret;
        }
        return false;
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
                String url = formatURL("signin");
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
                Log.i("STATE","success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE", "done = true");
            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

    public static boolean signin(String email, String password) {
        SignInTask s = new SignInTask(email, password);
        s.execute();
        Log.i("STATE","Waiting for signin");
        while(!s.done)
            Log.i("SPAM","2");
        Log.i("STATE","Finished signin");
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
        String profilePic = null;
        String verificationPhoto = null;
        boolean done = false;
        boolean success = false;

        public SignUpTask(String email, String password, String first, String last, String phone, int seeker, int owner, String profilePic, String verificationPhoto){
            this.email = email;
            this.password = password;
            this.first = first;
            this.last = last;
            this.phone = phone;
            this.seeker = seeker;
            this.owner = owner;
            this.profilePic = profilePic;
            this.verificationPhoto = verificationPhoto;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("signup");
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
                            + profilePic + "&verificationPhoto=" + verificationPhoto;
                } else {
                    urlParameters = "email=" + email + "&password=" + password + "&first=" + first +
                            "&last=" + last + "&phone=" + phone + "&seeker=" + seeker + "&owner=" + owner +
                            "&verificationPhoto=" + verificationPhoto;
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
                    Log.i("STATE","inputLine: " + inputLine);
                }
                in.close();
                success = true;
                Log.i("STATE","success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE", "done = true");
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
    public static int signup(String email, String password, String first, String last, String phone, int seeker, int owner, String profilePic, String verificationPhoto) {
        SignUpTask s = new SignUpTask(email, password, first, last, phone, seeker, owner, profilePic, verificationPhoto);
        s.execute();
        Log.i("STATE","start waiting for sign up task");
        while(!s.done)
            Log.i("SPAM","1");
        Log.i("STATE","signup: done");
        if(s.success)
            return 200;
        else
            return 401;
    }

    static class ModifyUserTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        String password;
        String first;
        String last;
        String phone;
        int seeker;
        int owner;
        String profilePic;
        boolean done = false;
        boolean success = false;

        public ModifyUserTask(String email, String password, String first, String last, String phone, boolean seeker, boolean owner, String profilePic){
            this.email = email;
            this.password = password;
            this.first = first;
            this.last = last;
            this.phone = phone;
            this.seeker = seeker ? 1 : 0;
            this.owner = owner ? 1 : 0;
            this.profilePic = profilePic;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("modify/user");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;
//
//                if (profilePic != null) {
//                    urlParameters = "email=" + email + "&password=" + password + "&first=" + first +
//                            "&last=" + last + "&phone=" + phone + "&seeker=" + seeker + "&owner=" + owner + "&profilePic="
//                            + profilePic.toString();
//                } else {
                urlParameters = "";
                if (email != null)
                    urlParameters += "&email=" + email;
                if(password != null)
                    urlParameters += "&password=" + password;
                if(first != null)
                    urlParameters += "&first=" + first;
                if(last != null)
                    urlParameters += "&last=" + last;
                if(phone != null)
                    urlParameters += "&phone=" + phone;
                if(profilePic != null)
                    urlParameters += "&profilePic=" + profilePic;
                urlParameters += "&seeker=" + seeker + "&owner=" + owner;

                if (!urlParameters.equals((""))){
                    urlParameters = urlParameters.substring(1);
                    System.out.println("~~~~~~~~~~~~~"+urlParameters);
                }
//                }

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
    public static int modifyUser(SentUserDAO user) {
        ModifyUserTask s = new ModifyUserTask(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.isSeaker(), user.isOwner(), user.getProfilePic());
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
                String url = formatURL("book/spot");
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
    static class CreateSpotTask extends AsyncTask<Void,Void,Void>
    {
        SentSpotDAO spot;
        boolean done = false;
        boolean success = false;

        public CreateSpotTask(SentSpotDAO spot){
            this.spot = spot;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("post/spot");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "address=" + spot.getAddress() + "&spotType=" + spot.getSpotType()+ "&isCovered=" + spot.isCovered() +
                        "&cancelationPolicy=" + spot.getCancelationPolicy() + "&price=" + spot.getPrice() + "&start=" + spot.getStartTime()
                        + "&end=" + spot.getEndTime() + "&description=" + spot.getDescription() + "&isRecurring=" + spot.isRecurring()
                        + "&picture=" + spot.getPicture();

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

    public static int createSpot(SentSpotDAO spot){
        CreateSpotTask s = new CreateSpotTask(spot);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return 200;
        else
            return 401;
    }

    static class DeleteSpotTask extends AsyncTask<Void,Void,Void>
    {
        int spotID;
        boolean done = false;
        boolean success = false;

        public DeleteSpotTask(int spotID){
            this.spotID = spotID;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("post/spot");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "spotID=" + spotID;

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

    public static int deleteSpot(int spotID){
        DeleteSpotTask s = new DeleteSpotTask(spotID);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return 200;
        else
            return 401;
    }

    static class ViewSpotHistoryTask extends AsyncTask<Void,Void,Void>
    {
        List<Integer> spotIDs;
        String email;
        boolean done = false;
        boolean success = false;

        public ViewSpotHistoryTask(String email){
            this.email = email;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("view/history?email="+email);
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
                spotIDs = gson.fromJson(response.toString(), typeOfT);
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

    public static List<Integer> viewSpotHistory(String email) throws Exception {
        ViewSpotHistoryTask s = new ViewSpotHistoryTask(email);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return s.spotIDs;
        return null;
    }

    static class ViewRentalsTask extends AsyncTask<Void,Void,Void>
    {
        List<Integer> spotIDs;
        String email;
        boolean done = false;
        boolean success = false;

        public ViewRentalsTask(String email){
            this.email = email;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("view/rentals?email="+email);
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
                spotIDs = gson.fromJson(response.toString(), typeOfT);
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

    public static List<Integer> viewRentals(String email) throws Exception {
        ViewRentalsTask s = new ViewRentalsTask(email);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return s.spotIDs;
        return null;
    }

    static class CancelReservationTask extends AsyncTask<Void,Void,Void>
    {
        int spotID;
        boolean done = false;
        boolean success = false;

        public CancelReservationTask(int spotID){
            this.spotID = spotID;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("cancel/reservation");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "spotID=" + spotID;

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

    public static int cancelReservation(int spotID){
        CancelReservationTask s = new CancelReservationTask(spotID);
        s.execute();
        while(!s.done)
            ;
        if(s.success)
            return 200;
        else
            return 401;
    }

}