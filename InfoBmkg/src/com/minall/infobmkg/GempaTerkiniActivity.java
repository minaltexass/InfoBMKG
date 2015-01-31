package com.minall.infobmkg;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.android.gms.internal.fo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GempaTerkiniActivity extends Activity {
	
	// flag for Internet connection status
    Boolean isInternetPresent = false;
    static final String TAG="Gempa Terkini";
    // Connection detector class
    ConnectionDetector cd;
    Bitmap bitmap;
    ImageView img;
    Double lat = -1.6166091799309863;
	Double lng = 103.62681995085461;
	
	static final String URL1 = "http://data.bmkg.go.id/autogempa.xml";
	static final String KEY_ITEM1 = "gempa";
	static final String KEY_ID1 = "Tanggal";
	static final String KEY_TANGGAL1 = "Tanggal";
	static final String KEY_JAM1 = "Jam";
	static final String KEY_POINT1 = "point";
	static final String KEY_KOORDINAT1 = "coordinates";
	static final String KEY_LINTANG1 = "lintang";
	static final String KEY_BUJUR1 = "bujur";
	static final String KEY_KEKUATAN_GEMPA1 = "Magnitude";
	static final String KEY_KEDALAMAN1 = "Kedalaman";
	static final String KEY_WILAYAH1 = "Wilayah1";
	static final String KEY_WILAYAH2 = "Wilayah2";
	static final String KEY_WILAYAH3 = "Wilayah3";
	static final String KEY_WILAYAH4 = "Wilayah4";
	static final String KEY_WILAYAH5 = "Wilayah5";
	static final String KEY_POTENSI = "Potensi";
	private ProgressDialog pDialog;
	ListView lv1;
	GoogleMap gempa;
	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gempa_terkini);
		
		/*
		gempa = ((MapFragment) getFragmentManager().findFragmentById(R.id.img_gempa1))
	    	.getMap();
		gempa.addMarker(new MarkerOptions().position(new LatLng(-7.841785, 110.469904)).title("Jambi City").snippet("Minal Juadli"));
		gempa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-7.841785, 110.469904),10));
		*/
		lv1=(ListView)findViewById(R.id.list_new);
		cd = new ConnectionDetector(getApplicationContext());
		// get Internet status
         isInternetPresent = cd.isConnectingToInternet();

         // check for Internet status
         if (isInternetPresent) {
             // Internet Connection is Present
             // make HTTP requests
             new AmbilData().execute();
         } else {
             // Internet connection is not present
             // Ask user to connect to Internet
            Intent no =new Intent(getApplicationContext(),NoInternetAccessActivity.class);
            startActivity(no);
            finish();
         }
         
	}

	class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GempaTerkiniActivity.this);
            pDialog.setMessage("Mengambil Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            Log.e(TAG, "Memuat Data");
        }
        
        protected String doInBackground(String... args) {

    		XMLParser parser = new XMLParser();
    		String xml = parser.getXmlFromUrl(URL1);
    		Document doc = parser.getDomElement(xml);

    		NodeList nl = doc.getElementsByTagName(KEY_ITEM1);
    		for (int i = 0; i < nl.getLength(); i++) {

            	HashMap<String, String> map = new HashMap<String, String>();
    			
    			Element e = (Element) nl.item(i);
    			
    			String koordinat = parser.getValue(e, KEY_KOORDINAT1);
    			String[] koor = koordinat.split(",");
    			
    			map.put(KEY_ID1, parser.getValue(e, KEY_ID1));
    			map.put(KEY_TANGGAL1, parser.getValue(e, KEY_TANGGAL1));
    			map.put(KEY_JAM1, parser.getValue(e, KEY_JAM1));
    			map.put(KEY_LINTANG1, "Garis Lintang : "+koor[1]);
    			map.put(KEY_BUJUR1, "Garis Bujur : "+ koor[0]);
    			map.put(KEY_KEKUATAN_GEMPA1, "Kekuatan Gempa : "+parser.getValue(e, KEY_KEKUATAN_GEMPA1));
    			map.put(KEY_KEDALAMAN1, "Kedalaman : "+ parser.getValue(e, KEY_KEDALAMAN1));
    			map.put(KEY_WILAYAH1, "Wilayah1 : "+parser.getValue(e, KEY_WILAYAH1));
    			map.put(KEY_WILAYAH2, "Wilayah2 : "+parser.getValue(e, KEY_WILAYAH2));
    			map.put(KEY_WILAYAH3, "Wilayah3 : "+parser.getValue(e, KEY_WILAYAH3));
    			map.put(KEY_WILAYAH4, "Wilayah4 : "+parser.getValue(e, KEY_WILAYAH4));
    			map.put(KEY_WILAYAH5, "Wilayah5 : "+parser.getValue(e, KEY_WILAYAH5));
    			map.put(KEY_WILAYAH1, "Wilayah : "+parser.getValue(e, KEY_WILAYAH1));
    			map.put(KEY_POTENSI, "Potensi : "+parser.getValue(e, KEY_POTENSI));
    			
    			menuItems.add(map);
    			Log.e(TAG, "Mengambil Data dari website");
    		}
			return null;
        }
        
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
        			
            		ListAdapter adapter = new SimpleAdapter(GempaTerkiniActivity.this, menuItems,
            				R.layout.daftar_gempaterkini,
            				new String[] { KEY_TANGGAL1, KEY_JAM1, KEY_LINTANG1, KEY_BUJUR1, KEY_KEKUATAN_GEMPA1, KEY_KEDALAMAN1, KEY_WILAYAH1,KEY_WILAYAH2,KEY_WILAYAH3,KEY_WILAYAH4,KEY_WILAYAH5,KEY_POTENSI }, 
            				new int[] {R.id.tanggal1, R.id.jam1, R.id.lintang1, R.id.bujur1,R.id.kekuatan1,R.id.kedalaman1,R.id.wilayah1,R.id.wilayah2,R.id.wilayah3,R.id.wilayah4,R.id.wilayah5,R.id.potensi });
            		 
            		lv1.setAdapter(adapter);
            		//ListView lv1 = getListView();
            		Log.e(TAG, "Menampilkan Data");
            		//Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_LONG).show();
            		
            		/*
            		String lat = ((TextView) findViewById(R.id.lintang1)).getText().toString();
            		String[] koorlin = lat.split(" : ");
            		String lng = ((TextView) findViewById(R.id.bujur1)).getText().toString();
            		String[] koorbuj = lng.split(" : ");
            		
            		//lat =String[](koorbuj);
            		
                    lng = Double.toString(GempaTerkiniActivity.this.lng);
            		*/
            		
            	    	
            		
            		/*
            		int count =lv.getCount();
            		for (int i = 0; i < count; i++) 
            		{
            			ViewGroup row=(ViewGroup)lv.getChildAt(i);
            			TextView tv=(TextView)row.findViewById(R.id.textView1);
					}
            		*/
            		
            		lv1.setOnItemClickListener(new OnItemClickListener() {
            			 
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
                        	
                            String lintang = ((TextView) view.findViewById(R.id.lintang1)).getText().toString();
                            String bujur = ((TextView) view.findViewById(R.id.bujur1)).getText().toString();
                            //String tanggal = ((TextView) view.findViewById(R.id.tanggal1)).getText().toString();
                            //String jam = ((TextView) view.findViewById(R.id.jam)).getText().toString();
                            //String kekuatan = ((TextView) view.findViewById(R.id.kekuatan)).getText().toString();
                            //String kedalaman = ((TextView) view.findViewById(R.id.kedalaman)).getText().toString();
                            String wilayah = ((TextView) view.findViewById(R.id.wilayah1)).getText().toString();
                            
                            Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_LONG).show();
                            Intent i = null;							
            				i = new Intent(GempaTerkiniActivity.this, PetaGempaTerkiniActivity.class);
                            
                            Bundle b = new Bundle();
            				b.putString("lintang", lintang);
            				b.putString("bujur", bujur);
            				//b.putString("tanggal", tanggal);
            				//b.putString("jam", jam);
            				//b.putString("kekuatan", kekuatan);
            				//b.putString("kedalaman", kedalaman);
            				b.putString("wilayah", wilayah);
            				i.putExtras(b);
                            startActivity(i);
                            
                        }
                    });
                }
            });
 		
        }
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gempa_terkini, menu);
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
