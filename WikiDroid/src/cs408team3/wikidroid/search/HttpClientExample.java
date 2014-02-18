/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs408team3.wikidroid.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.util.Log;

public class HttpClientExample {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String API_KEY = "AIzaSyAaRUVbkeSktuHiFFru6lMlC7SbS7ju5gA";
    private final String SEARCH_ENGINE_ID = "015353232511339500776:ppncxs5ywr4";
    private final String QUERY_FIELDS = "items(displayLink,link,title)";

    private final static String test = "{ \"items\": [  {   \"title\": \"Brazil - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil (disambiguation) - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_(disambiguation)\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil national football team - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_national_football_team\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil, Indiana - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil,_Indiana\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil (1985 film) - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_(1985_film)\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazilian - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazilian\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil women's national basketball team - Wikipedia, the free ...\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_women's_national_basketball_team\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil national baseball team - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_national_baseball_team\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil national futsal team - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_national_futsal_team\",   \"displayLink\": \"en.wikipedia.org\"  },  {   \"title\": \"Brazil national beach soccer team - Wikipedia, the free encyclopedia\",   \"link\": \"http://en.wikipedia.org/wiki/Brazil_national_beach_soccer_team\",   \"displayLink\": \"en.wikipedia.org\"  } ]}";
    /*
    //&fields=items(displayLink%2Clink%2Ctitle)
    public static void main(String[] args) throws Exception {

        HttpClientExample http = new HttpClientExample();

       // String url = "http://www.google.com/search?q=developer";
       // System.out.println("Sending request to: "+url); 

        System.out.println("Testing 1 - Send Http GET request");
		//http.sendGet(url);
        String result = http.searchGoogle("brazil");
        System.out.println(result);
        
        ArrayList<QueryContentHolder> parsed = http.JSONToArray(result);
        System.out.println(parsed);
	//System.out.println("\nTesting 2 - Send Http POST request");
        //http.sendPost();

    }
*/
    /**
     * Parse a string in format of JSON to a arrayList that each elements contains the title, link and displaylink.
     * @param JSONString
     * @return 
     */
    public ArrayList<QueryContentHolder> JSONToArray(String JSONString) {
        // parse the string result to an JSONObject
        Object parsed = JSONValue.parse(JSONString);
        JSONObject json = (JSONObject) parsed;

       // System.out.println("KEYS : " + json.keySet());
       // System.out.println("ITEMS : \n" + json.get("items"));
        /* get the element items, this element have the title, link and displaylink*/
        JSONArray items = (JSONArray) json.get("items");
        ArrayList<QueryContentHolder> linksFounded = null;

        // if none item are found, return null
        if (items.size() > 0) {
            linksFounded = new ArrayList<QueryContentHolder>(items.size());
        } else {
            return null;
        }

        for (Object item : items) {
            JSONObject aux = (JSONObject) item;

            linksFounded.add(new QueryContentHolder((String) aux.get("title"),
                    (String) aux.get("link"), (String) aux.get("displayLink")));
            
        }

        return linksFounded;

    }

    public String searchGoogle(String content) {
        String result = null;
            //example
        //https://www.googleapis.com/customsearch/v1?q=brasil&cref=*.wikipedia.org&cx=015353232511339500776%3Appncxs5ywr4&key=AIzaSyAaRUVbkeSktuHiFFru6lMlC7SbS7ju5gA
        String query = "https://www.googleapis.com/customsearch/v1?"
                + "key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID + "&q=" + content + "&cref=*.wikipedia.org/*"
                + "&fields=" + QUERY_FIELDS;
        Log.i("search","Custom request: \n" + query);
        for (int i = 0; i < 2; i++){
	        try {
	            result = sendGet(query);
	            return result;
	        } 
	        catch(IllegalArgumentException ex){
	        	Log.i("HTTPGET",ex.getMessage());
	            Logger.getLogger(HttpClientExample.class.getName()).log(Level.SEVERE, null, ex);
	            result = "wrong url";
	            return result;
	        }
	        catch (ClientProtocolException ex) {
	        	Log.i("HTTPGET", ex.getMessage());
	            Logger.getLogger(HttpClientExample.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        catch(IOException ex){
	        	Log.i("HTTPGET",ex.getMessage());
	            Logger.getLogger(HttpClientExample.class.getName()).log(Level.SEVERE, null, ex);
	            result = "IOException";
	            return result;
	        }
	        
        }
        return null;
    }
    
    // HTTP GET request
    private String sendGet(String URL) throws IllegalArgumentException, IOException, ClientProtocolException  {

        //CloseableHttpClient client = HttpClients.createDefault();
    	HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);
        // add request header
        //request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        Log.i("search","\nSending 'GET' request to URL : " + URL);
        Log.i("search","Response Code : "
                + response.getStatusLine().getStatusCode()+"\n");

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        //String res = result.toString().split("\n")[1];
        //System.out.println("\n"+result.toString());

        return result.toString();
    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

   
}
