package com.example.wallet_mate

import android.app.ProgressDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.wallet_mate.databinding.AddIncomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class IncomeAddActivity : AppCompatActivity() {

    private lateinit var binding: AddIncomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var selectedDate: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AddIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        selectedDate = Calendar.getInstance()

        val category= listOf("Salary","Award","Gift","Investment Return","Rent","Refunds","Sales","Coupons","Other Income")
        val autoComplete:AutoCompleteTextView=findViewById(R.id.incomecategory)

        val adapter=ArrayAdapter(this, R.layout.dropdown_item,category)
        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener=AdapterView.OnItemClickListener{
            adapterView, view, i, l ->  
            
            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(this, "Category", Toast.LENGTH_SHORT).show()
        }

        binding.incomebackBtn.setOnClickListener{
            onBackPressed()
        }

        binding.insaveButton.setOnClickListener{
            validateData()
        }

        binding.incomedate.setOnClickListener{
            showDatePicker()
        }
    }

    private var amount =""
    private var title=""
    private var category=""
    private var date=""
    private var note=""

    private fun validateData() {
        amount = binding.incomeamount.text.toString().trim()
//        val amountDouble = amount.toDoubleOrNull()
//
//        if (amountDouble == null) {
//            // handle error
//        } else {
//            // use amountDouble
//        }

        title=binding.incometitle.text.toString().trim()
        category=binding.incomecategory.text.toString().trim()
        date=binding.incomedate.text.toString().trim()
        note=binding.incomenote.text.toString().trim()


        if (amount.isEmpty()) {
            Toast.makeText(this, "Enter Amount...", Toast.LENGTH_SHORT).show()
        } else if(title.isEmpty()){
            Toast.makeText(this,"Enter Title...",Toast.LENGTH_SHORT).show()
        }
        else if(category.isEmpty()){
            Toast.makeText(this,"Enter Category...",Toast.LENGTH_SHORT).show()
            }
        else if(date.isEmpty()){
            Toast.makeText(this,"Enter Date...",Toast.LENGTH_SHORT).show()
        }

        else if(note.isEmpty()){
            Toast.makeText(this,"Enter Note...",Toast.LENGTH_SHORT).show()


        } else {
            addIncomeFirebase()
        }
    }

    private fun addIncomeFirebase() {
        progressDialog.show()
        val timestamp = System.currentTimeMillis()

        val hashMap=HashMap<String, Any>()
        hashMap["id"]="$timestamp"
        hashMap["amount"]= amount
        hashMap["title"]= title
        hashMap["category"]= category
        hashMap["date"]= date
        hashMap["note"]= note
        hashMap["timestamp"]= timestamp
        hashMap["uid"]="${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("Income")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Added Successfully...",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Faild to add due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDatePicker() {
        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, y, m, d ->
                selectedDate.set(y, m, d)
                binding.incomedate.setText("${d}/${m+1}/${y}")
            },
            year,
            month,
            day)

        datePickerDialog.show()
    }
}
