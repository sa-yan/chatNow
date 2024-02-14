package com.sayan.chatnow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sayan.chatnow.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityLoginBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the status bar
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding =ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.loginText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        })

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val emailLogin:String = binding.emailEtLogin.text.toString()
            val pwdLogin:String = binding.pwdEtLogin.text.toString()
            if(!(emailLogin.trim() == "" && pwdLogin.trim() == "")){
                loginUser(emailLogin, pwdLogin)
            }
        })

    }

    private fun loginUser(emailLogin: String, pwdLogin: String) {
        binding.progressLogin.visibility=ProgressBar.VISIBLE
        auth.signInWithEmailAndPassword(emailLogin, pwdLogin)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    binding.progressLogin.visibility=ProgressBar.GONE
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Failed!!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}