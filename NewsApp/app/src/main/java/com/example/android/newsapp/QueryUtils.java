package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;
import android.util.MalformedJsonException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * Helper methods related to requesting and receiving news data from Guardian API.
 * This class is only meant to hold static variables and methods which cannot be
 * Instantiated
 */
public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the Guardian API and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestURL) {
        // Creating URL object in order to establish connection with the
        // Guardian API
        URL url = createURL(requestURL);

        //Making the HTTP request and getting a json response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException ioEx) {
            Log.e(LOG_TAG, " Problem with making the HTTP request", ioEx);
        }

        // Extract queried fields from the JSON response and create a list of {@link News}
        List<News> list = getJsonData(jsonResponse);
        return list;
    }


    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> getJsonData(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) return null;

        //Creating an empty List and adding list items to it
        List<News> result = new ArrayList<>(  );

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject reader = new JSONObject(jsonResponse);
            JSONObject response = reader.getJSONObject("response");
            //System.out.println(response);

            JSONArray jsonArray = response.getJSONArray("results");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentNews = jsonArray.getJSONObject(i);
                String headline = currentNews.optString("webTitle");

                String category = currentNews.optString("pillarName");

                String thumbnail = currentNews.optString("webUrl");

                String publicationDate = currentNews.optString("webPublicationDate");


                JSONArray tags = currentNews.getJSONArray("tags");
                String author = "";

                if (tags.length() == 0) {
                    author = null;
                } else {
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject authorTag = tags.getJSONObject(j);
                        author += authorTag.optString("webTitle");
                    }
                }
                String date = publicationDate.substring( 5, 7 ) + "-" +
                        publicationDate.substring( 8, 10 ) + "-" +
                        publicationDate.substring( 2, 4 );

                //System.out.println(yearMonthDate);
                String time = publicationDate.substring(11, 16);
                //System.out.println(clock);
                News news = new News(time, date, category, headline, thumbnail, author);
                result.add(news);

            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem with parsin the json response", e);
        }

        return result;
    }


    /**
     * Making a HTTP request on the given URL and return a String response.
     * Course boilerplate
     */
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponder = "";

        //Fail fast, if url is null
        if (url == null) return jsonResponder;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponder = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException ioEx) {
            Log.e(LOG_TAG, "Problem retrieving Guardian API json result", ioEx);
        } finally {
            if (urlConnection != null) urlConnection.disconnect();

            if (inputStream != null) inputStream.close();
        }

        return jsonResponder;
    }

    /**
     * Converting the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     * Course boilerplate
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(  );
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append( line );
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns new URL object from the given request URL.
     * Course boilerplate
     */
    private static URL createURL(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException malUrlEx) {
            Log.e(LOG_TAG, "Problem with building the URL", malUrlEx);

        }
        return url;
    }
}
