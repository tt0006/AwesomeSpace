package com.example.us.awesomespace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper methods related to parse response data
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor as no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() { }


    //Fetch json data code ------------------------------------------------------------------------
    /**
     * Return an {@link APOD} object that has been built up from
     * parsing a JSON response.
     */
    public static APOD extractAPODrequest(String jsonResponse) {

        APOD apod = null;

        //parse the SAMPLE_JSON_RESPONSE.
        try {

            JSONObject root = new JSONObject(jsonResponse);

            String title = root.optString("title");
            String explanation = root.optString("explanation");
            String hdurl = root.optString("hdurl");
            String url = root.optString("url");
            String date = root.optString("date");
            String mediaType = root.optString("media_type");
            apod = new APOD(title, explanation, hdurl, url, date, mediaType);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the APOD JSON results", e);
        }

        // Return the APOD object
        return apod;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";

        // If the URL is null, then return early.
        if (url == null) {
            return response;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(20000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** public method to fetch JSON data*/
    public static APOD fetchAPOD(String REQUEST_URL){
        // Create URL object
        URL url = createUrl(REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        String response = "";
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"IOException occurs:", e);
        }

        return extractAPODrequest(response);
    }

    /** public method to fetch APOD image*/
    public static Bitmap fetchImage(String REQUEST_URL){
        // Create URL object
        URL url = createUrl(REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        Bitmap response = null;
        try {
            response = downloadImage(url);
        } catch (Exception e) {
            Log.e(LOG_TAG,"Exception occurs:", e);
        }

        return response;
    }

    /** public method to fetch youtube thumbnail*/
    public static Bitmap fetchThumbnail(String REQUEST_URL){
        String id = extractYTId(REQUEST_URL);
        //Log.i("APODViewModelAct1", String.format("id: %s" ,id));

        String thumbUrl = String.format("https://img.youtube.com/vi/%s/0.jpg", id);
        //Log.i("APODViewModelAct1", String.format("url: %s" ,thumbUrl));

        // Create URL object
        URL url = createUrl(thumbUrl);

        Bitmap response = null;
        try {
            response = downloadImage(url);
        } catch (Exception e) {
            Log.e(LOG_TAG,"Exception occurs:", e);
        }

        return response;
    }

    private static Bitmap downloadImage(URL url){
        // Don't perform the request if the URL is null.
        if (url == null) {
            return null;
        }
        HttpURLConnection connection = null;
        try{
        // Initialize a new http url connection
        connection = (HttpURLConnection) url.openConnection();

        // Connect the http url connection
        connection.connect();

        // Get the input stream from http url connection
        InputStream inputStream = connection.getInputStream();

        // Initialize a new BufferedInputStream from InputStream
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        // Convert BufferedInputStream to Bitmap object
        Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

        // Return the downloaded bitmap
        return bmp;

    }catch(IOException e){
        e.printStackTrace();
    }finally{
        // Disconnect the http url connection
        connection.disconnect();
    }
            return null;

    }

    //helper method to extract youtube id from youtube url
    private static String extractYTId(String youtubeUrl) {
        String video_id = "";

        try {
            if(youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {
                String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
                // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
                //String expression = "^.*(?:youtu.be\\/|v\\/|e\\/|u\\/\\w+\\/|embed\\/|v=)([^#\\&\\?]*).*";
                CharSequence input = youtubeUrl;
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(input);
                if(matcher.matches()) {
                    String groupIndex1 = matcher.group(7);
                    if(groupIndex1 != null && groupIndex1.length() == 11)
                        video_id = groupIndex1;
                }
            }
        } catch(Exception e) {
            Log.e("QueryUtils", "extractYTId " + e.getMessage());
        }
        return video_id;
    }

}