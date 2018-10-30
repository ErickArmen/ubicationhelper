package com.neoris.eeomartinez.ubicationhelper.features.login.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.neoris.eeomartinez.ubicationhelper.features.map.presentation.activities.ActivityMain
import java.util.*

private const val RC_SIGN_IN = 123

class ActivitySignIn: AppCompatActivity() {

    private val providers: List<AuthUI.IdpConfig> = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            RC_SIGN_IN -> {
                when (resultCode){
                    Activity.RESULT_OK -> {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null){
                            Toast.makeText(this@ActivitySignIn, "Hola ${user.displayName}", Toast.LENGTH_LONG).show()
                            goToMain()
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        finish()
                    }
                }
            }
            else -> {
                Toast.makeText(this@ActivitySignIn, IdpResponse.fromResultIntent(data)?.error?.message,
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, ActivityMain::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}