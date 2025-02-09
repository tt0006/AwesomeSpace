package com.example.us.awesomespace;

import android.content.Context;
import android.util.Log;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper methods related to parse response data
 */
final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Private constructor as no need to create {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() { }


    //Fetch json data code ------------------------------------------------------------------------
    /**
     * Return an {@link APOD} object that has been built up from
     * parsing a JSON response.
     */
    private static APOD extractAPODrequest(String jsonResponse) {

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
            Log.e(LOG_TAG, "Problem parsing the APOD JSON results", e);
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
            Log.e(LOG_TAG, "Problem retrieving the APOD JSON results.", e);
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
    static void fetchAPOD(Context context, String REQUEST_URL){
        // Create URL object
        URL url = createUrl(REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        String response = "";
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Exception occurs:", e);
        }

        APOD apodObj = extractAPODrequest(response);

        DataRepository.getInstance(context).insertData(apodObj);

    }

    /** helper method to extract youtube id from youtube url*/
    static String extractYTId(String youtubeUrl) {
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