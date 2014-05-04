package com.mposadar.googlemaps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity {
	
	private GoogleMap map;
	private static LatLng BOGOTA = new LatLng(4.60971,-74.08175);
	private static LatLng BARRANQUILLA = new LatLng(10.9642172, -74.7970353);
	private static LatLng MIAMI = new LatLng(25.789102, -80.20427819999998);
	private double latitude = 4.60971;
	private double longitude = -74.08175;
	private static int maxResults = 1;
	private LocationManager locManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // OBTENER UNA REFERENCIA AL LOCATION MANAGER
 		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
 		//CONOCER ULTIMA POSICION CONOCIDA
 		Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
 		
 		if (loc != null) {
 			latitude = loc.getLatitude();
 	 		longitude = loc.getLongitude();
		}

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setPadding(0, 50, 0, 0);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA, 14));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.addMarker(new MarkerOptions()
        .position(new LatLng(latitude, longitude))
        .title("Marker Bogotá")
        .snippet("Población 6,763 millones")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.iron)));
        
        // CLIC EN EL MARCADOR
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), arg0.getTitle(), Toast.LENGTH_LONG).show();
				return false;
			}
		});
        
        // DA LAS COORDENADAS DEL POLYLINE
        PolylineOptions rectOptions = new PolylineOptions()
        .add(BOGOTA)
        .add(BARRANQUILLA)
        .add(MIAMI);
        
        // DIBUJA EL POLYLINE
        Polyline polyline = map.addPolyline(rectOptions);
        
        // ACCEDER A DISTINTOS RECURSOS QUE NOS OFRECE LA GEOLOCALIZACION
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
        	
			List<Address> adreses = geocoder.getFromLocation(latitude, longitude, maxResults);
			// PAIS
			System.out.println(adreses.get(0).getCountryName());
			//DIRECCION
			System.out.println(adreses.get(0).getAddressLine(0));
			// CIUDAD - DEPARTAMENTO
			System.out.println(adreses.get(0).getAddressLine(1));
			// PAIS
			System.out.println(adreses.get(0).getAddressLine(2));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        registerLocation();
        
    }
    
    // CREAMOS UNA CLASE A PARTIR DEL OBJETO Criteria PARA
 	// QUE NOS INDIQUE QUE PROVIDER NOS DA MEJOR RESULTADO.
 	private Criteria getBestCriteria()
 	{
 		Criteria req = new Criteria();
 		req.setAccuracy(Criteria.ACCURACY_FINE);
 		req.setAltitudeRequired(true);

 		return req;
 	}
    
    //REGITRAR LA UBICACION
  	private void registerLocation()
  	{
  		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
  		String provider = locManager.getBestProvider(getBestCriteria(), true);
  		locManager.requestLocationUpdates(provider, 30000, 0, new MyLocationListener());
  	}
  	
  	//ESTA CLASE BUSCA LA LOCALIZACION ACTUALIZADA DEL DISPOSITIVO.
  	private class MyLocationListener implements LocationListener
  	{

  		@Override
  		public void onLocationChanged(Location location) {
  			// TODO Auto-generated method stub
  			LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
  			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
  		}

  		@Override
  		public void onProviderDisabled(String provider) {
  			// TODO Auto-generated method stub

  		}

  		@Override
  		public void onProviderEnabled(String provider) {
  			// TODO Auto-generated method stub

  		}

  		@Override
  		public void onStatusChanged(String provider, int status, Bundle extras) {
  			// TODO Auto-generated method stub

  		}

  	}

    
    public void MapaNormal(View view)
    {
    	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    
    public void MapaSatelite(View view)
    {
    	map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    
    public void MapaTerrain(View view)
    {
    	map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Toast.makeText(getApplicationContext(), "Click menu...", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
