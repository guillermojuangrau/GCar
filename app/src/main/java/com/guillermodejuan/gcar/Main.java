package com.guillermodejuan.gcar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Main extends AppCompatActivity {

    ImageButton bluetoothButton;
    ImageButton dataButton;
    ImageButton gpsButton;
    ImageButton volumeButton;
    ImageButton settingsButton;
    ImageButton callanaButton;
    ImageButton rebootButton;
    ImageButton torqueButton;
    ImageButton musicButton;
    ImageButton mapsButton;
    ImageButton homeButton;
    ImageButton gButton;
    boolean gbuttonState = true;
    boolean btenabled;
    boolean gpsenabled;
    boolean dataenabled;
    int volume;
    int maxvolume;
    String navigationaddress = "Default address";


    BluetoothAdapter mBluetoothAdapter;
    TelephonyManager telephonyManager;
    AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        linkButtonsWithTheirViews();
        addButtonListeners();
        setUpAdapters();
        setButtonStates();

    }

    private void isGPSEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            gpsenabled = false;
        }
        else{
            gpsenabled=true;
        }
    }

    private void updateGPS(){
        isGPSEnabled();
        if(gpsenabled){
            gpsButton.setImageDrawable(getDrawable(R.drawable.gps_green));
        }else{
            gpsButton.setImageDrawable(getDrawable(R.drawable.gps_grey));
        }
    }

    private void updateBluetooth(){
        if(mBluetoothAdapter.isEnabled()){
            bluetoothButton.setImageDrawable(getDrawable(R.drawable.btgreen));
            btenabled = true;
        }else{
            bluetoothButton.setImageDrawable(getDrawable(R.drawable.bt_grey));
            btenabled=false;
        }
    }

    private void updateData(){
        if(telephonyManager.getDataState()==TelephonyManager.DATA_CONNECTED){
            dataButton.setImageDrawable(getDrawable(R.drawable.data_green));
            dataenabled = true;
        }else{
            dataButton.setImageDrawable(getDrawable(R.drawable.data_grey));
            dataenabled=false;
        }
    }

    private void updateVolume(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxvolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if(volume == 0){
            volumeButton.setImageDrawable(getDrawable(R.drawable.mute_grey));
        }else{
            if(volume==maxvolume){
                volumeButton.setImageDrawable(getDrawable(R.drawable.volume_green));
            }else{
                volumeButton.setImageDrawable(getDrawable(R.drawable.volume_grey));
            }
        }
    }
    //Method to set up all the adapters to the phone's functionality
    private void setUpAdapters(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        telephonyManager =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        return;
    }
    //Method to set the initial button states
    private void setButtonStates(){
        updateBluetooth();
        updateGPS();
        updateData();
        updateVolume();
        //TODO
        return;
    }
    private void addButtonListeners(){
        bluetoothButton.setOnClickListener(buttonListener);
        dataButton.setOnClickListener(buttonListener);
        gpsButton.setOnClickListener(buttonListener);
        volumeButton.setOnClickListener(buttonListener);
        settingsButton.setOnClickListener(buttonListener);
        callanaButton.setOnClickListener(buttonListener);
        rebootButton.setOnClickListener(buttonListener);
        torqueButton.setOnClickListener(buttonListener);
        musicButton.setOnClickListener(buttonListener);
        mapsButton.setOnClickListener(buttonListener);
        homeButton.setOnClickListener(buttonListener);
        gButton.setOnClickListener(buttonListener);

    }
    private void linkButtonsWithTheirViews(){
        bluetoothButton =   findViewById(R.id.bluetooth);
        dataButton      =   findViewById(R.id.data);
        gpsButton       =   findViewById(R.id.gps);
        volumeButton    =   findViewById(R.id.volume);
        settingsButton  =   findViewById(R.id.settings);
        callanaButton   =   findViewById(R.id.callana);
        rebootButton    =   findViewById(R.id.reboot);
        torqueButton    =   findViewById(R.id.torque);
        musicButton     =   findViewById(R.id.music);
        mapsButton      =   findViewById(R.id.maps);
        homeButton      =   findViewById(R.id.homebutton);
        gButton         =   findViewById(R.id.gbutton);
        return;
    }

    private void showSettingsPopup(){
        Intent i = new Intent(this, SettingsActivity.class);
        i.putExtra("address", navigationaddress);
        startActivityForResult(i,1);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                navigationaddress = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId() /*to get clicked view id**/) {
                case R.id.bluetooth:

                    if(btenabled){
                        btenabled = false;
                        bluetoothButton.setImageDrawable(getDrawable(R.drawable.bt_grey));
                        mBluetoothAdapter.disable();



                    }else{
                        btenabled = true;
                        bluetoothButton.setImageDrawable(getDrawable(R.drawable.btgreen));
                        mBluetoothAdapter.enable();



                    }
                    break;
                case R.id.gps:

                    break;
                case R.id.data:
                    break;
                case R.id.volume:

                    Toast.makeText(Main.this,
                "volume is clicked!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.callana:

                    Toast.makeText(Main.this,
                "callana is clicked!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings:

                    showSettingsPopup();
                    break;
                case R.id.reboot:

                    Toast.makeText(Main.this,
                            "reboot is clicked!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.torque:

                    Toast.makeText(Main.this,
                "torque is clicked!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.maps:

                     Toast.makeText(Main.this,
                "maps is clicked!", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.music:

                    Toast.makeText(Main.this,
                "music is clicked!", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.homebutton:

                     Toast.makeText(Main.this,
                "home is clicked!", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.gbutton:
                    if(gbuttonState){
                        gButton.setImageResource(R.drawable.centrebuttonred_round);
                        gbuttonState=false;
                    }else{
                        gButton.setImageResource(R.drawable.centrebuttongreen_round);
                        gbuttonState=true;
                    }


                    break;
                default:
                    break;
            }
        }
    };


}
