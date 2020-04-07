package com.example.fuelmonitoring.user.fragments.mapdirectory;

import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fuelmonitoring.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyStationsData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    private  String placeName, vicinity, rating;

    public static double maxrating = 0.0;
    public static String maxratingnm, maxratingvic;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("\nData from app.....", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser dataParser = new DataParser();
        nearbyPlaceList = dataParser.parse(s);

        showNearbyPlaces(nearbyPlaceList);
        //showNotification(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {

            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

            placeName = googlePlace.get("place_name");
            vicinity = googlePlace.get("vicinity");

            Log.d("MarkerType ","Name: "+placeName+"Types: " + googlePlace.get("type"));

            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
    }

    /*
    public  void  showNotification(List<HashMap<String, String>> nearbyPlacesList){


        for (int i = 0; i < nearbyPlacesList.size(); i++) {

            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

            placeName = googlePlace.get("place_name");
            vicinity = googlePlace.get("vicinity");
            rating = googlePlace.get("rating");

            Log.d("MarkerType ","Name: "+placeName+"Rating: " + rating);

            if(Double.parseDouble(rating ) > maxrating){
                maxrating = Double.parseDouble(rating);
                maxratingnm = placeName;
                maxratingvic = vicinity;
            }
        }
    }
    */
}
