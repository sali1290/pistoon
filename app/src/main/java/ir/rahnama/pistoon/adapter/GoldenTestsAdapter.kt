package ir.rahnama.pistoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.GoldenTestModel


class GoldenTestsAdapter(var context: Context, private var goldentest : List<GoldenTestModel>) : RecyclerView.Adapter<GoldenTestsAdapter.MyViewHolder>() {




    private val counter=(1 .. 49).toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.golden_test_item_model, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.titleTextView.text = goldentest[position].title
        holder.answerTextView.text = goldentest[position].answer
        holder.txt_counter.text = counter[position].toString()

    }

    override fun getItemCount(): Int {
        return goldentest.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.goldenTestTitle)
        var answerTextView: TextView =  itemView.findViewById(R.id.godlenTestDes)
        var txt_counter: TextView =  itemView.findViewById(R.id.txt_counter)

    }
}
