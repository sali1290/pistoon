package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.AllTestModel

class ExamAdapter(
    var context: Context,
    private var data: MutableList<AllTestModel>,
    private var getterTool: GetAwnserListener,
    private var showAnswer: Boolean,
    private var clientAnswer: MutableList<Int>?,
    private var testType: Boolean,
    private var savedQuiz: MutableList<Int>?
) : RecyclerView.Adapter<ExamAdapter.MyViewHolder>() {


    private var clientAnswersClicked: MutableList<Int> = arrayListOf()
    private var saved: MutableList<Int> = arrayListOf()


    private var images = arrayOf(
        R.drawable.image1,
        R.drawable.image5,
        R.drawable.image5,
        R.drawable.image6,
        R.drawable.image7,
        R.drawable.image8,
        R.drawable.image9,
        R.drawable.image9,
        R.drawable.image10,
        R.drawable.image12,
        R.drawable.image14,
        R.drawable.image15,
        R.drawable.image17,
        R.drawable.image18,
        R.drawable.image19,
        R.drawable.image20,
        R.drawable.image22,
        R.drawable.image24,
        R.drawable.image25,
        R.drawable.image26,
        R.drawable.image27,
        R.drawable.image28,
        R.drawable.image30
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.exam_page, parent, false)
        return MyViewHolder(view)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ExamAdapter.MyViewHolder, position: Int) {


        holder.test_title.text = data[holder.adapterPosition].title
        holder.txt_answer1.text = data[holder.adapterPosition].answer1
        holder.txt_answer2.text = data[holder.adapterPosition].answer2
        holder.txt_answer3.text = data[holder.adapterPosition].answer3
        holder.txt_answer4.text = data[holder.adapterPosition].answer4

        if (data[holder.adapterPosition].image == null) {
            holder.imageView.setImageResource(images[0])
            images.shuffle()
        } else {
            holder.imageView.setImageBitmap(data[holder.adapterPosition].image)
        }




        if (showAnswer) {
            holder.test_bookmark.visibility = View.VISIBLE
            setAllCardViewToDefault(holder)
            ShowTrueAnswerToUser(holder, data[holder.layoutPosition].true_answer.toInt())
        } else {
            setAllCardViewToDefault(holder)
            if (clientAnswersClicked[holder.layoutPosition] != -1) {
                holder.test_cardview_parent.getChildAt(clientAnswersClicked[holder.layoutPosition]).background =
                    context.getDrawable(R.drawable.checked_user_answer)
            }
        }



        if (!testType && showAnswer) {
            for (i in savedQuiz!!) {
                if (i == data[holder.layoutPosition].id.toInt()) {
                    saved[holder.layoutPosition] = 1
                }
            }
            holder.test_bookmark.isChecked = saved[holder.layoutPosition] == 1
            holder.test_bookmark.setOnClickListener {
                if (saved[holder.layoutPosition] == 0) {
                    saved[holder.layoutPosition] = 1
                    getterTool.savedQuizId(data[holder.layoutPosition].id.toInt())
                } else {
                    saved[holder.layoutPosition] = 0
                    getterTool.DeleteSavedQuizId(data[holder.layoutPosition].id.toInt())
                }
            }
        } else {
            holder.test_bookmark.visibility = View.GONE
        }




        holder.cardview1.setOnClickListener { changingCardviewShape(holder, 0) }
        holder.cardview2.setOnClickListener { changingCardviewShape(holder, 1) }
        holder.cardview3.setOnClickListener { changingCardviewShape(holder, 2) }
        holder.cardview4.setOnClickListener { changingCardviewShape(holder, 3) }


    }


    override fun getItemCount(): Int {
        return data.size
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAllCardViewToDefault(holder: ExamAdapter.MyViewHolder) {
        for (i in 0..3) {
            holder.test_cardview_parent.getChildAt(i).background =
                context.getDrawable(R.drawable.radio_btn_shape)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun ShowTrueAnswerToUser(holder: ExamAdapter.MyViewHolder, trueAnswer: Int) {

        if (clientAnswer!![holder.layoutPosition] != -1) {
            holder.test_cardview_parent.getChildAt(clientAnswer!![holder.layoutPosition]).background =
                context.getDrawable(R.drawable.user_answer_false_shape)
        }

        for (i in 0..29) {
            when (context.getSharedPreferences(i.toString(), Context.MODE_PRIVATE)
                .getString("key", null)) {
                holder.txt_answer1.text -> {
                    holder.test_cardview_parent.getChildAt(0).background =
                        context.getDrawable(R.drawable.true_answer_shape)
                    break
                }
                holder.txt_answer2.text -> {
                    holder.test_cardview_parent.getChildAt(1).background =
                        context.getDrawable(R.drawable.true_answer_shape)
                    break
                }
                holder.txt_answer3.text -> {
//                    Toast.makeText(context , context.getSharedPreferences(i.toString(), Context.MODE_PRIVATE)
//                        .getString("key", null) , Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context , holder.txt_answer3.text , Toast.LENGTH_SHORT).show()
                    holder.test_cardview_parent.getChildAt(2).background =
                        context.getDrawable(R.drawable.true_answer_shape)
                    break
                }
                holder.txt_answer4.text -> {
                    holder.test_cardview_parent.getChildAt(3).background =
                        context.getDrawable(R.drawable.true_answer_shape)
                    break
                }
            }
        }


//        holder.test_cardview_parent.getChildAt(trueAnswer).background =
//            context.getDrawable(R.drawable.true_answer_shape)

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changingCardviewShape(holder: ExamAdapter.MyViewHolder, cardviewNumber: Int) {
        if (!showAnswer) {
            setAllCardViewToDefault(holder)
            holder.test_cardview_parent.getChildAt(cardviewNumber).background =
                context.getDrawable(R.drawable.checked_user_answer)
            getterTool.getAwsner(clientAnswersClicked)
            clientAnswersClicked[holder.layoutPosition] = cardviewNumber
        }

    }


    interface GetAwnserListener {
        fun getAwsner(clientAnswer: MutableList<Int>)
        fun savedQuizId(quizid: Int)
        fun DeleteSavedQuizId(quizid: Int)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var txt_answer1 = itemView.findViewById<TextView>(R.id.txt_text_answer1)
        var txt_answer2 = itemView.findViewById<TextView>(R.id.txt_text_answer2)
        var txt_answer3 = itemView.findViewById<TextView>(R.id.txt_text_answer3)
        var txt_answer4 = itemView.findViewById<TextView>(R.id.txt_text_answer4)
        var cardview1 = itemView.findViewById<CardView>(R.id.test_cardview1)
        var cardview2 = itemView.findViewById<CardView>(R.id.test_cardview2)
        var cardview3 = itemView.findViewById<CardView>(R.id.test_cardview3)
        var cardview4 = itemView.findViewById<CardView>(R.id.test_cardview4)
        var test_cardview_parent = itemView.findViewById<LinearLayout>(R.id.test_cardview_parent)
        var imageView = itemView.findViewById<ImageView>(R.id.testImageView)
        var test_title = itemView.findViewById<TextView>(R.id.quizTestTextView)
        var test_bookmark = itemView.findViewById<ToggleButton>(R.id.test_bookmark)


    }


    init {
        for (i in 0..29) {
            clientAnswersClicked.add(-1)
            saved.add(0)
        }
    }


}


