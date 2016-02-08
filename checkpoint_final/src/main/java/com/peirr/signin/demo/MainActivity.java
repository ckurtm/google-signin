package com.peirr.signin.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //The views to bind to
    private TextView message;
    private Button button;
    private View progress,content;

    private boolean loggedIn; //keeps track to see if we're logged in
    private GoogleApiClient client; // the Google client instance

    private final int SIGNIN_REQUEST_CODE = 32; //request code used to fire the account selection activity


    GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
          Log.e(TAG,"Connection Failed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        message = (TextView) findViewById(R.id.textView);
        progress = findViewById(R.id.progressBar);
        content = findViewById(R.id.content);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedIn){
                    signOut();
                }else{
                    signIn();
                }
            }
        });


        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();


    }

    private void signIn() {
        showProgress(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(signInIntent, SIGNIN_REQUEST_CODE);
    }

    private void signOut() {
        showProgress(true);
        Auth.GoogleSignInApi.signOut(client).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        showProgress(false);
                        updateButton(!status.isSuccess());
                    }
                });
    }


    private void showProgress(boolean show){
        content.setVisibility(show?View.INVISIBLE:View.VISIBLE);
        progress.setVisibility(show?View.VISIBLE:View.INVISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNIN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result) {
        showProgress(false);
        message.setText("");
        if (result.isSuccess()) { //signed in
            GoogleSignInAccount acct = result.getSignInAccount();
            message.setText(acct.getDisplayName() + "\n" + acct.getEmail());
            updateButton(true);
        } else { //signed out
           updateButton(false);
        }
    }

    void updateButton(boolean loggedIn){
        this.loggedIn = loggedIn;
        if(loggedIn) {
            button.setText(getString(R.string.app_signout));
        }else{
            message.setText("");
            button.setText(getString(R.string.app_signin));
        }
    }
}
