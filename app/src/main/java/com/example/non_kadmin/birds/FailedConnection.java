package com.example.non_kadmin.birds;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by non-kadmin on 7/30/2017.
 */

public class FailedConnection implements GoogleApiClient.OnConnectionFailedListener {
    Activity act;
    int RESOLVE_CONNECTION_REQUEST_CODE=0;
    public FailedConnection(Activity activity){
        super();
        act = activity;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //get error codes and handle them
        new Toast(act).makeText(act,connectionResult.toString(),1).show();
        //https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(act, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }else{
            GooglePlayServicesUtil
                    .getErrorDialog(connectionResult.getErrorCode(),act,0).show();
        }
    }


}
