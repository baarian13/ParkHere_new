
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class TestClientJava {

	private final String USER_AGENT = "Mozilla/5.0";

	static final String COOKIES_HEADER = "Set-Cookie";
	
	static CookieManager msCookieManager = new CookieManager();
	
	public static void main(String[] args) throws Exception {

		TestClientJava http = new TestClientJava();

		http.signIn("default12@test.com", "password");
		http.searchForSpots("707 West 28th street, Los Angeles CA, 90007");
	}
	
	static void setConnCookies(HttpURLConnection con){
		if (msCookieManager.getCookieStore().getCookies().size() > 0) {
		    // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
			String c = "";
			for(HttpCookie cookie : msCookieManager.getCookieStore().getCookies()){
				c += cookie.toString() + ";";
			}
			c = c.substring(0, c.length()-1);
			System.out.println(c);
			con.setRequestProperty("Cookie", c);    
		}
	}
	
	// HTTP POST request
	private void searchForSpots(String address) throws Exception {

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

		//print result
		System.out.println(response.toString());
	}

	// HTTP POST request
	private void signIn(String email, String password) throws Exception {

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
		System.out.println(response.toString());
	}

}