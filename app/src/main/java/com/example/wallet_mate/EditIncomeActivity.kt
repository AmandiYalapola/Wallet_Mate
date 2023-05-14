package com.example.wallet_mate
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.wallet_mate.databinding.ActivityEditIncomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class EditIncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditIncomeBinding
    private companion object{
        private const val TAG = "INCOME_EDIT_TAG"
    }
    private var id=""

    private lateinit var progressDialog: ProgressDialog

    private lateinit var categoryTitleArrayList :ArrayList<String>

    private lateinit var categoryIdArrayList:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id= intent.getStringExtra("id")!!

        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadIncomes()
        loadIncomeInfo()

        binding.incomebackBtnup.setOnClickListener{
            onBackPressed()
        }

        binding.incomeamountup.setOnClickListener{


        }
        binding.incometitleup.setOnClickListener{

        }
        binding.incomecategoryup.setOnClickListener{
            categoryDialog()

        }
        binding.incomedateup.setOnClickListener{

        }
        binding.incomenoteup.setOnClickListener{

        }

        binding.insaveButtonup.setOnClickListener{

            validateData()

        }
    }
    private var amount=""
    private var title=""
    private var category=""
    private var date=""
    private var note=""


    private fun loadIncomeInfo() {
        Log.d(TAG, "loadIncomeInfo: Loading Income info")

        val ref = FirebaseDatabase.getInstance().getReference("Income")
        ref.child(id)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectedIncomeID = snapshot.child("id").value.toString()
                    val amount = snapshot.child("amount").value.toString()
                    val title = snapshot.child("title").value.toString()
                    val category = snapshot.child("category").value.toString()
                    val date = snapshot.child("date").value.toString()
                    val note = snapshot.child("note").value.toString()

                    binding.incomeamountup.setText(amount)
                    binding.incometitleup.setText(title)
                    binding.incomecategoryup.setText(category)
                    binding.incomedateup.setText(date)
                    binding.incomenoteup.setText(note)

//                    Log.d(TAG,"onDataChange: Loading Income Info")
//                    val refincomes=FirebaseDatabase.getInstance().getReference("Income")
//                    refincomes.child(selectedCategoryId)
//                        .addListenerForSingleValueEvent(object :ValueEventListener{
//                            override fun onDataChange(snapshot: DataSnapshot){
//                                val amount=snapshot.child("amount").value
//
//
//                            }
//                        })


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }



    private fun validateData() {

        amount = binding.incomeamountup.text.toString().trim()
        title = binding.incometitleup.text.toString().trim()
        category = binding.incomecategoryup.text.toString().trim()
        date = binding.incomedateup.text.toString().trim()
        note = binding.incomenoteup.text.toString().trim()

        if(amount.isEmpty()){
            Toast.makeText(this, "Enter Income Amount", Toast.LENGTH_SHORT).show()
        }
        else if(title.isEmpty()) {
            Toast.makeText(this, "Enter Income Title", Toast.LENGTH_SHORT).show()
        }
        else if(category.isEmpty()) {
            Toast.makeText(this, "Enter Income Category", Toast.LENGTH_SHORT).show()
        }
        else if(date.isEmpty()) {
            Toast.makeText(this, "Enter Date", Toast.LENGTH_SHORT).show()
        }
        else if(note.isEmpty()) {
            Toast.makeText(this, "Enter Note", Toast.LENGTH_SHORT).show()
        }
        else {
            updateIncome()
        }
    }

    private fun updateIncome() {
        Log.d(TAG,"updateIncome: Sterting updating Income")

        progressDialog.setMessage("Updating Income info")
        progressDialog.show()

        val hashMap = HashMap<String, Any>()
        hashMap["amount"] = "$amount"
        hashMap["title"] = "$title"
        hashMap["category"] = "$category"
        hashMap["date"] = "$date"
        hashMap["note"] = "$note"

        val ref = FirebaseDatabase.getInstance().getReference("Income")
        ref.child(id)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.d(TAG, "updateIncome: Updated Successfully")
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, IncomeDisplayActivity::class.java))
            }
            .addOnFailureListener{e->
                Log.d(TAG, "updateIncome: Failed to add due to ${e.message}")
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

    private fun loadIncomes() {

        Log.d(TAG,"LoadIncome: Loading Income Detail")

        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Income")
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

                    Log.d(TAG,"onDataChange:Income ID $id")
                    Log.d(TAG,"onDataChange:Income Amount $amount")
                    Log.d(TAG,"onDataChange:Income Title $title")
                    Log.d(TAG,"onDataChange:Income Category $category")
                    Log.d(TAG,"onDataChange:Income Date $date")
                    Log.d(TAG,"onDataChange:Income Note $note")



                }
            }

            override fun onCancelled(error:DatabaseError){

            }
        })






    }
}