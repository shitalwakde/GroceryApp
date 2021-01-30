package com.app.controller;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> resultList;
    private static final String LOG_TAG = "4WheelConsulting";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBnoJIRByzeo4qLjyQFXxou1iBeVC_vhes";

    Context context;
    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
//            sb.append("&types=establishment");
//            sb.append("&location=22.0796,82.1391");
//            sb.append("&radius=15000");
//            sb.append("&strictbounds");
            sb.append("&components=country:in");
            URL url = new URL(sb.toString());



//            URL url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/xml?input="+URLEncoder.encode(input, "utf8")+"&types=establishment&location=22.0796,82.1391&radius=25&key=AIzaSyBnoJIRByzeo4qLjyQFXxou1iBeVC_vhes");
//            URL url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/xml?input="+URLEncoder.encode(input, "utf8")+"&types=establishment&location=22.0796,82.1391&radius=500&strictbounds&key=AIzaSyBnoJIRByzeo4qLjyQFXxou1iBeVC_vhes");
            Log.d("TAG","url : "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.e(LOG_TAG, ">>>>>>" + jsonObj);
            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Log.d("TAG","url2 : "+predsJsonArray.getJSONObject(i));
                JSONObject jsonObject=predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting");
                Log.d("TAG","main_text : "+jsonObject.getString("main_text"));
                Log.d("TAG","secondary_text : "+jsonObject.getString("secondary_text"));
                Log.d("TAG","url2 : "+predsJsonArray.getJSONObject(i).getString("description"));
                Log.d("TAG","url3 : "+"============================================================");
                resultList.add(jsonObject.getString("main_text")+ "\n"+jsonObject.getString("secondary_text"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}

