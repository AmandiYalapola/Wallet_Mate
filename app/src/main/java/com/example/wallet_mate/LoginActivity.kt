package com.example.wallet_mate

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet_mate.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding

    private  lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate((layoutInflater))
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.createAccountbtn.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            validateData()
        }
    }
    private var email = ""
    private var password = ""

    private fun validateData() {

        email = binding.lgemail.text.toString().trim()
        password = binding.lgpassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid email format...", Toast.LENGTH_SHORT).show()

        }
        else if (password.isEmpty()){
            Toast.makeText(this,"Enter Password...", Toast.LENGTH_SHORT).show()

        }
        else{
            loginUser()
        }

    }

    private fun loginUser() {

        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                checkUser()

            }
            .addOnFailureListener{ e->
                Toast.makeText(this,"Login failed due to ${e.message}...",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {

        progressDialog.setMessage("Checking user...")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{


                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))


                }
                override fun onCancelled(error: DatabaseError){
                    progressDialog.dismiss()


                }
            })
    }
}