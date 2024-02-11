package com.sayan.chatnow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sayan.chatnow.UserModel
import com.sayan.chatnow.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var dbRef:DatabaseReference

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the status bar
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.signupText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        auth = Firebase.auth


        binding.btnSignup.setOnClickListener(View.OnClickListener {
            val emailSignup:String = binding.emailEtSignup.text.toString()
            val nameSignup:String = binding.nameEtSignup.text.toString()
            val pwdSignup:String = binding.pwdEtSignup.text.toString()
            if(!(emailSignup.trim() == "" && pwdSignup.trim() == "" && nameSignup.trim()=="")){
                signUpUser(nameSignup, emailSignup, pwdSignup)
            }
        })


    }

    private fun signUpUser(nameSignup:String, emailSignup: String, pwdSignup: String) {
        auth.createUserWithEmailAndPassword(emailSignup, pwdSignup)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Success!!", Toast.LENGTH_SHORT).show()
                    adduserToDataBase(nameSignup, emailSignup, auth.currentUser?.uid!!)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Failed!!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun adduserToDataBase(nameSignup: String, emailSignup: String, uid: String) {
        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef.child("users").child(uid).setValue(UserModel(nameSignup, emailSignup, uid))
    }
}