package com.example.vballack.facebooklogindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


public class MainActivity extends Activity implements View.OnClickListener {
    private static String TAG = "FacebookLoginDemo";

    private LoginButton mFbLoginBtn;
    private CallbackManager callbackManager;

    private void assignViews() {
        mFbLoginBtn = (LoginButton) findViewById(R.id.fbLoginBtn);
        mFbLoginBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "facebook_account_oauth_Success", Toast.LENGTH_SHORT);
                Log.e(TAG, "token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "facebook_account_oauth_Cancel", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "facebook_account_oauth_Error", Toast.LENGTH_SHORT);
                Log.e(TAG, "e: " + e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fbLoginBtn) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null || accessToken.isExpired()) {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
            }
        }
    }
}
