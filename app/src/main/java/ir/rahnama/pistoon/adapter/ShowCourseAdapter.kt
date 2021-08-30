package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.AllTestModel
import ir.rahnama.pistoon.model.CourseListModel
import java.util.*


class ShowCourseAdapter(
    var context: Context,
    private var courseList: MutableList<CourseListModel>,
    private var quizList: MutableList<AllTestModel>,
    private var listener: OnClickedListener,
    private var savedPage: MutableList<Int>
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val LAYOUT_COURSE = 0
    private val LAYOUT_QUIZ = 1
    //page number for saving
    private var saved : MutableList<Int>  = arrayListOf(0,0,0,0,0,0,0,0)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        for (i in savedPage){
            saved[i]=1
        }
        return when(viewType){
            LAYOUT_COURSE -> CourseViewHolder(
                    inflater.inflate(
                            R.layout.course_page_model,
                            parent,
                            false
                    )
            )
            else -> QuizViewHolder(inflater.inflate(R.layout.quiz_page_model, parent, false))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if (holder.itemViewType==LAYOUT_COURSE){
            holder as CourseViewHolder
            holder.courseimage.setImageBitmap(courseList[0].image)
            holder.coursetitle.text=courseList[0].title




            when(holder.layoutPosition){
                0 -> { holder.body.text = courseList[0].page1 }
                1 -> { holder.body.text = courseList[0].page2 }
                2 -> { holder.body.text = courseList[0].page3 }
                4 -> { holder.body.text = courseList[0].page4 }
                5 -> { holder.body.text = courseList[0].page5 }
                7 -> { holder.body.text = courseList[0].page6 }
            }


            holder.btn_bookmark.isChecked = saved[holder.layoutPosition]==1
            holder.btn_bookmark.setOnClickListener{

                 if (saved[holder.layoutPosition] == 0) {
                    saved[holder.layoutPosition] = 1
                     listener.bookmarkPage(courseList[0].id.toString(),holder.layoutPosition.toString())
                } else {
                    saved[holder.layoutPosition] = 0
                     listener.DeletebookmarkPage(courseList[0].id.toString(),holder.layoutPosition.toString())
                }
            }


        }else{
            holder as QuizViewHolder
           when(holder.layoutPosition){
               3 -> {InitShowQuizToUser(holder,0)}
               6 -> {InitShowQuizToUser(holder,1)}
           }


            holder.radio_group.setOnCheckedChangeListener { group: RadioGroup, checkedId: Int ->

                val seleted= group.findViewById<RadioButton>(holder.radio_group.checkedRadioButtonId)
                var trueAnswer = 0
                when(holder.layoutPosition){
                    3 -> trueAnswer=quizList[0].true_answer.toInt()
                    6 -> trueAnswer=quizList[1].true_answer.toInt()
                }

                if (holder.radioId.indexOf(seleted)!=trueAnswer){
                    seleted.background = context.getDrawable(R.drawable.quiz_false_shape)
                    seleted.setTextColor(context.getColor(R.color.trikyRed))
                    seleted.buttonTintList = context.getColorStateList(R.color.trikyRed)
                    when(holder.layoutPosition) {
                        3 -> listener.firstAnswer(false)
                        6 -> listener.secondAnswer(false)
                    }
                }

                holder.radioId[trueAnswer].background = context.getDrawable(R.drawable.quiz_true_shape)
                holder.radioId[trueAnswer].setTextColor(context.getColor(R.color.light_green))
                holder.radioId[trueAnswer].buttonTintList = context.getColorStateList(R.color.light_green)
                for (i in holder.radioId.indices) {
                    holder.radioId[i].isEnabled = false
                }


                if (holder.radioId.indexOf(seleted)==trueAnswer){
                    when(holder.layoutPosition) {
                        3 -> listener.firstAnswer(true)
                        6 -> listener.secondAnswer(true)
                    }
                }
            }

        }

    }



    override fun getItemViewType(position: Int): Int {

        return when(position){
            3 -> LAYOUT_QUIZ
            6 -> LAYOUT_QUIZ
            else -> LAYOUT_COURSE
        }

    }


    private fun InitShowQuizToUser(holder: ShowCourseAdapter.QuizViewHolder,quizNumber:Int){

        if (quizList[quizNumber].image == null) {
            holder.quizimage.setImageBitmap(courseList[0].image)
        } else {
            holder.quizimage.setImageBitmap(quizList[quizNumber].image)
        }
        holder.quiztitle.text = quizList[quizNumber].title
        holder.btn_radio1.text = quizList[quizNumber].answer1
        holder.btn_radio2.text = quizList[quizNumber].answer2
        holder.btn_radio3.text = quizList[quizNumber].answer3
        holder.btn_radio4.text = quizList[quizNumber].answer4

    }



    inner class CourseViewHolder(var itemView: View):RecyclerView.ViewHolder(itemView){

        var courseimage = itemView.findViewById<ImageView>(R.id.course_image)
        var coursetitle = itemView.findViewById<TextView>(R.id.course_txt_title)
        var body = itemView.findViewById<TextView>(R.id.course_txt_body)
        var btn_bookmark = itemView.findViewById<ToggleButton>(R.id.course_bookmark)


    }


    inner class QuizViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){


         var radioId = ArrayList<RadioButton>()
        init {
            radioId.add(itemView.findViewById(R.id.quiz_radio_btn1))
            radioId.add(itemView.findViewById(R.id.quiz_radio_btn2))
            radioId.add(itemView.findViewById(R.id.quiz_radio_btn3))
            radioId.add(itemView.findViewById(R.id.quiz_radio_btn4))


        }


        var radio_group = itemView.findViewById<RadioGroup>(R.id.quiz_radio_group)
        var btn_radio1 = itemView.findViewById<RadioButton>(R.id.quiz_radio_btn1)
        var btn_radio2 = itemView.findViewById<RadioButton>(R.id.quiz_radio_btn2)
        var btn_radio3 = itemView.findViewById<RadioButton>(R.id.quiz_radio_btn3)
        var btn_radio4 = itemView.findViewById<RadioButton>(R.id.quiz_radio_btn4)
        var quiztitle = itemView.findViewById<TextView>(R.id.txt_quiz_title)
        var quizimage = itemView.findViewById<ImageView>(R.id.quiz_imageView)


    }

    override fun getItemCount(): Int {
        return 8
    }



     interface OnClickedListener{
        fun firstAnswer(state: Boolean)
        fun secondAnswer(state: Boolean)
        fun bookmarkPage(courseId: String, coursePage: String)
        fun DeletebookmarkPage(courseId: String, coursePage: String)
    }





}