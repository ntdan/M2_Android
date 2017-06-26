package vn.cusc.mapdemo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MarkerOptions> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                if (markers.size() == 2) {
                    mMap.clear();
                    markers.clear();
                } else {
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.vn));
                    markers.add(markerOptions);
                    mMap.addMarker(markers.get(markers.size() - 1));

                    if (markers.size() == 2) {

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (int i = 0; i < markers.size(); i++) {
                            builder.include(markers.get(i).getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(bounds, 10);
                        mMap.animateCamera(camera);

                        veduongdi(markers.get(0).getPosition(), markers.get(1).getPosition());
                    }
                }
                /*
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
                mMap.animateCamera(camera);*/
            }
        });
    }

    public void veduongdi(LatLng tu, LatLng den) {
        veduongdixml a = new veduongdixml();
        a.execute(tu.latitude, tu.longitude, den.latitude, den.longitude);
    }

    class veduongdixml extends AsyncTask<Double, Void, Void> {
        ArrayList<LatLng> mangtoado;
        String kc = "";

        @Override
        protected Void doInBackground(Double... params) {
            Direction md = new Direction();
            LatLng x = new LatLng(params[0], params[1]);
            LatLng y = new LatLng(params[2], params[3]);
            Document doc = md.getDocument(x, y, Direction.MODE_DRIVING);
            mangtoado = md.getDirection(doc);

            kc = md.getDistanceText(doc);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            PolylineOptions rectLine = new PolylineOptions().width(5).color(
                    Color.BLUE); // Màu và độ rộng
            // di chuyen camera chua toan bo marker
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < mangtoado.size(); i++) {
                rectLine.add(mangtoado.get(i));
                builder.include(mangtoado.get(i));
            }
            mMap.addPolyline(rectLine);
            // Tìm đường bao tất cả các marker
            LatLngBounds bounds = builder.build();
            CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(bounds, 10);
            mMap.animateCamera(camera);

            Toast.makeText(MapsActivity.this, "KC:" + kc, Toast.LENGTH_SHORT).show();
        }
    }
}
