package com.minall.infobmkg;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends ActionBarActivity {
Button gempa,history,cuaca,about;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		gempa=(Button)findViewById(R.id.btn_gempa);
		history=(Button)findViewById(R.id.btn_history);
		cuaca=(Button)findViewById(R.id.btn_ramalan);
		about=(Button)findViewById(R.id.btn_about);
		
		history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aa = new Intent(getApplicationContext(), InfoBmkgActivity.class);
				startActivity(aa);
			}
		});
		gempa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent bb = new Intent(getApplicationContext(), GempaTerkiniActivity.class);
				startActivity(bb);
			}
		});
		
		cuaca.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cuaca = new Intent(getApplicationContext(),PerkiraanCuacaActivity.class);
				startActivity(cuaca);
			}
		});
		
		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent about = new Intent(getApplicationContext(),AboutActivity.class);
				startActivity(about);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
	public void keluar(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Apakah Anda akan keluar?");
		builder.setTitle("Keluar..");
		builder.setIcon(R.drawable.gagal)
		.setCancelable(false)
		.setPositiveButton("Ya",new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog,int id) {
		    	  //session.logoutUser();
		    	  finish();
		            }
		      })
		.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog,int id) {
		                  dialog.cancel();
		            }
		      }).show();
		}	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
         keluar();
        }   
        return super.onKeyDown(keyCode, event);
     }
}
