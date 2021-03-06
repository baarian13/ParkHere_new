package com.lazeebear.parkhere.ServerConnector;


import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotAddressDateDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotButtonDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.AddressDetailsDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentAddressDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentAddressDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentSpotDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentUserDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDateDAO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
                            String url = formatURL("search/spot/location?address="+ address.replace(' ', '+'));
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
                    Thread.sleep(100);//Log.i("SPAM","search");
                if(s.success)
                        return s.spots;
                return null;
            }

    static class SearchSpotDateTask extends AsyncTask<Void,Void,Void>
    {
        List<SpotDateDAO> spots;
        String start;
        String end;
        boolean done = false;
        boolean success = false;

        public SearchSpotDateTask(String start, String end){
            this.start = start;
            this.end = end;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("search/spot/date?start="+start+"&end="+end);
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
                Type typeOfT = new TypeToken<List<SpotDateDAO>>(){}.getType();
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

    public static List<SpotDateDAO> searchSpotDate(String start, String end) throws Exception {
        SearchSpotDateTask s = new SearchSpotDateTask(start,end);
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","search");
        if(s.success)
            return s.spots;
        return null;
    }

    //////////  search by both address and date ////////////
    static class SearchSpotAddressAndDateTask extends AsyncTask<Void,Void,Void>
    {
        List<SpotAddressDateDAO> spots;
        String address;
        String start;
        String end;
        boolean done = false;
        boolean success = false;

        public SearchSpotAddressAndDateTask(String address, String start, String end){
            this.address = address;
            this.start = start;
            this.end = end;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("search/spot/locationanddate?address="+ address.replace(' ', '+')+"&start="+start+"&end="+end);
//                String url = formatURL("search/spot/locationanddate?start="+start+"&end="+end);
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
                Type typeOfT = new TypeToken<List<SpotAddressDateDAO>>(){}.getType();
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

    public static List<SpotAddressDateDAO> searchSpotAddressAndDate(String address, String start, String end) throws Exception {
        SearchSpotAddressAndDateTask s = new SearchSpotAddressAndDateTask(address, start, end);
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","search");
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
            Thread.sleep(100);//Log.i("SPAM","getting spot");
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

    public static ReturnedUserDAO userDetails(String email) throws Exception {
        UserDetailsTask s = new UserDetailsTask(email);
        ReturnedUserDAO user;
        s.execute();
        Log.i("STATE","Waiting for user details");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","user details");
        Log.i("STATE","Finished waiting for user details");
        if(s.success) {
            user = s.user;



            return user;
        }
        return null;
    }

    static class SpotsOwnedTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        List<SpotButtonDAO> spots;
        boolean done = false;
        boolean success = false;

        public SpotsOwnedTask(String email){
            this.email = email;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("view/postings?email="+email);
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
                Type typeOfT = new TypeToken<List<SpotButtonDAO>>(){}.getType();
                spots = gson.fromJson(response.toString(), typeOfT);
                //print result
                success = true;
                Log.i("STATE","owned spots success = true");
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

    public static List<SpotButtonDAO> spotsOwned(String email) throws Exception {

        SpotsOwnedTask o = new SpotsOwnedTask(email);
        o.execute();
        while (!o.done)
            Thread.sleep(100);
        if (o.success) {
            return o.spots;
        }

        return new ArrayList<>();
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
                Log.i("STATE","Check user - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","Check user - done = true");
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
        Log.i("STATE","waiting for check user to finish");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","check user");
        Log.i("STATE","finished checking user");
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

    public static boolean signin(String email, String password) throws Exception {
        SignInTask s = new SignInTask(email, password);
        s.execute();
        Log.i("STATE","Waiting for signin");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","sign in");
        Log.i("STATE","Finished signin");
        return s.success;
    }

    static class ContactServiceTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        String message;
        boolean done = false;
        boolean success = false;

        public ContactServiceTask(String email, String message){
            this.email = email;
            this.message = message;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("contact/service");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "email=" + email + "&message=" + message;

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

    public static boolean contactService(String email, String message) throws Exception {
        ContactServiceTask s = new ContactServiceTask(email, message);
        s.execute();
        Log.i("STATE","Waiting for signin");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","sign in");
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
    public static int signup(String email, String password, String first, String last, String phone, int seeker, int owner, String profilePic, String verificationPhoto) throws Exception {
        SignUpTask s = new SignUpTask(email, password, first, last, phone, seeker, owner, profilePic, verificationPhoto);
        s.execute();
        Log.i("STATE","start waiting for sign up task");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","sign up");
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
                Log.i("STATE","modify user - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","modify user - done = true");
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
    public static int modifyUser(SentUserDAO user) throws Exception{
        ModifyUserTask s = new ModifyUserTask(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.isSeaker(), user.isOwner(), user.getProfilePic());
        s.execute();
        Log.i("STATE","waiting for modify user");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","modify user");
        Log.i("STATE","finished waiting for modify user");
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

                urlParameters = "email=" + spot.getEmail() + "&address=" + spot.getAddress() + "&spotType=" + spot.getSpotType()+ "&isCovered=" + spot.isCovered() +
                        "&cancelationPolicy=" + spot.getCancelationPolicy() + "&price=" + spot.getPrice() + "&start=" + spot.getStartTime()
                        + "&end=" + spot.getEndTime() + "&description=" + spot.getDescription() + "&isRecurring=" + spot.isRecurring()
                        + "&picture=" + spot.getPicture() + "&addressID=" +spot.getAddressID();

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

    public static int createSpot(SentSpotDAO spot) throws Exception{
        CreateSpotTask s = new CreateSpotTask(spot);
        s.execute();
        while(!s.done)
            Thread.sleep(100);
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
                String url = formatURL("delete/spot");
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

    public static int deleteSpot(int spotID) throws Exception{
        DeleteSpotTask s = new DeleteSpotTask(spotID);
        s.execute();
        while(!s.done)
            Thread.sleep(100);
        if(s.success)
            return 200;
        else
            return 401;
    }

    //using id + address instead of just address
    static class ViewSpotHistoryTask extends AsyncTask<Void,Void,Void>
    {
        //List<Integer> spotIDs = new ArrayList<>();
        List<SpotButtonDAO> spots = new ArrayList<>();
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
                System.out.println("Grabbed buffer");
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Getting gson object..");
                Gson gson = new Gson();
                //Type typeOfT = new TypeToken<List<List<Integer>>>(){}.getType();
                //List<List<Integer>> spotID_temp = gson.fromJson(response.toString(), typeOfT);
                Type typeOfT = new TypeToken<List<SpotButtonDAO>>(){}.getType();
                spots = gson.fromJson(response.toString(), typeOfT);
                /*
                int size = spotID_temp.size();
                System.out.println("Returned object size: " + size);
                for (int i=0; i<size; i++){
                    spotIDs.add(spotID_temp.get(i).get(0));
                }*/
                //print result
                success = true;
                Log.i("STATE","view spot history - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","view spot history - done = true");
            return null;
        }


        protected void onPostExecute(Void result) {

            // dismiss progress dialog and update ui
        }
    }

    public static List<SpotButtonDAO> viewSpotHistory(String email) throws Exception {
        ViewSpotHistoryTask s = new ViewSpotHistoryTask(email);
        s.execute();
        Log.i("STATE","waiting for view spot history task");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","view spot history");
        Log.i("STATE","finished waiting for view spot history");
        if(s.success)
            return s.spots;
        return null;
    }

    static class ViewRentalsTask extends AsyncTask<Void,Void,Void>
    {
        List<SpotButtonDAO> spots = new ArrayList<>();
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
                Type typeOfT = new TypeToken<List<SpotButtonDAO>>(){}.getType();
                spots = gson.fromJson(response.toString(), typeOfT);
                /*int size = spotID_temp.size();
                System.out.println("Returned object size: " + size);
                for (int i=0; i<size; i++){
                    spots.add(spotID_temp.get(i));
                }*/
                //print result
                success = true;
                Log.i("STATE","view rentals - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","view rentals - done = true");
            return null;
        }


        protected void onPostExecute(Void result) {

            // dismiss progress dialog and update ui
        }
    }

    public static List<SpotButtonDAO> viewRentals(String email) throws Exception {
        ViewRentalsTask s = new ViewRentalsTask(email);
        s.execute();
        Log.i("STATE","Waiting for view rentals task");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","view rentals");
        Log.i("STATE","finished waiting for view rentals");
        if(s.success)
            return s.spots;
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

    public static int cancelReservation(int spotID) throws Exception{
        CancelReservationTask s = new CancelReservationTask(spotID);
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","cancel reservation");
        if(s.success)
            return 200;
        else
            return 401;
    }

    static class RateUserTask extends AsyncTask<Void,Void,Void>
    {
        String email;
        int rating;
        boolean done = false;
        boolean success = false;

        public RateUserTask(String email, int rating){
            this.email = email;
            this.rating = rating;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("rate/user");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "email=" + email + "&rating=" + rating;

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

    public static int rateUser(String email, int rating) throws Exception{
        RateUserTask s = new RateUserTask(email, rating);
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","rate user");
        if(s.success)
            return s.rating; //return 200?
        else
            return 401;
    }



    static class RateSpotTask extends AsyncTask<Void,Void,Void>
    {
        int spotId;
        int rating;
        boolean done = false;
        boolean success = false;

        public RateSpotTask(int spotId, int rating){
            this.spotId = spotId;
            this.rating = rating;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("rate/spot");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "spotId=" + spotId + "&rating=" + rating;

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

    public static int rateSpot(int spotId, int rating) throws Exception{
        RateSpotTask s = new RateSpotTask(spotId, rating);
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","rate user");
        if(s.success)
            return 200;
        else
            return 401;
    }

    // TODO: retrieve the addressID of the newly created address from the Server.
    static class CreateAddressTask extends AsyncTask<Void,Void,Void>{
        String address, ownerEmail, description , picture;
        int spotType, isCovered, addressID;
        boolean done = false, success = false;

        public CreateAddressTask(String address, String ownerEmail, String description, int spotType,
                                 int isCovered, String picture){
            this.address = address;
            this.ownerEmail = ownerEmail;
            this.description = description;
            this.spotType = spotType;
            this.isCovered = isCovered;
            this.picture = picture;
        }

        protected void onPreExecute() {}
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("create/address"); //TODO where does this URL come from?
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "email=" + ownerEmail + "&address=" + address +
                                "&description=" + description + "&spotType=" + spotType +
                                "&isCovered=" + isCovered + "&picture=" + picture;

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
        protected void onPostExecute() {}
    }

    // return the addressID of the newly created address
    public static int createAddress(String address, String ownerEmail, String description,
                                     int spotType, int isCovered, String picture) throws Exception {
        CreateAddressTask s = new CreateAddressTask(address, ownerEmail, description, spotType, isCovered, picture);
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","rate user");
        if(s.success)
            return 200; //s.addressID
        else
            return 401;
    }

    static class GetAddressesTask extends AsyncTask<Void,Void,Void>{
        String ownerEmail;
        List<SpotButtonDAO> addresses = new ArrayList<>();
        boolean done = false, success = false;

        public GetAddressesTask(String ownerEmail){
            this.ownerEmail = ownerEmail;
        }

        protected void onPreExecute() {}
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("get/address?email="+ownerEmail); //TODO
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
                System.out.println("Grabbed buffer");
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Getting gson object..");
                Gson gson = new Gson();
                Type typeOfT = new TypeToken<List<SpotButtonDAO>>(){}.getType();
                addresses = gson.fromJson(response.toString(), typeOfT);
                int size = addresses.size();
                System.out.println("Returned object size: " + size);
                /*for (int i=0; i<size; i++){
                    addresses.add(addresses_temp.get(i));
                }*/
                //print result
                success = true;
                Log.i("STATE","view spot history - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            return null;
        }
        protected void onPostExecute() {}
    }

    public static List<SpotButtonDAO> getAddressesOf(String email) throws Exception{
        //return returnPlaceholderAddressList();
        GetAddressesTask s = new GetAddressesTask(email);
        s.execute();
        while (!s.done)
            Thread.sleep(100); //Log.i("SPAM", "get addresses of user")
        if (s.success)
            return s.addresses;
        return null;
    }

    private static List<Integer> returnPlaceholderAddressList() {
        List<Integer> placeholder = new ArrayList<>();
        placeholder.add(1);
        return placeholder;
    }
    static class DeleteAddressTask extends AsyncTask<Void,Void,Void>
    {
        int addressID;
        String email;
        boolean done = false;
        boolean success = false;

        public DeleteAddressTask(int addressID, String email){
            this.addressID = addressID;
            this.email = email;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("delete/address");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;

                urlParameters = "addressID=" + addressID + "&email=" + email;

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

    public static int deleteAddress(int addressID, String email) throws Exception{
        DeleteAddressTask s = new DeleteAddressTask(addressID, email);
        s.execute();
        while(!s.done)
            Thread.sleep(100);
        if(s.success)
            return 200;
        else
            return 401;
    }


    static class ModifyAddressTask extends AsyncTask<Void,Void,Void>
    {
        int addressID;
        String address;
        String picture;
        String description;
        String email;
        int spotType;
        int isCovered;
        boolean done = false;
        boolean success = false;

        public ModifyAddressTask(int addressID, String address, String picture, String description, String email, int spotType, int isCovered) {
            this.addressID = addressID;
            this.address = address;
            this.picture = picture;
            this.description = description;
            this.email = email;
            this.spotType = spotType;
            this.isCovered = isCovered;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("modify/address");
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
                if (picture != null)
                    urlParameters += "&picture=" + picture;
                if(description != null)
                    urlParameters += "&description=" + description;
                if(address != null)
                    urlParameters += "&address=" + address;
                urlParameters += "&addressID=" + addressID + "&spotType=" + spotType + "&isCovered=" + isCovered;

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
                Log.i("STATE","modify user - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","modify user - done = true");
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
    public static int modifyAddress(SentAddressDAO address) throws Exception{
        ModifyAddressTask s = new ModifyAddressTask(address.getAddressID(), address.getAddress(), address.getPicture(), address.getDescription(), address.getEmail(), address.getSpotType(), address.getIsCovered());
        s.execute();
        Log.i("STATE","waiting for modify user");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","modify user");
        Log.i("STATE","finished waiting for modify user");
        if(s.success)
            return 200;
        else
            return 401;
    }

    static class AddressDetailsTask extends AsyncTask<Void,Void,Void>
    {
        String addressID;
        AddressDetailsDAO address;
        boolean done = false;
        boolean success = false;

        public AddressDetailsTask(String id){
            this.addressID = id;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("view/address?addressID="+addressID);
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
                Type typeOfT = new TypeToken<AddressDetailsDAO>(){}.getType();
                address = gson.fromJson(response.toString(), typeOfT);
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

    public static AddressDetailsDAO AddressDetails(Integer addressID) throws Exception {
        AddressDetailsTask s = new AddressDetailsTask(addressID+"");
        s.execute();
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","getting spot");
        if(s.success)
            return s.address;
        return null;
    }

    static class ModifyCancelationPolicyTask extends AsyncTask<Void,Void,Void>
    {
        int spotID;
        int cPolicy;
        boolean done = false;
        boolean success = false;

        public ModifyCancelationPolicyTask(int spotID, int cPolicy) {
            this.spotID = spotID;
            this.cPolicy = cPolicy;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("modify/cancelationpolicy");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = null;
                urlParameters = "spotID=" + spotID + "&cPolicy=" + cPolicy;

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
                Log.i("STATE","modify cancelation policy - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","modify cancelation policy - done = true");
            return null;
        }

        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

    public static int modifyCancelationPolicy(int spotID, int cPolicy) throws Exception{
        ModifyCancelationPolicyTask s = new ModifyCancelationPolicyTask(spotID, cPolicy);
        s.execute();
        Log.i("STATE","waiting for modify price");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","modify user");
        Log.i("STATE","finished waiting for modify price");
        if(s.success)
            return 200;
        else
            return 401;
    }

    static class ModifyPriceTask extends AsyncTask<Void,Void,Void>
    {
        int spotID;
        String price;
        boolean done = false;
        boolean success = false;

        public ModifyPriceTask(int spotID, String price) {
            this.spotID = spotID;
            this.price = price;
        }

        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            try {
                String url = formatURL("modify/price");
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
                urlParameters = "spotID=" + spotID + "&price=" + price;
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
                Log.i("STATE","modify price - success = true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            done = true;
            Log.i("STATE","modify price - done = true");
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
    public static int modifyPrice(int spotID, String price) throws Exception{
        ModifyPriceTask s = new ModifyPriceTask(spotID, price);
        s.execute();
        Log.i("STATE","waiting for modify price");
        while(!s.done)
            Thread.sleep(100);//Log.i("SPAM","modify user");
        Log.i("STATE","finished waiting for modify price");
        if(s.success)
            return 200;
        else
            return 401;
    }
}