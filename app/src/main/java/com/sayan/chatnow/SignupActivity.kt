package com.sayan.chatnow.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.sayan.chatnow.UpdateProfilePicture
import com.sayan.chatnow.UserModel
import com.sayan.chatnow.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var dbRef:DatabaseReference

    private lateinit var auth:FirebaseAuth
    val uriString: String = "https://firebasestorage.googleapis.com/v0/b/chatnow-e5671.appspot.com/o/user.png?alt=media&token=73fd80a7-cced-44dc-a285-703c7c02a7d3"

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
                binding.progressSignup.visibility=ProgressBar.VISIBLE
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Success!!", Toast.LENGTH_SHORT).show()
                    adduserToDataBase(nameSignup, emailSignup, auth.currentUser?.uid!!, uriString)
                    val intent = Intent(this, UpdateProfilePicture::class.java)
                    binding.progressSignup.visibility=ProgressBar.GONE
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.result.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun adduserToDataBase(nameSignup: String, emailSignup: String, uid: String, uri:String) {
        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef.child("users").child(uid).setValue(UserModel(nameSignup, emailSignup, uid,uri))
    }
}