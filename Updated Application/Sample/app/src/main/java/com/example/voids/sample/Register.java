package com.example.voids.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import org.apache.http.message.BasicNameValuePair;
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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {
	private EditText name,surname,identity,email,passwod,cpassword;
	private Button register;
	private Pattern pattern;
    private Matcher matcher;
    private Spinner province;
    private String typeOfCase;
    private TextView testR;
    private JSONObject json;
    private String response = "";
    private String filePath = "", caseType ="", caseLocation;
    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    MessageDigest messageDigest;
	private Pattern passPattern;
	private Matcher passMatcher;
	private static final String PASSWORD_PATTERN =
			"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$&%*+-]).{8,20})";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		//
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
		passPattern = Pattern.compile(PASSWORD_PATTERN);
		testR = (TextView) findViewById(R.id.testR);


        try {
            filePath = getIntent().getStringExtra("filePath");
            caseType = getIntent().getStringExtra("caseType");
            caseLocation = getIntent().getStringExtra("caseLocation");
        }catch(Exception e){
            e.printStackTrace();
        }
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
				//startLogin();
				if(validatefields()){
					
					Toast.makeText(getApplicationContext(), "Thanks all values are filled", Toast.LENGTH_LONG);
					
					if(comparePass()){
						Toast.makeText(getApplicationContext(), "Thanks password successfully confirmed", Toast.LENGTH_LONG);
						try {
							//testR.setText();
							getPostData();
                            // send data to server
                            startRegistration();



						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					
					
				}
				
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
		
		
		if(passwod.getText().toString().trim().length() == 0 || !validate(passwod.getText().toString().trim())) {
			passwod.requestFocus();
			passwod.setError("sorry, UpperCase, LowerCase, Numbers and Special Characters in a String. It must have 8 characters minimum");
			return false;
		}


		
		if(cpassword.getText().toString().trim().length() == 0){
			cpassword.requestFocus();
			cpassword.setError("sorry, empty field");
			cpassword.setText("");
			return false;
		}

	if(!passwod.getText().toString().trim().equals(cpassword.getText().toString().trim()))
	{
		cpassword.requestFocus();
		cpassword.setError("sorry, password not the same");
		return false;

	}

		
		return true;
	}

	public boolean validate(String password1){

		passMatcher = passPattern.matcher(password1);
		return passMatcher.matches();

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


public String getPostData() throws JSONException {
	JSONObject jsonObj = new JSONObject();
	JSONArray jsonArr = new JSONArray();

    String nm = new String(name.getText().toString());
    String sn = new String(surname.getText().toString());
    String id = new String(identity.getText().toString());
    String em = new String(email.getText().toString());
    String ps = new String(passwod.getText().toString());
    String pr = new String(typeOfCase);
	jsonObj.accumulate("name",nm);
	jsonObj.accumulate("surname", sn);
	jsonObj.accumulate("identity", id);
	jsonObj.accumulate("email", em);
	jsonObj.accumulate("password", ps);
	jsonObj.accumulate("province", pr);
	
	
	return jsonObj.toString();
		
}

public void startRegistration(){
	
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
	    new RegisterRequest().execute(pairs);

		startMenu();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
   
}

	private void startMenu() throws JSONException {
		//if(json != null)
		//{
			String status = json.getString("status");
			String message = json.getString("msg");
			
			
			if(status.toLowerCase().equals("success"))
			{
                testR.setText(message);
				Intent open = new Intent("com.example.voids.sample.LOGGIN");
				try{
                    open.putExtra("filePath", filePath);
                    open.putExtra("caseType", caseType);
                    open.putExtra("caseLocation", caseLocation);
				}catch(Exception e){e.printStackTrace();}
				
				startActivity(open);
			}else{
				
				Toast.makeText(getApplicationContext(), status+": "+message, Toast.LENGTH_LONG).show();
				
			}
		//}else{
			
			//Toast.makeText(getApplicationContext(),"Server connection problem detected. Please contact admin.", Toast.LENGTH_LONG).show();
		//}
		
	}

	public void processResponse(){

		try{
			Intent open = new Intent("com.example.voids.sample.LOGGIN");
			open.putExtra("response",response);
			//open.putExtra("identity",username.getText().toString());
			startActivity(open);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

public JSONObject register(String url,  List<NameValuePair> pairs)
        throws ClientProtocolException, IOException, IllegalStateException,
        JSONException {
	
    	DefaultHttpClient client = (DefaultHttpClient) WebServiceWrapper.getNewHttpClient();
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
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



public class RegisterRequest extends AsyncTask<List<NameValuePair>, Integer, JSONObject> {

	@Override
	protected JSONObject doInBackground(List<NameValuePair>... params) {
		// TODO Auto-generated method stub
		try {
			json = register(Config.URL_REGISTER, params[0]);
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

			processResponse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

}
