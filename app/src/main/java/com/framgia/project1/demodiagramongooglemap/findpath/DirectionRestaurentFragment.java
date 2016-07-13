package com.framgia.project1.demodiagramongooglemap.findpath;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.framgia.project1.demodiagramongooglemap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Do Van Khanh on 11/25/2015.
 */
public class DirectionRestaurentFragment extends Fragment implements GoogleMap.OnMyLocationChangeListener{
    private AlertDialog alertDialog ;
    private ConnectionDetector cd;
    private boolean isConnect= false;
    private ProgressDialog progressDialog;
    private GoogleMap map;
    private String name, address;
    private ArrayList<LatLng> markerPoints;
    private Double latRes, longRes;
    private boolean done= false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_activity_direction_restaurent, container, false);

        cd = new ConnectionDetector(getActivity());
        isConnect = cd.isConnectToInternet();
        progressDialog = new ProgressDialog(getActivity());
        alertDialog= new AlertDialog.Builder(getActivity()).create();
        SupportMapFragment fm = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(
            R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(this);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))Ơ
        Criteria criteria = new Criteria();
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
      //  location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if(isConnect==true){
            try {
                Bundle bundle = this.getArguments();
                latRes = Double.parseDouble(bundle.getString("latitude"));
                longRes = Double.parseDouble(bundle.getString("longtitude"));
                name = bundle.getString("name");
                address = bundle.getString("address");
            } catch (Exception e) {
                Toast.makeText(getContext(), "Lỗi Intent", Toast.LENGTH_SHORT).show();
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    progressDialog.setTitle("Đang tải dữ liệu");
                    progressDialog.setMessage("Xin bạn chờ 1 chút");
                    progressDialog.show();

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.002489,105.846728), 14));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(21.002489,105.846728)).bearing(90).zoom(15).tilt(30).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    LatLng origin = new LatLng(21.002489,105.846728);
                    LatLng dest = new LatLng(latRes, longRes);
                    //crete option for origin
                    MarkerOptions options = new MarkerOptions();
                    options.title("điểm xuất phát");
                    options.snippet("vị trí của bạn");
                    options.position(new LatLng(21.002489,105.846728));
                    // create option for target
                    MarkerOptions optionstarget = new MarkerOptions();
                    optionstarget.title(name);
                    optionstarget.snippet(address);
                    optionstarget.position(dest);
                    Marker marker, marker2;
                    marker = map.addMarker(optionstarget);
                    marker2 = map.addMarker(options);
                    marker.showInfoWindow();
                    marker2.showInfoWindow();

                    String url = getDirectionsUrl(origin, dest);
                    // Sensor enabled

                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);


                    markerPoints = new ArrayList<LatLng>();
                    map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            progressDialog.cancel();
                            Toast.makeText(getActivity(), "ok", Toast.LENGTH_LONG).show();
                        }
                    });

                // Getting reference to SupportMapFragment of the activity_main
            } else {

                alertDialog.setTitle("Bạn chưa bật GPS");
                alertDialog.setMessage("Hãy bật và làm lại từ đầu");
                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getActivity().finish();
                    }
                });
                alertDialog.show();
            }
        }
        else{
            alertDialog.setTitle("Kết nối có vấn đề");
            alertDialog.setMessage("Hãy kiểm tra lại wifi hoặc dữ liệu mạng của bạn");
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //getActivity().finish();
                }
            });
            alertDialog.show();
        }
        return rootView;
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        // Origin of route
        String str_origin = "origin=21.002489,105.846728";
        // Destination of route
        String str_dest = "destination="+latRes+","+longRes;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        // Building thdrawe parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        // Output format
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMyLocationChange(Location location) {

    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog= new ProgressDialog(getActivity());
    }
}
