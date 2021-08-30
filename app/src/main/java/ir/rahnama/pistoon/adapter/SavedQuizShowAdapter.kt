package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.AllTestModel

class SavedQuizShowAdapter(var context: Context,private var data:MutableList<AllTestModel> , private var listener:DeleteSavedQuiz) : RecyclerView.Adapter<SavedQuizShowAdapter.MyViewHolder>() {





    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedQuizShowAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.saved_quiz_item_model,parent,false))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: SavedQuizShowAdapter.MyViewHolder, position: Int) {

        holder.saved_quiz_title.text=data[position].title


        val answers= arrayOf(data[position].answer1, data[position].answer2, data[position].answer3, data[position].answer4)
        val id  =data[position].true_answer.toInt()
        holder.saved_quiz_txt1.text= answers[id]


        if(data[position].image==null){
            holder.saved_item_model_image.visibility=View.GONE
        }else{
            holder.saved_item_model_image.visibility=View.VISIBLE
            holder.saved_item_model_image.setImageBitmap(data[position].image)
        }





        holder.saved_quiz_bookmark.setOnClickListener{
                listener.DeleteSavedQuizId(data[position].id.toString())
            Log.i("test",data[position].id.toString())
        }


    }

    override fun getItemCount(): Int {
     return data.size
    }


    inner class MyViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){


        val saved_quiz_txt1=itemview.findViewById<TextView>(R.id.saved_quiz_txt1)
        val saved_quiz_title=itemview.findViewById<TextView>(R.id.saved_quiz_title)
        val saved_item_model_image=itemview.findViewById<ImageView>(R.id.saved_item_model_image)
        val saved_quiz_bookmark=itemview.findViewById<ImageView>(R.id.saved_quiz_bookmark)





    }



    interface DeleteSavedQuiz{
        fun DeleteSavedQuizId(quizid:String)
    }





}

