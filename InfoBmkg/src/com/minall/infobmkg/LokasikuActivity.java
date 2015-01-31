package com.minall.infobmkg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LokasikuActivity extends FragmentActivity {
	GoogleMap googlemap;
	MarkerOptions markerOptions;
	LatLng latLng;
	LatLng prevLatLng = null;
	Marker marker = null;
	ArrayList<LatLng> arrline = new ArrayList<LatLng>();
	private LocationManager locManager;
	private LocationListener locListener;
	private ProgressDialog pDialog;
	private GoogleMap map;
	Double lat = -6.29436;
	Double lng = 106.8859;
	Button CariLokasi;
	String tanggal,jam,kekuatan,kedalaman,wilayah;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lokasiku);
		FragmentManager fragmentManager = getSupportFragmentManager();
		SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager
				.findFragmentById(R.id.mapcurrloc);

		// Getting a reference to the map
		googlemap = supportMapFragment.getMap();
		lokasi_gempa();
		initLocationManager();
	}
	public void lokasi_gempa(){
		try {
	    	
	    	Bundle b = getIntent().getExtras();

            String koordinatLintang = b.getString("lintang");
            String[] koorlin = koordinatLintang.split(" : ");
            String koordinatBujur = b.getString("bujur");
            String[] koorbuj = koordinatBujur.split(" : ");
            
            lat = Double.parseDouble(koorlin[1]);
            lng = Double.parseDouble(koorbuj[1]);

            tanggal = b.getString("tanggal");
            jam = b.getString("jam");
            kekuatan = b.getString("kekuatan");
            kedalaman = b.getString("kedalaman");
            wilayah = b.getString("wilayah");
         
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	    final LatLng posisi = new LatLng(lat,lng);
	    Marker lokasi = googlemap.addMarker(new MarkerOptions()
	    		.position(posisi)
	    	    .title("Informasi Lokasi")
	            .snippet(wilayah)
	            .icon(BitmapDescriptorFactory
	            .fromResource(R.drawable.marker)));
	   	 
	          	// Move the camera instantly to hamburg with a zoom of 15.
	    		//googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, 6));
	   	 
	           	// Zoom in, animating the camera.
	    		//googlemap.animateCamera(CameraUpdateFactory.zoomTo(6), 1500, null);
	    	  
	}

	/**
	 * Initialize the location manager.
	 */
	private void initLocationManager() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locListener = new LocationListener() {
			// method ini akan dijalankan apabila koordinat GPS berubah
			public void onLocationChanged(Location newLocation) {

				displayCurrentLoctoMap(newLocation);
			}

			public void onProviderDisabled(String arg0) {

			}

			public void onProviderEnabled(String arg0) {
				Location location = locManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				displayCurrentLoctoMap(location);
			}

			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
		};
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locListener);
		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		displayCurrentLoctoMap(location);
	}

	/**
	 * This method will be called when current position changed is submitted via
	 * the GPS.
	 *
	 * @param newLocation
	 */
	protected void displayCurrentLoctoMap(Location newLocation) {
		try {
			LatLng currlok = new LatLng(newLocation.getLatitude(),
					newLocation.getLongitude());

			if (marker != null)
				marker.remove();
			markerOptions = new MarkerOptions().position(currlok)
					.title("Lokasi sekarang")
					.snippet(wilayah)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerr));

			googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(currlok, 4));
			 // call and execute ReversGeocoding Task
              new ReverseGeocodingTask(getBaseContext()).execute(currlok);

		} catch (NullPointerException e) {
			Toast.makeText(LokasikuActivity.this,"Tidak bisa mencari lokasi, silahkan check GPS atau koneksi internet anda",
					Toast.LENGTH_LONG).show();
		}
	}

	private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>{
		
        Context mContext;

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;

            List<Address> addresses = null;
            String addressText="";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getLocality(),
                address.getCountryName());
            }
            else
            {
            	Address address = addresses.get(0);
            	addressText=String.format("Alamat anda belum terdaftar",
            					address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
            	                address.getLocality(),
            	                address.getCountryName());
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting the snippet for the marker.
            // This will be displayed on taping the marker
        	//Log.d("TAG","Alamat:"+addressText);
            markerOptions.snippet(addressText);
            marker = googlemap.addMarker(markerOptions);

        }
    }

}