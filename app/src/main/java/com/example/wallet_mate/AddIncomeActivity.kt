package com.example.wallet_mate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup

class AddIncomeActivity : AppCompatActivity() {

    private lateinit var incometitle: EditText
    private lateinit var incomecategory: AutoCompleteTextView
    private lateinit var incomeamount: EditText
    private lateinit var etDate: EditText
    private lateinit var incomedate: Button
    private lateinit var incomenote: EditText
    private lateinit var toolbarLinear: LinearLayout
    private var type: Int = 1 //expense is the default value
    private var amount: Double = 0.0
    private var date: Long = 0
    private var invertedDate: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)
    }
}