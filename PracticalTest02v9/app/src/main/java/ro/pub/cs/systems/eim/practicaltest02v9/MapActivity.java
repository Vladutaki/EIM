package ro.pub.cs.systems.eim.practicaltest02v9;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);  // Map activity layout

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);  // Initialize the map
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Ghelmegioaia coordinates (latitude, longitude)
        LatLng ghelmegioaia = new LatLng(45.5369, 25.2243);

        // Move camera to Ghelmegioaia
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghelmegioaia, 10));

        // Add a marker in Bucharest
        LatLng bucharest = new LatLng(44.4268, 26.1025);
        googleMap.addMarker(new MarkerOptions().position(bucharest).title("Bucharest"));

        // You can add other features to the map here
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();  // Resume the map view
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();  // Pause the map view
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();  // Destroy the map view
    }
}
