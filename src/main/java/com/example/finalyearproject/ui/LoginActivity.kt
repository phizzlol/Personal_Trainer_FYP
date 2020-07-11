package com.example.finalyearproject.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finalyearproject.R
import com.example.finalyearproject.utils.login
import com.example.finalyearproject.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    mAuth = FirebaseAuth.getInstance()

        button_sign_in.setOnClickListener{
            val email = edit_text_email.text.toString().trim()
            val password = edit_text_password.text.toString().trim()

            if (email.isEmpty()) {
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edit_text_email.error = "Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6) {
                edit_text_password.error = "Atleast 6 Character Password Required"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }


    //register link
        text_view_register.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        text_view_forget_password.setOnClickListener{
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        }
    }
    private fun loginUser(email: String, password: String)
    {
        progressbar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            task ->
            progressbar.visibility = View.GONE
            if(task.isSuccessful){
                //login successful
                login()
                }
            else{
                //login unsuccessful
                task.exception?.message?.let{
                    toast(it)
                }
            }
        }
    }
    override fun onStart(){
        super.onStart()
        mAuth.currentUser?.let{
            login()
        }
    }


}
