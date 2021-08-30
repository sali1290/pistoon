package ir.rahnama.pistoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.AllTestModel

class TrickyTestAdapter(

        var context: Context,
        private var trickyList: List<AllTestModel>,
        private var longListener: RecyclerViewActionListener
)
    :RecyclerView.Adapter<TrickyTestAdapter.MyViewHolder>() {

    private var isSelectedAll = false
    private var itemSelected :MutableList<Int>? = arrayListOf()
    private var clickable : Boolean = false
    private var itemposition :ArrayList<Int> = arrayListOf()
    private var itemSelectedCount = 0



    fun selectAll() {
        isSelectedAll = true
        for(i in itemposition.indices) itemposition[i] = 1
        itemSelectedCount=trickyList.size
        longListener.OnLongClick(itemSelected!!,itemSelectedCount)
        notifyDataSetChanged()
    }

    fun unselectall() {
        isSelectedAll = false
        itemSelectedCount=0
        itemSelected!!.clear()
        for(i in itemposition.indices) itemposition[i] = 0
        longListener.OnLongClick(itemSelected!!,itemSelectedCount)
        notifyDataSetChanged()
    }

    fun clearPrevData(){
        itemSelected!!.clear()
        for(i in itemposition.indices) itemposition[i] = 0
        clickable=false
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrickyTestAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        for(i in trickyList.indices) itemposition.add(0)
        return MyViewHolder(inflater.inflate(R.layout.trickey_item_model, parent, false))
    }

    override fun onBindViewHolder(holder: TrickyTestAdapter.MyViewHolder, position: Int) {



         if (itemposition[position]==1){
            holder.linear_tricky.background = ContextCompat.getDrawable(context, R.color.trikyTransparentRed)
        }else{
            holder.linear_tricky.background = ContextCompat.getDrawable(context, R.color.white_deep)
        }


        holder.title_txt.text=trickyList[position].title
        val answers= arrayOf(trickyList[position].answer1, trickyList[position].answer2, trickyList[position].answer3, trickyList[position].answer4)
        val id  =trickyList[position].true_answer.toInt()
        holder.answer_txt.text= answers[id]






        if(trickyList[position].image==null){
            holder.image.visibility=View.GONE
        }else{
            holder.image.visibility=View.VISIBLE
            holder.image.setImageBitmap(trickyList[position].image)
        }



        holder.linear_tricky.setOnLongClickListener {
            itemposition[position]=1
            itemSelectedCount=1
            itemSelected!!.add(trickyList[position].id.toInt())
            notifyDataSetChanged()
            longListener.OnLongClick(itemSelected!!,itemSelectedCount)
            clickable=true
            true
        }


        holder.linear_tricky.setOnClickListener{
           if (clickable){
                if ( itemposition[position]==1){
                itemposition[position]=0
                itemSelected!!.remove(trickyList[position].id.toInt())
                    itemSelectedCount--
                    notifyDataSetChanged()
            }else if(itemposition[position]==0){
                itemposition[position]=1
                itemSelected!!.add(trickyList[position].id.toInt())
                    itemSelectedCount++
                notifyDataSetChanged()
            }
            longListener.OnLongClick(itemSelected!!,itemSelectedCount)
           }


        }






    }

    override fun getItemCount(): Int {
        return trickyList.size
    }


   inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

       var title_txt: TextView = itemView.findViewById(R.id.tricky_txt_title)
       var answer_txt: TextView = itemView.findViewById(R.id.tricky_txt_answer)
       var image: ImageView = itemView.findViewById(R.id.tricky_image)
       var linear_tricky: LinearLayout = itemView.findViewById(R.id.linear_tricky)





   }


    interface RecyclerViewActionListener {
        fun OnLongClick(itemSelected: MutableList<Int> , itemSelectedCount : Int)
    }




}


