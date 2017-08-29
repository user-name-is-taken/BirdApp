package com.example.non_kadmin.birds;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static GoogleSignInOptions gso;
    public static GoogleApiClient gac;
    SignInButton signInButton;
    static final int RC_SIGN_IN = 0;
    static final int TWEET_RC = 1;
    public static final String ACCT_NAME="Account name";
    TextView mStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusTextView = (TextView) findViewById(R.id.status);
        String sco = "https://www.googleapis.com/auth/drive";
        Scope scope = new Scope(sco);
        gso = new GoogleSignInOptions.Builder(gso.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(scope).build();
        gac = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,new FailedConnection(this))//problem?
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).addScope(scope)
                .build();
        //the first this is a fragment activity?
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);


        /*
        if(!loggedIn()){
            Intent int = new Intent(this, "login"); log it
            startActivity(int);
        }else{
            start camera/ picture activity
        }

         */
    }



    @Override
    public void onClick(View view) {
        Log.d("id",view.getId()+"   "+R.id.sign_in_button);
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gac);
        startActivityForResult(signInIntent,RC_SIGN_IN);
        //
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            new Toast(this).makeText(this,result.toString(),1).show();
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        //pass
        Log.d("sign-in","handleSignInResult:"+result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt,acct.getDisplayName()));
            launchTweetDelete(acct);
            //gac.disconnect();
        }
    }

    private void launchTweetDelete(GoogleSignInAccount acct){
        //acct.fwr
        Intent tweetDelete = new Intent(this,TweetOrDelete.class);
        tweetDelete.putExtra(ACCT_NAME, acct.getGivenName());
        //pass in the acct somehow
        //acct.getServerAuthCode();
        //acct.getId();
        startActivity(tweetDelete);
    }
}
