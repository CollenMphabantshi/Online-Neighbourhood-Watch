package com.example.voids.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loggin extends Activity {
	private EditText username,password;
	private Button loggin;
	private TextView goregister;
	private Pattern pattern;
    private Matcher matcher;

    private String response = "";
    private String filePath = "", caseType ="", caseLocation = "";
    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private String json = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loggin);
		try{
			intialise();
			onClicks();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	public void intialise(){
		username = (EditText) findViewById(R.id.usernameId);
		goregister = (TextView) findViewById(R.id.goregister);
		password = (EditText) findViewById(R.id.passwordId);
		loggin = (Button) findViewById(R.id.logginId);
		pattern = Pattern.compile(EMAIL_PATTERN);

        try {

            filePath = getIntent().getStringExtra("filePath");
            caseType = getIntent().getStringExtra("caseType");
            caseLocation = getIntent().getStringExtra("caseLocation");
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	
	public void onClicks() {
		loggin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validatefields()){
                    try {
                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        new LoginRequest().execute(pairs);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
				}else{
					Toast.makeText(getApplicationContext(), "Sorry, all fields must be field.", Toast.LENGTH_LONG);
				}
				
			}
		});
		
		goregister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                try {
                    Intent open = new Intent("com.example.voids.sample.REGISTER");
                    open.putExtra("filePath", filePath);
                    startActivity(open);
                }catch (Exception e){
                    e.printStackTrace();
                }
			}
		});
		
	}
	
	public boolean validatefields(){
		
		if(username.getText().toString().trim().length() == 0 || !validateEmail(username.getText().toString().trim()) ){
			username.requestFocus();
			username.setError("sorry, invalid email");
			return false;
		}
		
		if(password.getText().toString().trim().length() == 0){
			password.requestFocus();
			password.setError("sorry empty field");
			return false;
		}
		
		return true;
	}
	
	public boolean validateEmail(String str){
		matcher = pattern.matcher(str);
		return matcher.matches();
	}



    public void startUpload(){
        try {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            new RequestSender().execute(pairs);
        }catch(Exception e){e.printStackTrace();}
    }


    public String hashFile(File file) throws Exception {
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
            sb.append(Integer.toString((hb[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


    public void processResponse(){

        try{
            Intent open = new Intent("com.example.voids.sample.UPLOADTOSERVER");
            open.putExtra("response",response);
            open.putExtra("identity",username.getText().toString());
            startActivity(open);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String request(String url,  List<NameValuePair> pairs) throws Exception {
        try{
            /**
             * Get Captured File location
             */

            HttpPost httppost = new HttpPost(url);
            File file = new File(filePath);
            if(file.exists()) {
                FileBody capturedFile = new FileBody(file);
                StringBody hash = new StringBody(hashFile(file));
                MultipartEntity en = new MultipartEntity();
                en.addPart("capturedfile", capturedFile);
                en.addPart("hash", hash);
                en.addPart("username",new StringBody(username.getText().toString()));
                en.addPart("caseType",new StringBody(caseType));
                en.addPart("caseLocation",new StringBody(caseLocation));
                httppost.setEntity(en);

                System.out.println("executing request " + httppost.getRequestLine());
                DefaultHttpClient httpclient = (DefaultHttpClient) WebServiceWrapper.getNewHttpClient();

                HttpResponse res = httpclient.execute(httppost);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(res.getStatusLine());
                    BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
                    String line = "";
                    StringBuffer sb = new StringBuffer();
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    return sb.toString();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }else{
                Log.e("Debug", "Request Failed.");
            }


        }catch (Exception e){
            Log.e("Debug", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }



    public class RequestSender extends AsyncTask<List<NameValuePair>, Integer,String> {

        @Override
        protected String doInBackground(List<NameValuePair>... params) {
            // TODO Auto-generated method stub
            try {
                response = request(Config.URL_UPLOAD, params[0]);
                //response = request("http://192.168.173.1:8080/uWatch-war/api/", params[0]);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                // if successful go to loggin
                processResponse();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public String login(String url,  List<NameValuePair> pairs)
            throws Exception {

        StringBody usern = new StringBody(username.getText().toString());
        StringBody pass = new StringBody(password.getText().toString());
        MultipartEntity en = new MultipartEntity();
        en.addPart("username", usern);
        en.addPart("password", pass);


        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(en);

        System.out.println("executing request " + httppost.getRequestLine());
        DefaultHttpClient httpclient = (DefaultHttpClient) WebServiceWrapper.getNewHttpClient();
        System.out.println("==================================================================");
        HttpResponse res = httpclient.execute(httppost);
        try {

            System.out.println(res.getStatusLine());
            BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            String line = "";
            StringBuffer sb = new StringBuffer();
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            ;
            System.out.println("==================================================================");
            return sb.toString();
        } catch(Exception ex){
            ex.printStackTrace();
        }

        return null;
    }



    public class LoginRequest extends AsyncTask<List<NameValuePair>, Integer, String> {

        @Override
        protected String doInBackground(List<NameValuePair>... params) {
            // TODO Auto-generated method stub
            try {
                json = login(Config.URL_LOGIN, params[0]);
                //json = login("http://192.168.173.1:8080/uWatch-war/api/", params[0]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                // if successful start upload
                if(result != null)
                {
                    if(result.startsWith("Success:")){
                        startUpload();
                    }else{
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
