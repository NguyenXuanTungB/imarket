package com.framgia.project1.demodiagramongooglemap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.framgia.project1.demodiagramongooglemap.data.model.Edge;
import com.framgia.project1.demodiagramongooglemap.data.model.Point;
import com.framgia.project1.demodiagramongooglemap.data.remote.DatabaseRemote;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    public static int statement = 0;
    Button control_but, delete_data, draw_path, find_way;
    DatabaseRemote remote;
    private GoogleMap mMap;
    private List<Point> nodes;
    private List<Edge> edges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        remote = new DatabaseRemote(this);
        try {
            remote.openDatabase();
        } catch (SQLDataException e) {
            e.printStackTrace();
        }
        nodes= remote.getListPoint();
        edges= remote.getListEdge();

        control_but = (Button) findViewById(R.id.show_dialog);
        delete_data = (Button) findViewById(R.id.delete_data);
        draw_path = (Button) findViewById(R.id.find_path);
        draw_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.statement = 2;
                Intent intent = new Intent(MapsActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });
        find_way=(Button)findViewById(R.id.demo_find_way);
        find_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edges.size()>0){
                    for(int i=0;i<edges.size();i++) {
                        LatLng first = remote.getPointFromId(edges.get(i).getIdStart());
                        LatLng second = remote.getPointFromId(edges.get(i).getIdEnd());
                        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(first, second).width(2));
                    }
                }
                if(nodes.size()>0){
                    List<Marker> markers = new ArrayList<Marker>();
                    for(int i=0;i<nodes.size();i++){
                        MarkerOptions options= new MarkerOptions().position(remote.getPointFromId(nodes.get(i).getId())).title("diem "+nodes.get(i).getId());
                        Marker marker= mMap.addMarker(options);
                        markers.add(marker);
                    }
                    for(int i=0;i<markers.size();i++){
                        markers.get(i).showInfoWindow();
                    }
                }

            }
        });
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                remote.deleteData();
                BitmapDescriptor BD = BitmapDescriptorFactory.fromResource(R.drawable.mbt2);
                LatLng bigC = new LatLng(21.007380, 105.793139);
                GroundOverlayOptions goo = new GroundOverlayOptions().image(BD).position(bigC, 116f, 150f).bearing((float) 53);
                GroundOverlay imageOverlay = mMap.addGroundOverlay(goo);
                mMap.addMarker(new MarkerOptions().position(bigC).title("Big C"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(bigC).zoom(18).build();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(bigC));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        double lat = latLng.latitude;
                        double lng = latLng.longitude;
                        long id = remote.savePoint(new Point(1, lat, lng));
                        LatLng point = new LatLng(lat, lng);
                        Marker locationMarket = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(" điểm " + id).snippet(" điểm " + id));
                        locationMarket.showInfoWindow();
                    }
                });
            }

        });

        control_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.statement = 1;
                Intent intent = new Intent(MapsActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setGroundOverLay();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                long id = remote.savePoint(new Point(1, lat, lng));
                LatLng point = new LatLng(lat, lng);
                Marker locationMarket = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(" điểm " + id).snippet(" điểm " + id));
                locationMarket.showInfoWindow();
            }
        });

        if(edges.size()>0){
            for(int i=0;i<edges.size();i++){
            }
        }
        List<Point> listPoint = new ArrayList<>();
    }
public void setGroundOverLay(){
    BitmapDescriptor BD = BitmapDescriptorFactory.fromResource(R.drawable.mbt2);
    LatLng bigC = new LatLng(21.007380, 105.793139);
    GroundOverlayOptions goo = new GroundOverlayOptions().image(BD).position(bigC, 116f, 150f).bearing((float) 53);

    GroundOverlay imageOverlay = mMap.addGroundOverlay(goo);
    mMap.addMarker(new MarkerOptions().position(bigC).title("Big C"));
    CameraPosition cameraPosition = new CameraPosition.Builder().target(bigC).zoom(18).build();
    mMap.moveCamera(CameraUpdateFactory.newLatLng(bigC));
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DialogActivity.draw == 1) {
            LatLng first = remote.getPointFromId(DialogActivity.firstPoint);
            LatLng second = remote.getPointFromId(DialogActivity.secondPoint);
            Polyline polyline = mMap.addPolyline(new PolylineOptions().add(first, second).width(2));
            float results[] = new float[1];
            Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, results);
            remote.saveEdge(new Edge(DialogActivity.firstPoint, DialogActivity.secondPoint, results[0]));
            Toast.makeText(MapsActivity.this, " " + remote.getListEdge().size(), Toast.LENGTH_LONG).show();
            DialogActivity.draw = 0;
        }
        if (DialogActivity.draw == 2) {

        }
    }
}