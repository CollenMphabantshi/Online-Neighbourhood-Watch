package com.example.voids.sample;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadToServer extends Activity {

    private TextView text;
    private Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaded);
        try {
            text = (TextView) findViewById(R.id.responseText);
            home = (Button) findViewById(R.id.back_home);
            home.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try{
                        Intent open = new Intent("com.example.voids.sample.HOME");
                        String identity = getIntent().getStringExtra("identity").toString();
                        open.putExtra("identity", identity);
                        startActivity(open);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            String res = getIntent().getStringExtra("response").toString();
            text.setText(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
	

}
