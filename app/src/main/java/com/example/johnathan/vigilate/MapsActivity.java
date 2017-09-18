package com.example.johnathan.vigilate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker myPosition;
    double lat = 0.0;
    double lng = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Obtiene la ubicación actual, crea un marker en esa posición en google maps
        //y monitorea los cambios en la ubicación cada 15 segundos
        findLocation();

    }

    //Método para agregar markador en el mapa
    public void addMarker(double lat, double lng) {
        LatLng coordinates = new LatLng(lat, lng);
        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinates,16);
        //Sí el marcador ya existe, se elimina
        if (myPosition != null) {
            myPosition.remove();
        }
        //recordar cambiar el icono de este marcador
        myPosition = mMap.addMarker(new MarkerOptions().position(coordinates).title("Mi ubicacipon"));
        //cambia la posición de la camara a la ubicación entrada por parametro

        mMap.animateCamera(myLocation);

        mMap.setMaxZoomPreference(100);
    }

    private void updateLocation(Location location) {
        //En caso que la location sea null, no se cerrará acutomáticamente la aplicación
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            addMarker(lat, lng);
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //Método para encontrar la ubicación actual del usuario
    private void findLocation() {

        //Se agregan permisos para poder tomar la posición actual
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //tomamos la ubicación actual
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //la actualizamos por medio del método hecho anteriormete
        updateLocation(location);
        //indicamos que actualice la ubicación actual cada 15 segundos
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locListener);
    }

}
