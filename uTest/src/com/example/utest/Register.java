package com.example.utest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Register extends Activity{
	private EditText name,surname,identity,email,passwod,cpassword;
	private Button register;
	private Pattern pattern;
    private Matcher matcher;
    private Spinner province;
    private String typeOfCase;
    private TextView testR;
    private JSONObject json;
    private String URL_UPLOAD = "http://192.168.43.15/10.50.64.78/display.php";

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    MessageDigest messageDigest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		try {
			messageDigest  = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			initialise();
			onClicks();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initialise(){
		name = (EditText) findViewById(R.id.name);
		surname =  (EditText) findViewById(R.id.surname);
		identity =  (EditText) findViewById(R.id.identity);
		email =  (EditText) findViewById(R.id.email);
		passwod =  (EditText) findViewById(R.id.password);
		cpassword =  (EditText) findViewById(R.id.confirmpassword);
		province =  (Spinner) findViewById(R.id.province);
		register = (Button) findViewById(R.id.register);
		pattern = Pattern.compile(EMAIL_PATTERN);
		testR = (TextView) findViewById(R.id.testR);
	}
	
	public void onClicks(){
		
		province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
					typeOfCase = province.getSelectedItem().toString();
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startLogin();
				/*if(validatefields()){
					
					Toast.makeText(getApplicationContext(), "Thanks all values are filled", Toast.LENGTH_LONG);
					
					if(comparePass()){
						Toast.makeText(getApplicationContext(), "Thanks password successfully confirmed", Toast.LENGTH_LONG);
						try {
							testR.setText(getPostData());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					
					
				}*/
				
			}
		});
	}
	
public boolean validatefields(){
		
		
		
		if(name.getText().toString().trim().length() == 0){
			name.requestFocus();
			name.setError("sorry, empty field");
			return false;
		}
		
		if(surname.getText().toString().trim().length() == 0){
			surname.requestFocus();
			surname.setError("sorry, empty field");
			return false;
		}
		
		if(identity.getText().toString().trim().length() == 0){
			identity.requestFocus();
			identity.setError("sorry, empty field");
			return false;
		}
		
		if(email.getText().toString().trim().length() == 0 || !validateEmail(email.getText().toString().trim()) ){
			email.requestFocus();
			email.setError("sorry, invalid email");
			return false;
		}
		
		
		if(passwod.getText().toString().trim().length() == 0){
			passwod.requestFocus();
			passwod.setError("sorry, empty field");
			return false;
		}
		
		if(cpassword.getText().toString().trim().length() == 0){
			cpassword.requestFocus();
			cpassword.setError("sorry, empty field");
			return false;
		}
		
		return true;
	}
	
	public boolean validateEmail(String str){
		matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public boolean comparePass(){
		if(!cpassword.getText().toString().trim().endsWith(passwod.getText().toString().trim())){
			cpassword.requestFocus();
			cpassword.setError("passwords are not the same");
			cpassword.setText("");
			passwod.setText("");
			return true;
		}else
			return true;
		
	}
	
public String getPostData() throws JSONException{
	JSONObject jsonObj = new JSONObject();
	JSONArray jsonArr = new JSONArray();
	messageDigest.update(name.getText().toString().trim().getBytes());
	String nm = new String(messageDigest.digest());
	messageDigest.update(surname.getText().toString().trim().getBytes());
	String sn = new String(messageDigest.digest());
	messageDigest.update(identity.getText().toString().trim().getBytes());
	String id = new String(messageDigest.digest());
	messageDigest.update(email.getText().toString().trim().getBytes());
	String em = new String(messageDigest.digest());
	messageDigest.update(passwod.getText().toString().trim().getBytes());
	String ps = new String(messageDigest.digest());
	messageDigest.update(typeOfCase.getBytes());
	String pr = new String(messageDigest.digest());
	
	jsonObj.accumulate("name",nm);
	jsonObj.accumulate("surname", sn);
	jsonObj.accumulate("identity", id);
	jsonObj.accumulate("email", em);
	jsonObj.accumulate("password", ps);
	jsonObj.accumulate("province", pr);
	
	
	return jsonObj.toString();
		
	}

public void startLogin(){
	
	String nam = name.getText().toString().trim();
	String surnam = surname.getText().toString().trim();
	String identit = identity.getText().toString().trim();
	String emai = email.getText().toString().trim();
	String pass = passwod.getText().toString().trim();
	String provinc = typeOfCase;
	
	
    try {
    	List<NameValuePair> pairs = new ArrayList<NameValuePair>();  
    	
        pairs.add(new BasicNameValuePair("name",nam));
		pairs.add(new BasicNameValuePair("surname",surnam));
		pairs.add(new BasicNameValuePair("identity",identit));
		pairs.add(new BasicNameValuePair("email",emai));
		pairs.add(new BasicNameValuePair("password",pass));
		pairs.add(new BasicNameValuePair("province",provinc));
	    new Read().execute(pairs);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
   
}

	private void startMenu() throws JSONException{
		if(json != null)
		{
			String status = json.getString("status");
			String message = json.getString("msg");
			
			
			if(status.toLowerCase().equals("pass"))
			{
				Intent open = new Intent("com.example.utest.LOGGIN");
				try{
					open.putExtra("Identity", identity.getText().toString());
				}catch(Exception e){e.printStackTrace();}
				
				startActivity(open);
			}else{
				
				Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
				
			}
		}else{
			
			//Toast.makeText(getApplicationContext(),"Server connection problem detected. Please contact admin.", Toast.LENGTH_LONG).show();
		}
		
	}

public JSONObject request(String url,  List<NameValuePair> pairs)
        throws ClientProtocolException, IOException, IllegalStateException,
        JSONException {
	
    	DefaultHttpClient client = (DefaultHttpClient) WebServiceWrapper.getNewHttpClient();
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,HTTP.UTF_8);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuffer sb = new StringBuffer();
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
        	sb.append(line + NL);
        }
        in.close();
        
        System.out.println("OUTPUT: >>>>> "+sb.toString());
        JSONObject obj = new JSONObject(sb.toString());
        return obj;
}



public class Read extends AsyncTask<List<NameValuePair>, Integer, JSONObject>{

	@Override
	protected JSONObject doInBackground(List<NameValuePair>... params) {
		// TODO Auto-generated method stub
		try {
			json = request(URL_UPLOAD, params[0]);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			// if successful go to loggin
			startMenu();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

}
