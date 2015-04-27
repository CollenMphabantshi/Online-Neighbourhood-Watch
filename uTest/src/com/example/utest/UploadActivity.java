package com.example.utest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

//import org.apache.commons.codec.binary.Base64;
//import com.google.api.client.util.Base64;


import com.example.utest.R;
import com.example.utest.R.color;
import com.example.utest.R.id;
import com.example.utest.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemSelectedListener;

public class UploadActivity extends Activity{
	private static final String TAG = Main. class. getSimpleName() ;
	private ProgressBar progressBar;
	private String filePath,geolocation;
	private TextView txtPercentage,uploadedData,messageText;
	private Encryption encrypte;
	private ImageView imgPreview;
	private VideoView vidPreview;
	private Button btnUpload,uploadButton;
	private Spinner cases;
	private String typeOfCase = null;
	long totalSize = 0;
	 private String URL_UPLOAD = "http://192.168.43.15/10.50.64.78/uploader.php";
	int serverResponseCode = 0;
    ProgressDialog dialog = null;
    
    MessageDigest messageDigest;
	//style="?android:attr/progressBarStyleHorizontal"
	
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
		Toast. makeText(getApplicationContext() ,"*************************"+filePath, Toast. LENGTH_LONG). show() ;
		
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
		Toast. makeText(getApplicationContext() ,"Sorry, file path is missing! ", Toast. LENGTH_LONG) . show() ;
		}
		
		cases.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)view;
				
				if(!(tv.getText().toString()).equals("Select type of case:")){
					
					
					typeOfCase = cases.getSelectedItem().toString();
					
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
				Intent open = new Intent("com.example.utest.LOGGIN");
				startActivity(open);
				
				
			}
		});
		uploadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent open = new Intent("com.example.utest.REGISTER");
				startActivity(open);
				
				
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
     * @param imageDataString - a {@link java.lang.String}
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
	
	
    
    public String hashFile(File file) throws Exception{
        String algorithm = "SHA-256";
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(file);
        byte[] bb = new byte[1024];
        int br = -1;
        while((br = fis.read(bb)) != -1)
        {
            digest.update(bb, 0, br);
        }
        byte[] hib = digest.digest();
        String hash = convertToHexString(hib);
        return hash;
    }
    
    public static String convertToHexString(byte[] hb){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < hb.length;i++){
            sb.append(Integer.toString((hb[i] & 0xff)+0x100, 16).substring(1));
        }
        return sb.toString();
    }
	
	
	
	}	

