package com.peirr.signin.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //The views to bind to
    private TextView message;
    private Button button;
    private View progress, content;

    private boolean loggedIn; //keeps track to see if we're logged in
    private GoogleApiClient client; // the Google client instance

    private final int SIGNIN_REQUEST_CODE = 32; //request code used to fire the account selection activity


    GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.e(TAG, "Connection Failed");
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
                if (loggedIn) {
                    signOut();
                } else {
                    signIn();
                }
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();

    }


    private void signIn() {
    }

    private void signOut() {
    }

    private void showProgress(boolean show){
        content.setVisibility(show?View.INVISIBLE:View.VISIBLE);
        progress.setVisibility(show?View.VISIBLE:View.INVISIBLE);
    }

}
