package com.guillermodejuan.gcar;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class SettingsActivity extends AppCompatActivity {

Button acceptButton;
TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        acceptButton = findViewById(R.id.accept);
        text =  findViewById(R.id.address);
        acceptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDataAndGoBack();
            }
        });


    }

    private void recordDataAndGoBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", text.getEditableText());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

}








