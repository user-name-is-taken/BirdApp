package com.example.non_kadmin.birds;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

/*
Activities in this app:
    1) Login/connect to pi via FTP
        a. make this able to use different Pis
        b. login to twitter also
    2) Tweet/not tweet (using FTP and twitter API)
    3) Take pic live stream/web cam
        a. main activity

https://developers.google.com/identity/sign-in/android/
 https://developers.google.com/identity/sign-in/android/sign-in?configured=true
 follow the above directions for sign in w/google


 */
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    static final int RC_SIGN_IN = 0;
    static final int TWEET_RC = 1;
    GoogleApiClient TDgac;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        switch ((requestCode)){
           /*
            case ID_INTENT:
                if(resultCode == RESULT_OK){
                    //if
                    driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                }
                finish();
                break;
                */
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //maybe just put the image location in the bundle and save the image
        //in storage?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TDgac = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                //.setAccountName(acct_name)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        TDgac.connect();
    }



    /*
    @Override
    public void onClick(View view) {
        Log.d("id",view.getId()+"   "+R.id.sign_in_button);
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            /*Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
            */
            new Toast(this).makeText(this, "Menu",2).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.birdui, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }


//could make this connector into its own class
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        new Toast(this).makeText(this, "failed connection",1).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        new Toast(this).makeText(this, "connected",1).show();
        //getBirdsFileID();
    }

    @Override
    public void onConnectionSuspended(int i) {
        new Toast(this).makeText(this, "suspended connection",1).show();
    }

}
