package com.example.wallet_mate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.wallet_mate.databinding.LoginBinding
import com.google.firebase.database.core.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var binding:LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.loginBtn.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))


        }
        binding.googleSignInBtn.setOnClickListener{

        }

//        fun makeCurrentFragment(fragment: Fragment) { //method 2
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fl_wrapper, fragment)
//                commit()
//            }
        }



    }

