package com.guillermodejuan.gcar;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

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
    ImageButton rotationButton;
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
    boolean autorotation;
    boolean TTSactive = false;
    String navigationaddress = "51°57'25.3 N 3°35'17.5 W";
    String playlist;
    Context context;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 1;
    private static final int MY_DATA_CHECK_CODE = 2;

    BluetoothAdapter mBluetoothAdapter;
    TelephonyManager telephonyManager;
    AudioManager audioManager;
    TextToSpeech repeatTTS;
    Resources res;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this.getApplicationContext();
        res = getResources();

        linkButtonsWithTheirViews();
        addButtonListeners();
        setUpAdapters();
        setButtonStates();
        setupTTS();
        speak(R.string.welcome_message);

    }

    private void speak(int idOfStringToSpeak){
        String stringToSpeak = res.getString(idOfStringToSpeak);
        repeatTTS.speak(stringToSpeak, TextToSpeech.QUEUE_ADD, null, stringToSpeak);
    }

    private void speak(String stringToSpeak){

        repeatTTS.speak(stringToSpeak, TextToSpeech.QUEUE_ADD, null, stringToSpeak);
    }

    private void setupTTS(){
        //prepare the TTS to repeat chosen words
        Intent checkTTSIntent = new Intent();
        //check TTS data
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        //start the checking Intent - will retrieve result in onActivityResult
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
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

    private void updateAutoRotation(){
        if (android.provider.Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
            autorotation=true;
            rotationButton.setImageDrawable(getDrawable(R.drawable.rotation_green));
        }else{
            autorotation=false;
            rotationButton.setImageDrawable(getDrawable(R.drawable.rotation_green));
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

    private void updateCallAnaButton(){
        //Button turns to green if data is enabled
        if(dataenabled){
            callanaButton.setImageDrawable(getDrawable(R.drawable.callana_green));
        }else{
            callanaButton.setImageDrawable(getDrawable(R.drawable.callana_grey));
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
        updateCallAnaButton();
        updateAutoRotation();
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
        rotationButton.setOnClickListener(buttonListener);
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
        rotationButton =   findViewById(R.id.rotation);
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

    private void launchCarDialsApp() {
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("org.prowl.torquefree");
        startActivity( LaunchIntent );
    }

    private void toggleVolumeLevels() {
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxvolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if(maxvolume-volume==0){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 2);
        }else{
            if(volume==0){
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxvolume, 2);
            }else{
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxvolume, 2);
            }
        }
        updateVolume();
    }


    public static void setAutoOrientationEnabled(Context context, boolean enabled)
    {
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_SETTINGS) {
            if(resultCode == Activity.RESULT_OK){
                navigationaddress = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            //we have the data - create a TTS instance
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                repeatTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            //TTS configuration
                            repeatTTS.setLanguage(Locale.UK);
                            TTSactive = true;


                        }
                    }
                });

                //data not installed, prompt the user to install it
            else
            {
                //intent will take user to TTS download page in Google Play
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }



private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Uri gmmIntentUri;
            Intent mapIntent;

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
                    setButtonStates();
                    break;
                case R.id.gps:
                    Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                    setButtonStates();
                    break;
                case R.id.data:
                    Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(i);
                    setButtonStates();
                    break;
                case R.id.volume:

                    toggleVolumeLevels();

                    break;
                case R.id.callana:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);

                    // the _ids you save goes here at the end of /data/12562
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/3578"),
                            "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                    intent.setPackage("com.whatsapp");

                    startActivity(intent);



                    break;
                case R.id.settings:

                    showSettingsPopup();
                    setButtonStates();
                    break;
                case R.id.rotation:
                    if (checkSelfPermission(Manifest.permission.WRITE_SETTINGS)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(Main.this,
                                new String[]{Manifest.permission.WRITE_SETTINGS},
                                MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
                    }else{
                        if (autorotation){
                            setAutoOrientationEnabled(context, false);
                        }else{
                            setAutoOrientationEnabled(context, true);
                        }
                        setButtonStates();
                    }


                    break;
                case R.id.torque:
                    speak(R.string.starting_torque);
                    launchCarDialsApp();
                    setButtonStates();
                    break;
                case R.id.maps:
                    speak(R.string.starting_torque);
                    gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(navigationaddress));
                    mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                    setButtonStates();
                    break;

                case R.id.music:
                    speak(R.string.starting_music);
                    Toast.makeText(Main.this,
                            "Functionality not yet implemented.", Toast.LENGTH_SHORT).show();
                    setButtonStates();
                    break;

                case R.id.homebutton:
                    speak(R.string.starting_home);
                    gmmIntentUri = Uri.parse("google.navigation:q=10,+Devonshire+Street,+Cheltenham,+United+Kingdom");
                    mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    setButtonStates();

                    break;

                case R.id.gbutton:
                    if(gbuttonState){
                        gButton.setImageResource(R.drawable.centrebuttonred_round);
                        gbuttonState=false;
                    }else{
                        gButton.setImageResource(R.drawable.centrebuttongreen_round);
                        gbuttonState=true;
                    }
                    setButtonStates();

                    break;
                default:
                    break;
            }
        }
    };




    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (autorotation){
                        setAutoOrientationEnabled(context, false);
                    }else{
                        setAutoOrientationEnabled(context, true);
                    }

                    setButtonStates();


                } else {
                    Toast.makeText(Main.this,
                            "Permission not granted to change screen rotation!", Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
