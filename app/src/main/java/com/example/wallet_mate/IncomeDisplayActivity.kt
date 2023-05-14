package com.example.wallet_mate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallet_mate.databinding.IncomeDetailsListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IncomeDisplayActivity : AppCompatActivity() {

    private lateinit var binding: IncomeDetailsListBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var category: ArrayList<IncomeActivity>

    private lateinit var adapterIncome: AdapterIncome

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IncomeDetailsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        category = ArrayList()
        adapterIncome = AdapterIncome(this, category)
        binding.incomerv.adapter = adapterIncome
        binding.incomerv.layoutManager = LinearLayoutManager(this)

        binding.floatingActionButton3.setOnClickListener {
            startActivity(Intent(this, IncomeAddActivity::class.java))
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Income")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                category.clear()
                var totalIncome = 0.00
                for (incomeSnapshot in dataSnapshot.children) {
                    val model = incomeSnapshot.getValue(IncomeActivity::class.java)
                    model?.let { category.add(it)
                        totalIncome += it.amount.toDouble()
                    }
                }
                binding.DescTextView.text = "Total Income(LKR): "
                binding.toalin.text="$totalIncome"

                adapterIncome.notifyDataSetChanged()
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

private operator fun Double.plusAssign(amount: String) {

}

