package com.thomasphillips3.landmarkapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private val RC_SIGN_IN = 9001
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SignInActivity", "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initView()
    }

    private fun initView() {
        Log.d("SignInActivity", "initView()")
        findViewById<View>(R.id.button_signIn).setOnClickListener(this)

        //Configure Sign In Client
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        //initialize client to connect
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent: Intent? = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    override fun onStart() {
        super.onStart()
        updateUi(GoogleSignIn.getLastSignedInAccount(this))
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            updateUi(completedTask.getResult(ApiException::class.java))
        } catch (e: ApiException) {
            error(this.localClassName, "signInResult: failed. code=${e.statusCode}")
            updateUi(null)
        }
    }

    private fun updateUi(account: GoogleSignInAccount?) {
        if (account != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun error(name: String, message: String) {
        Log.w(name, message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        signIn()
    }
}
