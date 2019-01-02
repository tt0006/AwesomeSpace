package com.example.us.awesomespace;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Helper methods related to parse response data
 */
public final class QueryUtils {

    /** Sample JSON response */
    private static final String SAMPLE_JSON_RESPONSE = "{\"date\":\"2019-01-01\",\"explanation\":\"This floating ring is the size of a galaxy.  In fact, it is a galaxy -- or at least part of one: the photogenic Sombrero Galaxy, one of the largest galaxies in the nearby Virgo Cluster of Galaxies.  The dark band of dust that obscures the mid-section of the Sombrero Galaxy in optical light actually glows brightly in infrared light.  The featured image, digitally sharpened, shows the infrared glow, recently recorded by the orbiting Spitzer Space Telescope, superposed in false-color on an existing image taken by NASA's Hubble Space Telescope in optical light. The Sombrero Galaxy, also known as M104, spans about 50,000 light years across and lies 28 million light years away.  M104 can be seen with a small telescope in the direction of the constellation Virgo.   News: New Horizons Spacecraft Passes Ultima Thule\",\"hdurl\":\"https://apod.nasa.gov/apod/image/1901/sombrero_spitzer_3000.jpg\",\"media_type\":\"image\",\"service_version\":\"v1\",\"title\":\"The Sombrero Galaxy in Infrared\",\"url\":\"https://apod.nasa.gov/apod/image/1901/sombrero_spitzer_1080.jpg\"}";

    /**
     * Create a private constructor as no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return an {@link APOD} object that has been built up from
     * parsing a JSON response.
     */
    public static APOD extractAPODrequest() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<APOD> APODs = new ArrayList<>();

        //parse the SAMPLE_JSON_RESPONSE.
        try {

            JSONObject root = new JSONObject(SAMPLE_JSON_RESPONSE);

            String title = root.optString("title");
            String explanation = root.optString("explanation");
            String hdurl = root.optString("hdurl");
            APODs.add(new APOD(title,explanation,hdurl));

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the APOD JSON results", e);
        }

        // Return the APOD object
        return APODs.get(0);
    }

}