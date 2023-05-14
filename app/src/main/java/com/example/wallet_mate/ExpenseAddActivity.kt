package com.example.wallet_mate

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet_mate.databinding.AddExpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class ExpenseAddActivity : AppCompatActivity() {

    private lateinit var binding: AddExpenseBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var selectedDate: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        selectedDate = Calendar.getInstance()

        val category= listOf("Food","Travel","Shopping","Health","Bills","Other")
        val autoComplete:AutoCompleteTextView=findViewById(R.id.exCategory)

        val adapter=ArrayAdapter(this, R.layout.dropdown_item,category)
        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener=AdapterView.OnItemClickListener{
                adapterView, view, i, l ->

            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(this, "Category", Toast.LENGTH_SHORT).show()
        }

        binding.exBackBtn.setOnClickListener{
            onBackPressed()
        }

        binding.exSaveButton.setOnClickListener{
            validateData()
        }

        binding.exDate.setOnClickListener{
            showDatePicker()
        }
    }

    private var amount =""
    private var title=""
    private var category=""
    private var date=""
    private var note=""

    private fun validateData() {
        amount = binding.exAmount.text.toString().trim()
        val amountDouble = amount.toDoubleOrNull()

        if (amountDouble == null) {
            // handle error
        } else {
            // use amountDouble
        }

        title=binding.exTitle.text.toString().trim()
        category=binding.exCategory.text.toString().trim()
        date=binding.exDate.text.toString().trim()
        note=binding.exNote.text.toString().trim()


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
            addExpenseFirebase()
        }
    }

    private fun addExpenseFirebase() {
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

        val ref = FirebaseDatabase.getInstance().getReference("Expense")
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
                binding.exDate.setText("${d}/${m+1}/${y}")
            },
            year,
            month,
            day)

        datePickerDialog.show()
    }
}