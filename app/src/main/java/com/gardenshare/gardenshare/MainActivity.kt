package com.gardenshare.gardenshare

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Amplify.Auth.fetchAuthSession(
            {
                val session = it as AWSCognitoAuthSession
                when (session.identityId.type) {
                    AuthSessionResult.Type.SUCCESS -> {
                        val accessToken: String? = session.userPoolTokens.value?.accessToken
                        if(accessToken.isNullOrEmpty()) {
                            Log.i("AuthQuickStart", "Access token is null or empty")
                        } else {
                            val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("accessKey", accessToken)
                            val intentToMainMenu = Intent(this, MainScreen::class.java)
                            startActivity(intentToMainMenu)
                        }
                    }
                    AuthSessionResult.Type.FAILURE -> {
                        Log.w("AuthQuickStart", "IdentityId not found", session.identityId.error)
                        val intentToLogin = Intent(this, AmplifyMainApp::class.java)
                        startActivity(intentToLogin)
                    }
                }
            },
            { Log.e("AuthQuickStart", "Failed to fetch session", it) }
        )
        setContentView(R.layout.activity_main)
    }
}