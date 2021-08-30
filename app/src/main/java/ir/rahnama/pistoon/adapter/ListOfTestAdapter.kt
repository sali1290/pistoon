package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.TestListModel


class ListOfTestAdapter(
    var context: Context,
    private var testList : MutableList<TestListModel>,
    private var listener : MoveTestID,
    private var isShowLockIc:Boolean
) :
    RecyclerView.Adapter<ListOfTestAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.list_of_test_item_model, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.testTitleTextView.text = testList[position].name
        val percent = testList[position].percent.toInt()


        when (percent) {
            in 1..50 -> {
                holder.percentTextView.setTextColor(context.resources.getColor(R.color.trikyRed))
                holder.testProgress.progressTintList = ColorStateList.valueOf(context.resources.getColor(R.color.trikyRed))
            }
            in 51..79 -> {
                holder.percentTextView.setTextColor(context.resources.getColor(R.color.golden))
                holder.testProgress.progressTintList = ColorStateList.valueOf(context.resources.getColor(R.color.golden))
            }
            in 80..100 -> {
                holder.percentTextView.setTextColor(context.resources.getColor(R.color.light_green))
                holder.testProgress.progressTintList = ColorStateList.valueOf(context.resources.getColor(R.color.light_green))
            }
        }


        val percentText= "$percent%"
        holder.percentTextView.text = percentText
        holder.testProgress.progress = testList[position].percent.toInt()



        if (isShowLockIc){
            holder.test_ic_lock.visibility=View.VISIBLE
        }




        holder.testCardView.setOnClickListener {
            listener.getID(testList[position].id.toInt())
        }
    }

    override fun getItemCount(): Int {
        return testList.size
    }

    interface MoveTestID {
        fun getID(testPosition: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var testCardView: CardView = itemView.findViewById(R.id.testRowCardView)
        var testTitleTextView: TextView = itemView.findViewById(R.id.testRowTitlesTextView)
        var percentTextView: TextView = itemView.findViewById(R.id.testRowPercentTextView)
        var testProgress: ProgressBar = itemView.findViewById(R.id.testRowProgressBar)
        var test_ic_lock: ImageView = itemView.findViewById(R.id.test_ic_lock)
    }


}
