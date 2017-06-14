package vn.cusc.mapdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MarkerOptions> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cantho = new LatLng(10.0359302, 105.7766715);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cantho));
        MarkerOptions marker = new MarkerOptions().position(cantho)
                .title("Marker in Can tho")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.vn));

        mMap.addMarker(marker);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cantho, 8));

        markers.add(marker);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.vn));

                markers.add(markerOptions);

                mMap.addMarker(markerOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i = 0; i < markers.size(); i++) {
                    builder.include(markers.get(i).getPosition());
                }
                LatLngBounds bounds = builder.build();
                CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(bounds, 10);
                mMap.animateCamera(camera);
            }
        });
    }
}
