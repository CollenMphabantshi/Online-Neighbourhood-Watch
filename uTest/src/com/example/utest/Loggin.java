package com.example.utest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Loggin extends Activity{
	private EditText username,password;
	private Button loggin;
	private TextView goregister;
	private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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
	}
	
	public void onClicks() {
		loggin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validatefields()){
				/*Intent open = new Intent("com.example.utest.MAIN");
				
				startActivity(open);*/
				}else{
					Toast.makeText(getApplicationContext(), "Sorry, all fields must be field.", Toast.LENGTH_LONG);
				}
				
			}
		});
		
		goregister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent open = new Intent("com.example.utest.REGISTER");
				
				startActivity(open);
				
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
	
	public void getPostData(){
		
		
	}
	

}
