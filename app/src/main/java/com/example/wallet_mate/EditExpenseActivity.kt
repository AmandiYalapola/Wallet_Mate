package com.example.wallet_mate

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet_mate.databinding.ActivityEditExpenseBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditExpenseBinding
    private companion object{
        private const val TAG = "Expense_EDIT_TAG"
    }
    private var id=""

    private lateinit var progressDialog: ProgressDialog

    private lateinit var categoryTitleArrayList :ArrayList<String>

    private lateinit var categoryIdArrayList:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id= intent.getStringExtra("id")!!

        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadExpenses()
        loadExpenseInfo()

        binding.exBackBtnE.setOnClickListener{
            onBackPressed()
        }

        binding.exAmountE.setOnClickListener{


        }
        binding.exTitleE.setOnClickListener{

        }
        binding.exCategoryE.setOnClickListener{
            categoryDialog()

        }
        binding.exDateE.setOnClickListener{

        }
        binding.exNoteE.setOnClickListener{

        }

        binding.exUpdateButton.setOnClickListener{

            validateData()

        }
    }
    private var amount=""
    private var title=""
    private var category=""
    private var date=""
    private var note=""


    private fun loadExpenseInfo() {
        Log.d(TAG, "loadExpenseInfo: Loading Expense info")

        val ref = FirebaseDatabase.getInstance().getReference("Expense")
        ref.child(id)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var selectedExpenseID = snapshot.child("id").value.toString()
                    val amount = snapshot.child("amount").value.toString()
                    val title = snapshot.child("title").value.toString()
                    val category = snapshot.child("category").value.toString()
                    val date = snapshot.child("date").value.toString()
                    val note = snapshot.child("note").value.toString()

                    binding.exAmountE.setText(amount)
                    binding.exTitleE.setText(title)
                    binding.exCategoryE.setText(category)
                    binding.exDateE.setText(date)
                    binding.exNoteE.setText(note)


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }



    private fun validateData() {

        amount = binding.exAmountE.text.toString().trim()
        title = binding.exTitleE.text.toString().trim()
        category = binding.exCategoryE.text.toString().trim()
        date = binding.exDateE.text.toString().trim()
        note = binding.exNoteE.text.toString().trim()

        if(amount.isEmpty()){
            Toast.makeText(this, "Enter Expense Amount", Toast.LENGTH_SHORT).show()
        }
        else if(title.isEmpty()) {
            Toast.makeText(this, "Enter Expense Title", Toast.LENGTH_SHORT).show()
        }
        else if(category.isEmpty()) {
            Toast.makeText(this, "Enter Expected Category", Toast.LENGTH_SHORT).show()
        }
        else if(date.isEmpty()) {
            Toast.makeText(this, "Enter Date", Toast.LENGTH_SHORT).show()
        }
        else if(note.isEmpty()) {
            Toast.makeText(this, "Enter Note", Toast.LENGTH_SHORT).show()
        }
        else {
            updateExpense()
        }
    }

    private fun updateExpense() {
        Log.d(TAG,"updateIExpense: Starting updating Expense")

        progressDialog.setMessage("Updating Expense info")
        progressDialog.show()

        val hashMap = HashMap<String, Any>()
        hashMap["amount"] = "$amount"
        hashMap["title"] = "$title"
        hashMap["category"] = "$category"
        hashMap["date"] = "$date"
        hashMap["note"] = "$note"

        val ref = FirebaseDatabase.getInstance().getReference("Expense")
        ref.child(id)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.d(TAG, "updateExpense: Updated Successfully")
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ExpenseDisplayActivity::class.java))
            }
            .addOnFailureListener{e->
                Log.d(TAG, "updateExpense: Failed to add due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private var selectedIncomeID = ""
    private var selectedamount = ""
    private var selectedtitle = ""
    private var selectedcategory = ""
    private var selecteddate = ""
    private var selectednote = ""



    private  var selectedCategoryId=""
    private var selectCategoryTitle=""

    private fun categoryDialog() {

        val categoryArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for(i in categoryTitleArrayList.indices){
            categoryArray[i]=categoryIdArrayList[i]

        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Category")
            .setItems(categoryArray){dialog,position ->
                selectedCategoryId = categoryIdArrayList[position]
                selectCategoryTitle=categoryTitleArrayList[position]


            }
            .show()


    }

    private fun loadExpenses() {

        Log.d(TAG,"LoadExpense: Loading Expense Detail")

        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Expense")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                categoryIdArrayList.clear()
                categoryTitleArrayList.clear()

                for(ds in snapshot.children){
                    val id = "${ds.child("id").value}"
                    val amount = "${ds.child("amount").value}"
                    val title = "${ds.child("title").value}"
                    val category = "${ds.child("category").value}"
                    val date = "${ds.child("date").value}"
                    val note = "${ds.child("note").value}"

                    categoryIdArrayList.add(id)
                    categoryTitleArrayList.add(amount)
                    categoryTitleArrayList.add(title)
                    categoryTitleArrayList.add(category)
                    categoryTitleArrayList.add(date)
                    categoryTitleArrayList.add(note)

                    Log.d(TAG,"onDataChange:Expense ID $id")
                    Log.d(TAG,"onDataChange:Expense Amount $amount")
                    Log.d(TAG,"onDataChange:Expense Title $title")
                    Log.d(TAG,"onDataChange:Expense Category $category")
                    Log.d(TAG,"onDataChange:Expense Date $date")
                    Log.d(TAG,"onDataChange:Expense Note $note")



                }
            }

            override fun onCancelled(error:DatabaseError){

            }
        })






    }
}