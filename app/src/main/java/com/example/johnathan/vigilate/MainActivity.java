package com.example.johnathan.vigilate;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnathan.vigilate.Broadcasts.BtnDetected;
import com.example.johnathan.vigilate.Services.ServiceLocationGPS;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private GoogleApiClient googleApiClient;
    private Switch switch1;
    private TextView mensaje;
    private TextView activar;
    private Toolbar toolbar;
    private BroadcastReceiver btnDetected;
    private IntentFilter intentFilter;
    private Button btnStopAlarm;
    private BtnDetected btnDetectedSettings;
    private SharedPreferences sharedPreferencesSettings;
    private SharedPreferences.Editor editorSettings;
    public final static String NAME_SHAREDPREFERENCE_SETTING = "settings";
    public final static String NAME_BTNDETECTED_ACTIVED = "bntDetectedActived";



    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);


        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        /*
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            nameTextView.setText(account.getDisplayName());
            Log.d("MIAPP", account.getPhotoUrl().toString());
        }else{
            goLogInScreen();
        }
        */
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializo el broadcast boot
        //sendBroadcastBoot();

        //inicializo el sharedPeferenceSettings
        sharedPreferencesSettings = getSharedPreferences(this.NAME_SHAREDPREFERENCE_SETTING, this.MODE_PRIVATE);
        //inicialiizo el editor de sharedPrefereceSettings
        SharedPreferences settings = getPreferences(this.MODE_PRIVATE);
        editorSettings= settings.edit();


        btnStopAlarm = (Button) findViewById(R.id.btnStopAlarm);
        //btnStopAlarm.setEnabled(btnDetectedSettings.getAlarmSent());
        if (btnDetectedSettings.getAlarmSent()){
            btnStopAlarm.setVisibility(View.VISIBLE);
        }else {
            btnStopAlarm.setVisibility(View.INVISIBLE);
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        switch1 =  findViewById(R.id.switch1);
        mensaje= findViewById(R.id.mensaje);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //se activa la función para dectectar secuencia de botón
                    editorSettings.putBoolean(NAME_BTNDETECTED_ACTIVED, true);
                    editorSettings.commit();
                    //registerBtnDetected();
                    //habilitar broadcast
                    //getApplication().registerReceiver(btnDetected,intentFilter);

                    mensaje.setText("En caso de emergencia presiona 5 veces la tecla de bloqueo");
                    Toast.makeText (getApplicationContext() ,"Se activo la aplicacion",Toast.LENGTH_SHORT).show();

                }else{

                    //se desactiva la función para detectar secuencia de botón
                    editorSettings.putBoolean(NAME_BTNDETECTED_ACTIVED, false);
                    editorSettings.commit();

                    //unregisterReceiver(btnDetected);

                    mensaje.setText("Estás desprotegido, ACTÍVAME");
                    Toast.makeText (getApplicationContext() ,"Se desactivo la aplicacion",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public  boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.btnStopAlarm:
                stopServiceLocationGps();
                btnStopAlarm.setVisibility(View.INVISIBLE);
                break;
            case R.id.menuLocation:
                //opciones para opcion My Location
                //location();
                break;
            case R.id.menuSettings:
                //opciones settings
                settings();
                break;
            case R.id.menuLogOut:
                //opciones singOut
                logOut();
                break;

        }

        return true;
    }


    public void logOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()) {
                    goLogInScreen();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void revoke(View view) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()) {
                    goLogInScreen();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo revocar el acceso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void location(){
        Intent intents = new Intent(this, MapsActivity.class);
        startActivity(intents);
    }

    public void settings(){
        Intent intents = new Intent(this, Settings.class);
        startActivity(intents);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void sendBroadcastBoot(){
        Intent intent = new Intent("START_SERVICE_LISTENER");
        sendBroadcast(intent);
    }

    public void registerBtnDetected(){
        btnDetected = new BtnDetected();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.registerReceiver(btnDetected, intentFilter);
    }

    public void stopServiceLocationGps(){
        Intent intentServiceLocation = new Intent(this, ServiceLocationGPS.class);
        stopService(intentServiceLocation);
    }

}
