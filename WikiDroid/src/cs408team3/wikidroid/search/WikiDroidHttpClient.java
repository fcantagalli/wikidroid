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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class WikiDroidHttpClient {

    private static final String     TAG              = "WikiDroidHttpClient";

    private static final String     USER_AGENT       = "Mozilla/5.0";
    private static final String     API_KEY          = "AIzaSyAaRUVbkeSktuHiFFru6lMlC7SbS7ju5gA";
    private static final String     SEARCH_ENGINE_ID = "015353232511339500776:ppncxs5ywr4";
    private static final String     QUERY_FIELDS     = "items(displayLink,link,title)";

    private final AndroidHttpClient mHttpClient;

    public WikiDroidHttpClient() {
        mHttpClient = AndroidHttpClient.newInstance(USER_AGENT);
    }

    /**
     * Parse a string in format of JSON to a arrayList that each elements
     * contains the title, link and displaylink.
     *
     * @param JSONString
     * @return
     */
    public ArrayList<QueryContentHolder> JSONToArray(String JSONString) {
        // parse the string result to an JSONObject
        Log.v(TAG, JSONString);
        try {
            JSONObject json = new JSONObject(JSONString);

            /*
             * get the element items, this element have the title, link and
             * displaylink
             */
            JSONArray items = (JSONArray) json.get("items");
            ArrayList<QueryContentHolder> linksFounded = null;

            // if none item are found, return null
            if (items != null && items.length() > 0) {
                linksFounded = new ArrayList<QueryContentHolder>(items.length());
            } else {
                return null;
            }

            for (int i = 0; i < items.length(); i++) {
                try {
                    JSONObject aux = items.getJSONObject(i);
                    linksFounded.add(new QueryContentHolder((String) aux.get("title"), (String) aux.get("link"), (String) aux.get("displayLink")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return linksFounded;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String searchGoogle(String content) {
        String result = null;
        // example
        // https://www.googleapis.com/customsearch/v1?q=brasil&cref=*.wikipedia.org&cx=015353232511339500776%3Appncxs5ywr4&key=AIzaSyAaRUVbkeSktuHiFFru6lMlC7SbS7ju5gA
        String query = "";
        Uri q = new Uri.Builder()
                .scheme("https")
                .authority("www.googleapis.com")
                .path("/customsearch/v1")
                .appendQueryParameter("key", API_KEY)
                .appendQueryParameter("cx", SEARCH_ENGINE_ID)
                .appendQueryParameter("q", content)
                .appendQueryParameter("cref", "*.wikipedia.org/*")
                .appendQueryParameter("fields", QUERY_FIELDS)
                .build();
        query = q.toString();
        Log.i("search", "Custom request: \n" + query);
        for (int i = 0; i < 2; i++) {
            try {
                result = sendGet(query);
                return result;
            } catch (IllegalArgumentException ex) {
                Log.i("HTTPGET", ex.getMessage());
                Logger.getLogger(WikiDroidHttpClient.class.getName()).log(Level.SEVERE, null, ex);
                result = "wrong url";
                return result;
            } catch (ClientProtocolException ex) {
                Log.i("HTTPGET", ex.getMessage());
                Logger.getLogger(WikiDroidHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Log.i("HTTPGET", ex.getMessage());
                Logger.getLogger(WikiDroidHttpClient.class.getName()).log(Level.SEVERE, null, ex);
                result = "IOException";
                return result;
            }

        }
        return null;
    }

    // HTTP GET request
    public String sendGet(String URL) throws IllegalArgumentException, IOException, ClientProtocolException {
        HttpGet request = new HttpGet(URL);

        HttpResponse response = mHttpClient.execute(request);

        Log.i("search", "\nSending 'GET' request to URL : " + URL);
        Log.i("search", "Response Code : "
                + response.getStatusLine().getStatusCode() + "\n");

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    public void close() {
        mHttpClient.close();
    }

}
