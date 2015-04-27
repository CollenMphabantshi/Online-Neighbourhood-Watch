package com.example.utest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;


import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaDrmException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends Activity implements OnMyLocationChangeListener{
	
	// LogCat tag
	private static final String TAG = Main.class. getSimpleName() ;
	
	// Camera activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	private Uri fileUri; // file url to store image/video
	private LinearLayout btnCapturePicture, btnRecordVideo;
	
	private LinearLayout picVidAud;
	
	int status;
	String location,ImageNameOrVideoName=null;
	
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		try{
			LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
			boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!enabled) {
				  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				  Toast.makeText(this, "Enabled :" + enabled, Toast.LENGTH_SHORT).show();
				  startActivity(intent);
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		
		try{
			initializeLocationVariable();
			initialize();
			onclicks();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		if(!isDeviceSupportCamera()){
			Toast.makeText(getApplicationContext(), "Sorry no camera", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	public void initialize(){
		btnCapturePicture = (LinearLayout)findViewById(R.id.btnCapturePicture);
		btnRecordVideo = (LinearLayout)findViewById(R.id.btnRecordVideo);
		
		//picVidAud = (LinearLayout)findViewById(R.id.picVidAud);
	}
	
	public void initializeLocationVariable(){
		
		if( status != ConnectionResult.SUCCESS){
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}else{
			
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragId)).getMap();
			map.setMyLocationEnabled(true);
			map.setOnMyLocationChangeListener(this);
			
		}
	
		
}
	
	public void onclicks(){
		
		
		
		btnCapturePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureImage();
			}
		});
		
		btnRecordVideo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recordVideo();
			}
		});
		
	}
	
	private boolean isDeviceSupportCamera(){
		if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			return true;
		}else{
			return false;
		}
		
	}
	
	private void captureImage(){
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		
		
		startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		
	}
	
	private void recordVideo(){
		Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
		i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		
		
		startActivityForResult(i, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
	}
	
	

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		fileUri = savedInstanceState.getParcelable("file_uri");
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putParcelable("file_uri", fileUri);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// launching upload activity
				launchUploadActivity(true) ;
				
			} else if (resultCode == RESULT_CANCELED) {
			// user cancelled Image capture
				Toast. makeText(getApplicationContext() ,"User cancelled image capture", Toast. LENGTH_SHORT). show() ;
			} else {
			// failed to capture image
				Toast. makeText(getApplicationContext() ,	"Sorry! Failed to capture image", Toast. LENGTH_SHORT)	. show() ;
				}
			} 
			else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
				// video successfully recorded
				// launching upload activity
				launchUploadActivity(false) ;
				} else if (resultCode == RESULT_CANCELED) {
				// user cancelled recording
					launchUploadActivity(false) ;
					Toast. makeText(getApplicationContext() ,"User cancelled video recording", Toast. LENGTH_SHORT)	. show() ;
				} else {
				// failed to record video
					
					Toast. makeText(getApplicationContext() ,"Sorry! Failed to record video", Toast. LENGTH_SHORT). show() ;
					}
			}
		
	}


	private void launchUploadActivity(boolean b) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Main.this, UploadActivity.class);
		i.putExtra("filePath", fileUri.getPath());
		Toast. makeText(getApplicationContext() ,fileUri.getPath(), Toast. LENGTH_SHORT). show() ;
		
		i.putExtra("GeoLation", location);
		i.putExtra("isImage", b);
		startActivity(i);
		
	}


	private Uri getOutputMediaFileUri(int mediaTypeImage) {
		// TODO Auto-generated method stub
		return Uri.fromFile(getOutputMediaFile(mediaTypeImage));
	}


	private File getOutputMediaFile(int type) {
		// TODO Auto-generated method stub
		File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),Config.IMAGE_DIRECTORY_NAME);
		if(!mediaStorage.exists()){
			
			if(!mediaStorage.mkdirs()){
				
				Log.d(TAG, "Failed to create"+Config.IMAGE_DIRECTORY_NAME+" driectory");
				return null;
			}
			
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale. getDefault() ) . format(new Date() ) ;
				File mediaFile;
				if (type == MEDIA_TYPE_IMAGE) {
					
				mediaFile = new File(mediaStorage. getPath() + File. separator + "IMG_" + timeStamp + ". jpg") ;
				ImageNameOrVideoName =mediaFile.getPath();
				
				} else if (type == MEDIA_TYPE_VIDEO) {
					
				mediaFile = new File(mediaStorage. getPath() + File. separator + "VID_" + timeStamp + ". mp4") ;
				ImageNameOrVideoName =mediaFile.getPath();
				
				} else {
				return null;
				}
		
		return mediaFile;
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMyLocationChange(Location loc) {
		// TODO Auto-generated method stub
		String time,date;
		double longitude,latitude;
		try {
			JSONObject locate = new JSONObject();
    		JSONObject object = new JSONObject();
    		//get geolactions, time and date
    		longitude = loc.getLongitude();
    		latitude = loc.getLatitude();
    		Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            time = formattedDate.substring(11);
            date = formattedDate.substring(0, formattedDate.length()-9);
         
            String add = "";
            try{
            	add = getAddress(longitude,latitude);
            }catch(Exception ex){
            	add = "Sorry i tried";
            }
            
			locate.accumulate("Longitude", longitude);
			locate.accumulate("Latitude", latitude);
			locate.accumulate("Bearing", loc.getBearing());
			locate.accumulate("Altitude", loc.getAltitude());
			locate.accumulate("Accuracy", loc.getAccuracy());
			locate.accumulate("Address",add);
			
			object.accumulate("Time", time);
			object.accumulate("Date", date);
			object.accumulate("Location", locate.toString());
			
			location = object.toString();

			//value.setText(location);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public String getAddress(double longi, double lati){
		String myAddress = "";
		 Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

	       try {
	  List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
	 
	  if(addresses != null) {
	   Address returnedAddress = addresses.get(0);
	   StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
	   for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
	   }
	   myAddress = strReturnedAddress.toString();
	  }
	  else{
	   myAddress = "No Address returned!";
	  }
	 } catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		  myAddress = "Cannot get Address!";
	 }
	       
	       return myAddress;
	}
}
