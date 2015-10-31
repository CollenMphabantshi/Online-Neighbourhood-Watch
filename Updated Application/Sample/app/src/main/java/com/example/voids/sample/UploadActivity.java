package com.example.voids.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.codec.binary.Base64;
//import com.google.api.client.util.Base64;

public class UploadActivity extends Activity {
	private static final String TAG = Main. class. getSimpleName() ;
	private ProgressBar progressBar;
	private String filePath = "",geolocation;
	private TextView txtPercentage,uploadedData,messageText;
	private Encryption encrypte;
	private ImageView imgPreview;
	private VideoView vidPreview;
	private Button btnUpload,uploadButton;
	private Spinner cases, txtLocation;
	private String typeOfCase = null;
	long totalSize = 0;
	 private String URL_UPLOAD = "http://192.168.173.1/content/uploader.php";
	int serverResponseCode = 0;
    ProgressDialog dialog = null;
    
    MessageDigest messageDigest;
	//style="?android:attr/progressBarStyleHorizontal"
	private String response = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.upload);
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		txtPercentage = (TextView) findViewById(R. id. txtPercentage) ;
		uploadedData = (TextView) findViewById(R.id.uploadedData);
		btnUpload = (Button) findViewById(R. id. btnUpload) ;
		progressBar = (ProgressBar) findViewById(R. id. progressBar) ;
		imgPreview = (ImageView) findViewById(R. id. imgPreview) ;
		vidPreview = (VideoView) findViewById(R. id. videoPreview) ;
		cases = (Spinner) findViewById(R.id.cases);
        txtLocation = (Spinner) findViewById(R.id.location);
		messageText= (TextView) findViewById(R.id.uploadStatus);
		uploadButton = (Button)findViewById(R.id.uploadButton);
	     messageText  = (TextView)findViewById(R.id.uploadStatus);


		encrypte = new Encryption();
		// Changing action bar background color
		//getActionBar() . setBackgroundDrawable(	new ColorDrawable(Color. parseColor(getResources() . getString(	R. color. action_bar) ) ) ) ;
		
		// Receiving the data from previous activity
		Intent i = getIntent() ;
		
		// image or video path that is captured in previous activity
		filePath = i. getStringExtra("filePath") ;
		//Toast. makeText(getApplicationContext(), "*************************" + filePath, Toast.LENGTH_LONG). show() ;

		geolocation = i.getStringExtra("GeoLocation");
		
		// boolean flag to identify the media type, image or video
		boolean isImage = i. getBooleanExtra("isImage", true) ;
		if (filePath != null) {
		// Displaying the image or video on the screen
			try{
				//uploadedData.setText(converImageToString(filePath,true));
				messageDigest.update(typeOfCase.getBytes());
				String tp= new String(messageDigest.digest());
				messageDigest.update(filePath.getBytes());
				String fl= new String(messageDigest.digest());
				messageDigest.update(geolocation.getBytes());
				String gl= new String(messageDigest.digest());
				
				uploadedData.setText("Case: "+ tp+"\n file: "+ fl+"\n geoLocation: "+ gl);

			}catch(Exception e){
				e.printStackTrace();
			}
			
		previewMedia(isImage);
		} else {
		Toast. makeText(getApplicationContext(), "Sorry, file path is missing! ", Toast.LENGTH_LONG) . show() ;
		}
		
		cases.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
                try {
                    TextView tv = (TextView) view;

                    if (!(tv.getText().toString()).equals("Select type of case:")) {


                        typeOfCase = cases.getSelectedItem().toString();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Please select a type of case.",Toast.LENGTH_LONG);
                }
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                try {
                    Intent open = new Intent("com.example.voids.sample.LOGGIN");
                    open.putExtra("filePath", filePath);
                    open.putExtra("caseType", (String) cases.getSelectedItem());
                    open.putExtra("caseLocation", (String) txtLocation.getSelectedItem());
                    startActivity(open);

                }catch (Exception e){
                    e.printStackTrace();
                }

				
			}
		});
		uploadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                try {
                    Intent open = new Intent("com.example.voids.sample.REGISTER");
                    open.putExtra("filePath", filePath);
                    open.putExtra("caseType", (String) cases.getSelectedItem());
                    open.putExtra("caseLocation", (String) txtLocation.getSelectedItem());
                    startActivity(open);

                }catch (Exception e){
                    e.printStackTrace();
                }
			}
		});
	         
	    
    }
		
		
	
	
	public void startUpload(){
		
		
	}


	/**
     * Encodes the byte array into base64 string
     *
     * @param imageByteArray - byte array
     * @return String a {@link java.lang.String}
     */
    public static String encodeImage(byte[] imageByteArray) {

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    /**
     * Decodes the base64 string into byte array
     *
     * @param  - a {@link java.lang.String}
     * @return byte array
     */
    /*public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }*/

	private void previewMedia(boolean isImage) {
		// Checking whether captured media is image or video
		if (isImage) {
		imgPreview. setVisibility(View. VISIBLE) ;
		vidPreview. setVisibility(View. GONE) ;
		// bimatp factory
		BitmapFactory. Options options = new BitmapFactory. Options() ;
		// down sizing image as it throws OutOfMemory Exception for larger
		// images
		options. inSampleSize = 8;
		final Bitmap bitmap = BitmapFactory. decodeFile(filePath, options) ;
		imgPreview. setImageBitmap(bitmap) ;
		} else {
		imgPreview. setVisibility(View. GONE) ;
		vidPreview. setVisibility(View. VISIBLE) ;
		vidPreview. setVideoPath(filePath) ;
		// start playing
		vidPreview. start() ;
		}
		}
	
	
    

}

