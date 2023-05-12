package com.example.wallet_mate

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet_mate.databinding.SignUpBinding
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: SignUpBinding

    private  lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.RegBackbtn.setOnClickListener{
            onBackPressed()
        }

        binding.signupBtn.setOnClickListener{
            validataData()
        }


    }
    private var name = ""
    private var email = ""
    private var password = ""


    private fun validataData() {
        name = binding.rename.text.toString().trim()
        email = binding.reemail.text.toString().trim()
        password = binding.repassword.text.toString().trim()
        val cpassword = binding.repasswordretype.text.toString().trim()

        if(name.isEmpty()){

            Toast.makeText(this,"Enter your name...",Toast.LENGTH_SHORT).show()

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email Pattern...",Toast.LENGTH_SHORT).show()

        }
        else if (password.isEmpty()){
            Toast.makeText(this,"Enter password...",Toast.LENGTH_SHORT).show()

        }
        else if (cpassword.isEmpty()){
            Toast.makeText(this,"Conform password...",Toast.LENGTH_SHORT).show()

        }
        else if (password != cpassword){
            Toast.makeText(this,"password doesn't match...",Toast.LENGTH_SHORT).show()


        }
        else{
            createUserAccount()
        }




    }

    private fun createUserAccount() {

        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed creating account due to ${e.message}",Toast.LENGTH_SHORT).show()


            }

    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Saving user info...")

        val timestamp = System.currentTimeMillis()

       val uid = firebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = name
        hashMap["name"] = email
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Account Created...",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity,DashboardActivity::class.java))
                finish()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed Saving user information due to ${e.message}...",Toast.LENGTH_SHORT).show()


            }


    }


}