package com.minall.infobmkg;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PetaGempaTerkiniActivity extends FragmentActivity {
	@SuppressWarnings("unused")
	private GoogleMap map;
	String tanggal,jam,kekuatan,kedalaman,wilayah;
	Double lat = -6.29436;
	Double lng = 106.8859;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peta_gempa_terkini);
		 map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView))
		    		.getMap();
		 
		 try {
		    	
		    	Bundle b = getIntent().getExtras();

	            String koordinatLintang = b.getString("lintang");
	            String[] koorlin = koordinatLintang.split(" : ");
	            String koordinatBujur = b.getString("bujur");
	            String[] koorbuj = koordinatBujur.split(" : ");
	            
	            lat = Double.parseDouble(koorlin[1]);
	            lng = Double.parseDouble(koorbuj[1]);
	            
	            wilayah = b.getString("wilayah");
	         
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		 final LatLng posisi = new LatLng(lat,lng);
		    @SuppressWarnings("unused")
			Marker lokasi = map.addMarker(new MarkerOptions()
		    		.position(posisi)
		    	    .title("Informasi Lokasi")
		            .snippet(wilayah)
		            .icon(BitmapDescriptorFactory
		            .fromResource(R.drawable.marker)));
		   	 
		          	// Move the camera instantly to hamburg with a zoom of 15.
		   	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, 6));
		   	 
		           // Zoom in, animating the camera.
		    	    map.animateCamera(CameraUpdateFactory.zoomTo(6), 1500, null);
		    	    
		    	    map.setOnMapClickListener(new OnMapClickListener() {
					
					@Override
					public void onMapClick(LatLng arg0) {
						// TODO Auto-generated method stub
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.peta_gempa_terkini, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
