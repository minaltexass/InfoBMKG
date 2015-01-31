package com.minall.infobmkg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.GoogleMap;
import com.minall.infobmkg.GempaTerkiniActivity.AmbilData;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PerkiraanCuacaActivity extends ActionBarActivity {
	// flag for Internet connection status
    Boolean isInternetPresent = false;
     String aa;
    // Connection detector class
    ConnectionDetector cd;
    
    static final String TAG="Perkiraan Cuaca";
    
    static final String URL2 = "http://data.bmkg.go.id/cuaca_indo_1.xml";
	static final String KEY_ITEM2 = "Row";
	static final String KEY_ID2 = "Kota";
	static final String KEY_CUACA = "Cuaca";
	static final String KEY_JAM2 = "Jam";
	static final String KEY_SUHU_MIN = "SuhuMin";
	static final String KEY_SUHU_MAX = "SuhuMax";
	static final String KEY_KELEMBAPAN_MIN = "KelembapanMin";
	static final String KEY_KELEMBAPAN_MAX = "KelembapanMax";
	
	private ProgressDialog pDialog;
	ListView lv1;
	TextView tgl;
	GoogleMap gempa;
	
	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perkiraan_cuaca);
		lv1=(ListView)findViewById(R.id.list_cuaca);
		tgl=(TextView)findViewById(R.id.tgl_skrg);
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
         
         Calendar c1 = Calendar.getInstance();
 		 SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy");
 		 String strdate1 = sdf1.format(c1.getTime());
 		 tgl.setText(strdate1);
	}
	
	class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PerkiraanCuacaActivity.this);
            pDialog.setMessage("Mengambil Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            Log.e(TAG, "Memuat Data");
        }
        
        protected String doInBackground(String... args) {

    		XMLParser parser = new XMLParser();
    		String xml = parser.getXmlFromUrl(URL2);
    		Document doc = parser.getDomElement(xml);

    		NodeList nl = doc.getElementsByTagName(KEY_ITEM2);
    		for (int i = 0; i < nl.getLength(); i++) {

            	HashMap<String, String> map = new HashMap<String, String>();
    			
    			Element e = (Element) nl.item(i);
    			

    			
    			map.put(KEY_ID2, parser.getValue(e, KEY_ID2));
    			map.put(KEY_CUACA, parser.getValue(e, KEY_CUACA));
    			map.put(KEY_JAM2, parser.getValue(e, KEY_JAM2));
    			map.put(KEY_SUHU_MIN, "Suhu Min : "+parser.getValue(e, KEY_SUHU_MIN));
    			map.put(KEY_SUHU_MAX, "Suhu Max : "+ parser.getValue(e, KEY_SUHU_MAX));
    			map.put(KEY_KELEMBAPAN_MIN, "Kelembapan Min : "+parser.getValue(e, KEY_KELEMBAPAN_MIN));
    			map.put(KEY_KELEMBAPAN_MAX, "Kelembapan Max : "+parser.getValue(e, KEY_KELEMBAPAN_MAX));
    			 
    			menuItems.add(map);
    			Log.e(TAG, "Mengambil Data dari website");
    		}
			return null;
        }
        
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            String cuaca;
            runOnUiThread(new Runnable() {
                public void run() {
        			
            		ListAdapter adapter = new SimpleAdapter(PerkiraanCuacaActivity.this, menuItems,
            				R.layout.daftar_cuaca,
            				new String[] { KEY_ID2, KEY_CUACA, KEY_JAM2, KEY_SUHU_MIN, KEY_SUHU_MAX, KEY_KELEMBAPAN_MIN, KEY_KELEMBAPAN_MAX }, 
            				new int[] {R.id.kota, R.id.cuaca, R.id.suhu_min, R.id.suhu_max,R.id.kelembapan_min,R.id.kelembapan_max });
            		 
            		lv1.setAdapter(adapter);
            		//ListView lv1 = getListView();
            		Log.e(TAG, "Menampilkan Data");
            		
            		 /*
        			if (cuaca == ("Hujan Ringan")){
        				ImageView img =(ImageView)findViewById(R.id.img_cuaca);
        				img.setImageResource(R.drawable.markerr);
        			}
        			else if (cuaca == ("Hujan Sedang")){
        				ImageView img =(ImageView)findViewById(R.id.img_cuaca);
        				img.setImageResource(R.drawable.maps);
        			}
            		*/
            		lv1.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							//TODO Auto-generated method stub
							//Toast.makeText(getApplicationContext(), "Respon?" + aa.toString(), Toast.LENGTH_LONG).show();
						}
					});
                }
            });
 
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perkiraan_cuaca, menu);
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
