package com.example.wallet_mate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallet_mate.databinding.ExpenseDetailsListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExpenseDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ExpenseDetailsListBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var category: ArrayList<ExpenseActivity>

    private lateinit var adapterExpense: AdapterExpense

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpenseDetailsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        category = ArrayList()
        adapterExpense = AdapterExpense(this, category)
        binding.expenserv.adapter = adapterExpense
        binding.expenserv.layoutManager = LinearLayoutManager(this)

        binding.addNewExDL.setOnClickListener {
            startActivity(Intent(this, ExpenseAddActivity::class.java))
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Expense")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                category.clear()
                for (expenseSnapshot in dataSnapshot.children) {
                    val model = expenseSnapshot.getValue(ExpenseActivity::class.java)
                    model?.let { category.add(it) }
                }
                adapterExpense.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle databaseError
            }
        })
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // Handle user not logged in
        }
    }
}

