package com.khangle.myfitnessapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.data.network.MyFitnessAppAuthService
import com.khangle.myfitnessapp.ui.main.ContainerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LaunchScreenActivity: AppCompatActivity() {
    @Inject lateinit var authService: MyFitnessAppAuthService
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())

    // Create and launch sign-in intent
    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setIsSmartLockEnabled(false)
        .setAvailableProviders(providers)
        .build()

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(baseContext, "Sign In ${user?.email}", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO) {
                authService.registerUser(user!!.uid, user.email ?: "")
                val intent = Intent(baseContext, ContainerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

        } else {
            if (response == null) {
                // user back
                Toast.makeText(baseContext, "user cancel", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(baseContext, "email ${user.email}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ContainerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }, 1000)

        } else {
            signInLauncher.launch(signInIntent)
        }

        setContentView(R.layout.activity_launch_screen)
    }
}