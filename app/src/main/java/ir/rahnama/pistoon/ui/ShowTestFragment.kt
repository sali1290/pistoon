package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.ExamAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.model.AllTestModel
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment
import java.util.concurrent.TimeUnit


class ShowTestFragment(
    private var dataFromDB: MutableList<AllTestModel>,
    private var type: Boolean,
    private var testId: Int,
    private var showAnswer: Boolean,
    private var clientAnswer: MutableList<Int>? = null
) : BaseFragment(), View.OnClickListener, ExamAdapter.GetAwnserListener {


    //database
    private lateinit var db: MyDatabase

    //test recycler and adapter
    private lateinit var examAdapter: ExamAdapter
    private lateinit var examrecycler: RecyclerView
    private var clientAwnsers: MutableList<Int> = arrayListOf() //awnser hae client ro save mikone
    private var trueAnswer: MutableList<Int> = arrayListOf()
    private var trueAnswerText: MutableList<String> = arrayListOf()
    private var quizid: MutableList<Int> = arrayListOf()

    //compunent
    private lateinit var btn_end_test: Button
    private lateinit var btn_back_show_test: ImageView
    private lateinit var txt_timer: TextView
    private lateinit var steper_scrollview: HorizontalScrollView

    //previus fragment name in backstack
    private var FRAGMENT_NAME_IN_BACK_STACK = "test_list"

    //timer name
    private var timer: CountDownTimer? = null

    //steper item
    private lateinit var stepsLinear1: LinearLayout
    private lateinit var stepsLinear2: LinearLayout
    private lateinit var stepsLinear3: LinearLayout
    private lateinit var stepsLinear4: LinearLayout
    private lateinit var stepsLinear5: LinearLayout
    private lateinit var stepsLinear6: LinearLayout
    private lateinit var stepsLinear7: LinearLayout
    private lateinit var stepsLinear8: LinearLayout
    private lateinit var stepsLinear9: LinearLayout
    private lateinit var stepsLinear10: LinearLayout
    private lateinit var stepsLinear11: LinearLayout
    private lateinit var stepsLinear12: LinearLayout
    private lateinit var stepsLinear13: LinearLayout
    private lateinit var stepsLinear14: LinearLayout
    private lateinit var stepsLinear15: LinearLayout
    private lateinit var stepsLinear16: LinearLayout
    private lateinit var stepsLinear17: LinearLayout
    private lateinit var stepsLinear18: LinearLayout
    private lateinit var stepsLinear19: LinearLayout
    private lateinit var stepsLinear20: LinearLayout
    private lateinit var stepsLinear21: LinearLayout
    private lateinit var stepsLinear22: LinearLayout
    private lateinit var stepsLinear23: LinearLayout
    private lateinit var stepsLinear24: LinearLayout
    private lateinit var stepsLinear25: LinearLayout
    private lateinit var stepsLinear26: LinearLayout
    private lateinit var stepsLinear27: LinearLayout
    private lateinit var stepsLinear28: LinearLayout
    private lateinit var stepsLinear29: LinearLayout
    private lateinit var stepsLinear30: LinearLayout
    private lateinit var steper_parent_view: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_test, container, false)
        //database
        db = MyDatabase(requireActivity())
        //init copunent
        btn_end_test = view.findViewById(R.id.btn_end_test)
        btn_back_show_test = view.findViewById(R.id.btn_back_show_test)
        txt_timer = view.findViewById(R.id.txt_timer)
        steper_scrollview = view.findViewById(R.id.steper_scrollview)

        //inite steper view
        initSteperView(view)
        //hide bottomnavigation
        val bottom_nav =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_nav.visibility = View.GONE
        //recycler Component
        examrecycler = view.findViewById(R.id.testRecycler)
        //client answer
        for (i in 0..29) {
            clientAwnsers.add(-1) //een meghdar dehi baraye dast resi ba index baraye javabe har test
            trueAnswer.add(dataFromDB[i].true_answer.toInt())
            quizid.add(dataFromDB[i].id.toInt())

            when (dataFromDB[i].true_answer.toInt()) {
                0 -> {
                    requireActivity().getSharedPreferences(i.toString(), Context.MODE_PRIVATE)
                        .edit()
                        .putString("key", dataFromDB[i].answer1).apply()
                }
                1 -> {
                    requireActivity().getSharedPreferences(i.toString(), Context.MODE_PRIVATE)
                        .edit()
                        .putString("key", dataFromDB[i].answer2).apply()
                }
                2 -> {
                    requireActivity().getSharedPreferences(i.toString(), Context.MODE_PRIVATE)
                        .edit()
                        .putString("key", dataFromDB[i].answer3).apply()
                }
                3 -> {
                    requireActivity().getSharedPreferences(i.toString(), Context.MODE_PRIVATE)
                        .edit()
                        .putString("key", dataFromDB[i].answer4).apply()
                }
            }

        }
        selectedSteper(0)//start steper
        //visibility of end button
        if (showAnswer) {
            btn_end_test.visibility = View.INVISIBLE
            txt_timer.visibility = View.GONE
            initSteperToShowInShowAnswerMode()
        } else {
            timer()
        }


        showTestToUser()




        btn_end_test.setOnClickListener {
            loadFragment(TestResultPage(clientAwnsers, trueAnswer, quizid, type, testId))
            timer!!.cancel()
        }
        btn_back_show_test.setOnClickListener {
            if (showAnswer) {
                alertDialog(resources.getString(R.string.BackToCourseListTitle))
            } else {
                alertDialog(resources.getString(R.string.ExitExamTitle))
            }
        }


        examrecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    selectedSteper(position)

                }

            }
        })





        stepsLinear1.setOnClickListener(this)
        stepsLinear2.setOnClickListener(this)
        stepsLinear3.setOnClickListener(this)
        stepsLinear4.setOnClickListener(this)
        stepsLinear5.setOnClickListener(this)
        stepsLinear6.setOnClickListener(this)
        stepsLinear7.setOnClickListener(this)
        stepsLinear8.setOnClickListener(this)
        stepsLinear9.setOnClickListener(this)
        stepsLinear10.setOnClickListener(this)
        stepsLinear11.setOnClickListener(this)
        stepsLinear12.setOnClickListener(this)
        stepsLinear13.setOnClickListener(this)
        stepsLinear14.setOnClickListener(this)
        stepsLinear15.setOnClickListener(this)
        stepsLinear16.setOnClickListener(this)
        stepsLinear17.setOnClickListener(this)
        stepsLinear18.setOnClickListener(this)
        stepsLinear19.setOnClickListener(this)
        stepsLinear20.setOnClickListener(this)
        stepsLinear21.setOnClickListener(this)
        stepsLinear22.setOnClickListener(this)
        stepsLinear23.setOnClickListener(this)
        stepsLinear24.setOnClickListener(this)
        stepsLinear25.setOnClickListener(this)
        stepsLinear26.setOnClickListener(this)
        stepsLinear27.setOnClickListener(this)
        stepsLinear28.setOnClickListener(this)
        stepsLinear29.setOnClickListener(this)
        stepsLinear30.setOnClickListener(this)





        return view
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initSteperToShowInShowAnswerMode() {
        for (i in 0..29) {

            if (clientAnswer!![i] == dataFromDB[i].true_answer.toInt()) {
                steper_parent_view.getChildAt(i).background =
                    requireContext().getDrawable(R.drawable.indicator_done_shape)
            } else {
                steper_parent_view.getChildAt(i).background =
                    requireContext().getDrawable(R.drawable.indicator_false_answer)
            }

            if (clientAnswer!![i] == -1) {
                steper_parent_view.getChildAt(i).background =
                    requireContext().getDrawable(R.drawable.indicator_no_answer_shape)
            }
        }


    }

    private fun showTestToUser() {

        examAdapter = if (!type) {

            val savedQuiz = db.getSavedQuizOrderedId()

            ExamAdapter(
                requireActivity(),
                dataFromDB,
                this,
                showAnswer,
                clientAnswer,
                false,
                savedQuiz
            )
        } else {
            ExamAdapter(requireActivity(), dataFromDB, this, showAnswer, clientAnswer, true, null)
        }
        examrecycler.adapter = examAdapter
        examrecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(examrecycler)


    }


    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun initSteperView(view: View) {

        stepsLinear1 = view.findViewById(R.id.stepsLinear1)
        stepsLinear1.tag = 0
        stepsLinear2 = view.findViewById(R.id.stepsLinear2)
        stepsLinear2.tag = 1
        stepsLinear3 = view.findViewById(R.id.stepsLinear3)
        stepsLinear3.tag = 2
        stepsLinear4 = view.findViewById(R.id.stepsLinear4)
        stepsLinear4.tag = 3
        stepsLinear5 = view.findViewById(R.id.stepsLinear5)
        stepsLinear5.tag = 4
        stepsLinear6 = view.findViewById(R.id.stepsLinear6)
        stepsLinear6.tag = 5
        stepsLinear7 = view.findViewById(R.id.stepsLinear7)
        stepsLinear7.tag = 6
        stepsLinear8 = view.findViewById(R.id.stepsLinear8)
        stepsLinear8.tag = 7
        stepsLinear9 = view.findViewById(R.id.stepsLinear9)
        stepsLinear9.tag = 8
        stepsLinear10 = view.findViewById(R.id.stepsLinear10)
        stepsLinear10.tag = 9
        stepsLinear11 = view.findViewById(R.id.stepsLinear11)
        stepsLinear11.tag = 10
        stepsLinear12 = view.findViewById(R.id.stepsLinear12)
        stepsLinear12.tag = 11
        stepsLinear13 = view.findViewById(R.id.stepsLinear13)
        stepsLinear13.tag = 12
        stepsLinear14 = view.findViewById(R.id.stepsLinear14)
        stepsLinear14.tag = 13
        stepsLinear15 = view.findViewById(R.id.stepsLinear15)
        stepsLinear15.tag = 14
        stepsLinear16 = view.findViewById(R.id.stepsLinear16)
        stepsLinear16.tag = 15
        stepsLinear17 = view.findViewById(R.id.stepsLinear17)
        stepsLinear17.tag = 16
        stepsLinear18 = view.findViewById(R.id.stepsLinear18)
        stepsLinear18.tag = 17
        stepsLinear19 = view.findViewById(R.id.stepsLinear19)
        stepsLinear19.tag = 18
        stepsLinear20 = view.findViewById(R.id.stepsLinear20)
        stepsLinear20.tag = 19
        stepsLinear21 = view.findViewById(R.id.stepsLinear21)
        stepsLinear21.tag = 20
        stepsLinear22 = view.findViewById(R.id.stepsLinear22)
        stepsLinear22.tag = 21
        stepsLinear23 = view.findViewById(R.id.stepsLinear23)
        stepsLinear23.tag = 22
        stepsLinear24 = view.findViewById(R.id.stepsLinear24)
        stepsLinear24.tag = 23
        stepsLinear25 = view.findViewById(R.id.stepsLinear25)
        stepsLinear25.tag = 24
        stepsLinear26 = view.findViewById(R.id.stepsLinear26)
        stepsLinear26.tag = 25
        stepsLinear27 = view.findViewById(R.id.stepsLinear27)
        stepsLinear27.tag = 26
        stepsLinear28 = view.findViewById(R.id.stepsLinear28)
        stepsLinear28.tag = 27
        stepsLinear29 = view.findViewById(R.id.stepsLinear29)
        stepsLinear29.tag = 28
        stepsLinear30 = view.findViewById(R.id.stepsLinear30)
        stepsLinear30.tag = 29
        steper_parent_view = view.findViewById(R.id.steper_parent_view)


    }


    private fun timer() {
        timer = object : CountDownTimer(1200000, 1000) {
            @SuppressLint("SetTextI18n", "DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val hms = java.lang.String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                txt_timer.text = hms

            }

            override fun onFinish() {
                onFinishTimeAlertDialog()
            }
        }.start()
    }


    override fun onClick(p0: View?) {
        val position: Int = p0!!.tag as Int
        examrecycler.layoutManager!!.scrollToPosition(position)
        selectedSteper(position)
    }

    override fun getAwsner(clientAnswer: MutableList<Int>) {
        this.clientAwnsers = clientAnswer
    }

    override fun savedQuizId(quizid: Int) {
        db.setSaveQuizId(quizid)
    }

    override fun DeleteSavedQuizId(quizid: Int) {
        db.deleteSavedQuiz(quizid.toString())
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun selectedSteper(position: Int) {

        if (showAnswer) {
            initSteperToShowInShowAnswerMode()
        } else {
            for (i in 0..29) {
                if (clientAwnsers[i] != -1) {
                    steper_parent_view.getChildAt(i).background =
                        requireContext().getDrawable(R.drawable.indicator_do_answer_shape)
                } else {
                    steper_parent_view.getChildAt(i).background =
                        requireContext().getDrawable(R.drawable.indicator_no_answer_shape)
                }
            }

        }
        when (position) {
            in 1..10 -> steper_scrollview.smoothScrollTo(steper_parent_view.scrollX, 0)
            in 10..20 -> steper_scrollview.smoothScrollTo(steper_parent_view.width / 3, 0)
            in 20..29 -> steper_scrollview.smoothScrollTo(steper_parent_view.width, 0)
        }
        steper_parent_view.getChildAt(position).background =
            requireContext().getDrawable(R.drawable.indicator_golden_selector)

    }


    private fun alertDialog(alert_title: String) {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_show_test_page, null)

        val title = dialogLayout.findViewById<TextView>(R.id.alert_txt_title)
        val btn_ok = dialogLayout.findViewById<TextView>(R.id.btn_alert_ok)
        val btn_exit = dialogLayout.findViewById<TextView>(R.id.btn_alert_exit)

        title.text = alert_title

        btn_ok.setOnClickListener { builder.dismiss() }
        btn_exit.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            requireActivity().supportFragmentManager.popBackStack(
                FRAGMENT_NAME_IN_BACK_STACK,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            fragmentManager.remove(this).commit()

            if (!showAnswer) {
                timer!!.cancel()
            }

            builder.dismiss()

        }

        builder.setView(dialogLayout)
        builder.show()
    }

    private fun onFinishTimeAlertDialog() {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.on_finish_timer_alert, null)

        val btn_result = dialogLayout.findViewById<Button>(R.id.btn_result)


        btn_result.setOnClickListener {
            loadFragment(TestResultPage(clientAwnsers, trueAnswer, quizid, type, testId))
            builder.dismiss()
        }

        builder.setCancelable(false)
        builder.setView(dialogLayout)
        builder.show()
    }


    override fun onBackPressed(): Boolean {

        if (showAnswer) {
            alertDialog(resources.getString(R.string.BackToCourseListTitle))
        } else {
            alertDialog(resources.getString(R.string.ExitExamTitle))
        }
        return true
    }

}
