package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment


class TestResultPage(
    private var clientAnswer: MutableList<Int>,
    private var trueAnswer: MutableList<Int>,
    private var quizid: MutableList<Int>,
    private var type: Boolean,
    private var testId: Int
) : BaseFragment() {

    private lateinit var test_result_percent:TextView
    private lateinit var txt_trueAnswer_counter:TextView
    private lateinit var txt_test_result_title:TextView
    private lateinit var txt_test_result_title2:TextView
    private lateinit var txt_test_result_body:TextView
    private lateinit var test_result_progressbar:CircularProgressBar
    private lateinit var try_again_btn:Button
    private lateinit var show_answers_btn:Button
    private lateinit var btn_back_test_result:ImageView
    //data base
    private lateinit var db : MyDatabase
    //test percent
    private var percent=0
    //previus fragment name in backstack
    private var FRAGMENT_NAME_IN_BACK_STACK="test_list"


    private var userTrueAnswerCounter:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_test_result_page, container, false)

        test_result_percent=view.findViewById(R.id.test_result_percent)
        txt_trueAnswer_counter=view.findViewById(R.id.txt_trueAnswer_counter)
        txt_test_result_title=view.findViewById(R.id.txt_test_result_title)
        txt_test_result_title2=view.findViewById(R.id.txt_test_result_title2)
        txt_test_result_body=view.findViewById(R.id.txt_test_result_body)
        test_result_progressbar=view.findViewById(R.id.test_result_progressbar)
        show_answers_btn=view.findViewById(R.id.show_answers_btn)
        try_again_btn=view.findViewById(R.id.try_again_btn)
        btn_back_test_result=view.findViewById(R.id.btn_back_test_result)
        //data base init
        db= MyDatabase(requireContext())




        getResult()//get result test from prev fragment
        setResult()//set result and show user count of percent
        saveTestResult()//save percent in database




        try_again_btn.setOnClickListener{
            fragmentInit(testId,false)
            saveTrickyTest()
        }
        show_answers_btn.setOnClickListener{
            fragmentInit(testId,true)
            saveTrickyTest()
        }

        btn_back_test_result.setOnClickListener{
            alertDialog(resources.getString(R.string.BackToCourseListTitle))
        }



        return view
    }



    private fun saveTrickyTest(){

        if(!type){
            for (i in 0 .. 29){
                if (clientAnswer[i]!=trueAnswer[i]){
                    db.saveTrickyTest(quizid[i])
                }
            }
        }

    }

    private fun saveTestResult(){
        if (type){
            db.setExamPercent(testId.toString(),percent)
        }else{
            db.setQuizPercent(testId.toString(),percent)
        }
    }


    private fun getResult(){
        for (i in 0..29){
            if (clientAnswer[i] == trueAnswer[i]){
                userTrueAnswerCounter ++
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setResult(){
        percent=((100 * userTrueAnswerCounter)/30)
        test_result_percent.text="$percent%"
        txt_trueAnswer_counter.text=userTrueAnswerCounter.toString()
        test_result_progressbar.progress = percent.toFloat()
        when (percent) {
            in 0..50 -> {
                initResultColorAndText(resources.getColor(R.color.trikyRed),
                    getString(R.string.test_result_title_weak),getString(R.string.test_result_title2_weak),
                    getString(R.string.test_result_body_weak))
                txt_test_result_title.setTextColor(resources.getColor(R.color.trikyRed))

            }
            in 51..79 -> {
                initResultColorAndText(resources.getColor(R.color.golden),
                    getString(R.string.test_result_title_medium),getString(R.string.test_result_title2_medium),
                    getString(R.string.test_result_body_medium))
            }
            in 80..100 -> {
                initResultColorAndText(resources.getColor(R.color.light_green),
                    getString(R.string.test_result_title_good),getString(R.string.test_result_title2_good),
                    getString(R.string.test_result_body_good))
            }
        }

    }

    private fun fragmentInit (testId:Int, showAnswer:Boolean) {

        val testFragment = when(type){
            true -> ShowTestFragment(db.getExamTests(testId) , true , testId,showAnswer,clientAnswer)
            false -> ShowTestFragment( db.getQuizTests(testId) , false , testId,showAnswer,clientAnswer)
        }
        val fragmentManager  : FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container,testFragment).commit()

    }



    private fun initResultColorAndText(color: Int,title:String,title2:String,body:String){
        test_result_progressbar.progressBarColor=color
        txt_trueAnswer_counter.setTextColor(color)
        test_result_percent.setTextColor(color)
        txt_test_result_title.text=title
        txt_test_result_title2.text=title2
        txt_test_result_body.text=body

    }


    private fun alertDialog(alert_title: String) {
        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_show_test_page, null)

        val title = dialogLayout.findViewById<TextView>(R.id.alert_txt_title)
        val btn_ok = dialogLayout.findViewById<TextView>(R.id.btn_alert_ok)
        val btn_exit = dialogLayout.findViewById<TextView>(R.id.btn_alert_exit)

        title.text = alert_title

        btn_ok.setOnClickListener{ builder.dismiss() }
        btn_exit.setOnClickListener{
            val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
            requireActivity().supportFragmentManager.popBackStack(FRAGMENT_NAME_IN_BACK_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager.remove(this).commit()


            builder.dismiss()

        }


        builder.setView(dialogLayout)
        builder.show()
    }


    override fun onBackPressed(): Boolean {
        alertDialog(resources.getString(R.string.BackToCourseListTitle))
        return true
    }

}