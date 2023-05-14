package com.example.wallet_mate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet_mate.databinding.TransactionListItemBinding
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase

class AdapterExpense:RecyclerView.Adapter<AdapterExpense.HolderCategory> {



    private  val context:Context
    private val category:ArrayList<ExpenseActivity>
    private lateinit var binding:TransactionListItemBinding
    constructor(context: Context, category: ArrayList<ExpenseActivity>) {
        this.context = context
        this.category = category
    }





    inner class HolderCategory(itemView: MaterialCardView):RecyclerView.ViewHolder(itemView){
        var tvCategory:TextView = binding.tvCategory
        var tvAmount:TextView = binding.tvAmount
        var tvtitle:TextView = binding.tvtitle

//        var tvnote:TextView = binding.tvnote

        var deleteimageButton:ImageButton=binding.deleteimageButton
        var imageButtonedit:ImageButton=binding.imageButtonedit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding= TransactionListItemBinding.inflate(LayoutInflater.from(context),parent,false )
        return  HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = category[position]
        val id =model.id
        val amount = model.amount
        val category = model.category
        val title = model.title
        val note = model.note
        val uid = model.uid
        val timestamp=model.timestamp

        holder.tvCategory.text = category
        holder.tvAmount.text = amount
        holder.tvtitle.text = title
//        holder.tvnote.text = note
        holder.imageButtonedit.setOnClickListener{
            moreOptionDialog(model,holder)

            val builder=AlertDialog.Builder(context)
            builder.setTitle("Edit")
                .setMessage("Are sure want to Edit this Expense Detail")
                .setPositiveButton("Confirm"){a, d->

                    val intent=Intent(context,EditExpenseActivity::class.java)
                    intent.putExtra("id",id)
                    context.startActivity(intent)
//


                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()
                }
                .show()

        }





        holder.deleteimageButton.setOnClickListener{
            val builder=AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are sure want to delete this Expense Detail")
                .setPositiveButton("Confirm"){a, d->
                    Toast.makeText(context,"Deleting...",Toast.LENGTH_SHORT).show()
                    deleteexpense(model, holder)

                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()
                }
                .show()

        }


    }

    private fun moreOptionDialog(model: ExpenseActivity, holder: AdapterExpense.HolderCategory) {
        val id = model.id
        val amount = model.amount
        val title = model.title
        val category = model.category
        val data = model.date
        val note = model.note

    }

    private fun deleteexpense(model: ExpenseActivity, holder: HolderCategory) {

        val id = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Expense")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted..." ,Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener{e->
                Toast.makeText(context,"Unable to delete due to ${e.message}" ,Toast.LENGTH_SHORT).show()


            }

    }
}