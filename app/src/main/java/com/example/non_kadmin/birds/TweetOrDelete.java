package com.example.non_kadmin.birds;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by non-kadmin on 7/18/2017.
 * streaming video from Google Drive:
 *  https://stackoverflow.com/questions/29596614/android-stream-video-from-google-drive
 *
 *  saving an image to storage:
 *      https://stackoverflow.com/questions/19462213/android-save-images-to-internal-storage
 *      would also need to save the image's location in the drive
 *
 *This Activity works, but you'll probably have to play around with the
 * life cycle a bit to get the connection right.
 */

public class TweetOrDelete extends AppCompatActivity
{
    private static final int ID_INTENT = 11;
    private static DriveId driveId;
    //DriveFolder folder;
    Drawable bird;
    GoogleApiClient TDgac;

    //public TweetOrDelete(GoogleSignInAccount acct){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        //img = (ImageView) findViewById(R.id.birdPic);
        if(savedInstanceState == null){
//            connectToAPI(getIntent().getExtras().getString(MainActivity.ACCT_NAME));
        }



        //displayImage();
    }

    /*
    https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.Builder
    https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInAccount
    setting name doesn't work?

    https://stackoverflow.com/questions/21610239/how-do-i-switch-accounts-under-the-new-google-drive-android-api
        another way of getting the account

     https://stackoverflow.com/questions/40719389/perform-google-sheets-api-access-with-android-googlesigninapi-credentials
        sheets stuff

     https://developers.google.com/picker/docs/
        picker? another option?

     https://stackoverflow.com/questions/27108178/google-drive-api-android-how-to-obtain-a-drive-file-id?rq=1
        normal searching

     https://www.101apps.co.za/index.php/articles/android-apps-and-google-drive-a-tutorial.html
        this is somewhere else also
     */

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //swipe detection https://developer.android.com/training/gestures/detector.html
    private void displayImage(){
        //exists = imageInDrive?
        if(getAppDataFiles().size()>0) {
            //display the drive image
            //Image =
            //display 0
        }else{
            //display the default src
            //img.setImageResource(R.mipmap.ic_launcher);
        }
    }



    /*gets the ID of the folder the tweetable pictures are in
    from your google drive using an intent
    https://github.com/googledrive/android-demos/blob/master/app/src/main/java/com/google/android/gms/drive/sample/demo/PickFileWithOpenerActivity.java
     https://developers.google.com/android/reference/com/google/android/gms/drive/OpenFileActivityBuilder
     https://www.lynda.com/Google-Play-Services-tutorials/Select-using-OpenFileActivityBuilder/474086/503700-4.html
     https://www.101apps.co.za/index.php/articles/android-apps-and-google-drive-picking-files.html
     */
    private void getBirdsFileID(){
        //if(MainActivity.gac == null) { not the problem
        String[] mime = {DriveFolder.MIME_TYPE,};

        OpenFileActivityBuilder builder = Drive.DriveApi.newOpenFileActivityBuilder()
        .setMimeType(mime);
        if (builder == null)Log.e("hello", "error here");
        IntentSender intentSender = builder
                .build(TDgac);//
        //.setMimeType(new String[] { "text/plain", "text/html" }) // <- TEXT FILES
        try{
            startIntentSenderForResult(intentSender,
                    ID_INTENT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e){
            Log.w("intent", "unable to send intent", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        switch ((requestCode)){
            case ID_INTENT:
                if(resultCode == RESULT_OK){
                    //if
                    driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                }
                finish();
                break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //tweets the image at imgLoc with the caption tweetText
    public void tweet (String imgLoc, String tweetText) {
        // https://stackoverflow.com/questions/19120036/add-image-to-twitter-share-intent-android
        try{
            Intent tweetIntent = new Intent(Intent.ACTION_SEND);

            File imageFile = new File(Environment.getExternalStorageDirectory(),imgLoc);

            tweetIntent.putExtra(Intent.EXTRA_TEXT, tweetText);
            tweetIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
            tweetIntent.setType("image/jpeg");
            PackageManager pm = this.getPackageManager();
            List<ResolveInfo> lract = pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
            boolean resolved = false;
            for (ResolveInfo ri : lract){
                if (ri.activityInfo.name.contains("twitter")){
                    tweetIntent.setClassName(ri.activityInfo.packageName,
                            ri.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            startActivity(resolved ? tweetIntent:
            Intent.createChooser(tweetIntent, "Choose one"));
        }catch(final ActivityNotFoundException e){
            new Toast(this).makeText(this,"twitter not installed",0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //maybe just put the image location in the bundle and save the image
        //in storage?
    }

    /*gets the string file extensions to link to*/
    public ArrayList<String> getAppDataFiles() {
        ArrayList<String> appDataFiles=null;
        return appDataFiles;
    }

}
//precore