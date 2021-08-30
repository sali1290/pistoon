package ir.rahnama.pistoon.ui.mainfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.ui.GoldenTestFragment
import ir.rahnama.pistoon.ui.saved.SavedListFragment
import ir.rahnama.pistoon.ui.ShowCourseFragment
import ir.rahnama.pistoon.ui.TrickyFragment
import ir.rahnama.pistoon.ui.game.MainPageOfGameFragment
import java.lang.Exception


class  HomeFragment : BaseFragment() {

    private lateinit var db : MyDatabase
    private lateinit var theoryProgressBar : ProgressBar
    private lateinit var practicalProgressBar : ProgressBar
    private lateinit var theoryPercentTextView : TextView
    private lateinit var practicalPercentTextView : TextView
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var theoryMedal : ImageView
    private lateinit var practicalMedal : ImageView
    private lateinit var testProgressBar : CircularProgressBar
    private lateinit var lastSeen_img : ImageView
    private lateinit var lastSeen_name : TextView
    private lateinit var last_seen_cardview : CardView
    private lateinit var examPercent : TextView
    private lateinit var avrage_txt2 : TextView
    private lateinit var goldenTest_ic_lock : ImageView


    //last seen
    private var type = true
    private var ids = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_home, container, false)

        db= MyDatabase(requireActivity())


        val golden_test_button = view.findViewById<ConstraintLayout>(R.id.layout_golden_test)

        theoryProgressBar=view.findViewById(R.id.theory_progressbar)
        practicalProgressBar=view.findViewById(R.id.practical_progressbar)
        theoryPercentTextView=view.findViewById(R.id.theory_percent_txt)
        practicalPercentTextView=view.findViewById(R.id.practical_percent_txt)
        theoryMedal=view.findViewById(R.id.theory_medal)
        practicalMedal=view.findViewById(R.id.practical_medal)
        lastSeen_img=view.findViewById(R.id.last_seen_image)
        lastSeen_name=view.findViewById(R.id.last_seen_name)
         testProgressBar=view.findViewById(R.id.testProgressBar)
        last_seen_cardview=view.findViewById(R.id.last_seen_cardview)
        examPercent=view.findViewById(R.id.examPercent)
        avrage_txt2=view.findViewById(R.id.avrage_txt2)
        goldenTest_ic_lock=view.findViewById(R.id.golden_test_lock)
        val  tricky_cardview=view.findViewById<CardView>(R.id.tricky_cardview)
        val  saved_cardview=view.findViewById<CardView>(R.id.saved_cardview)
        val  game_cardview=view.findViewById<CardView>(R.id.cardview_game)
         bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility=View.VISIBLE


        //secure key values
        Hawk.init(requireActivity()).build()


        golden_test_button.setOnClickListener{checkAccountAndOpenGoldenTest()}
        game_cardview.setOnClickListener{loadFragment(MainPageOfGameFragment())}

        getPercent()
        LastSeen()


        //check golden test show ic
        if ( Hawk.get("fullAccount",0) == 20 || Hawk.get("goldenAccount" , 0) == 10 ){
            goldenTest_ic_lock.visibility=View.GONE
        }


        try {
            setTheoryProgressBar()
            setpracticalProgressBar()
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(requireActivity(), "مشکلی رخ داده است", Toast.LENGTH_SHORT).show()
        }


        tricky_cardview.setOnClickListener{loadFragment(TrickyFragment())}
        saved_cardview.setOnClickListener{loadFragment(SavedListFragment())}
        last_seen_cardview.setOnClickListener{loadLastSeenCourse()}

        return view
    }

    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    @SuppressLint("SetTextI18n")
    private fun setTheoryProgressBar(){
            val percent = db.getTheoryQuizIsDone()
            theoryProgressBar.progress = percent.toInt() * 25
            theoryPercentTextView.text=theoryProgressBar.progress.toString() + "%"
        if (theoryProgressBar.progress == 100) theoryMedal.setImageResource(R.drawable.ic_golden_medal)
    }

    @SuppressLint("SetTextI18n")
    private fun setpracticalProgressBar(){
            val percent = db.getPracticalQuizIsDone()
            practicalProgressBar.progress = (percent.toInt() * 12.5).toInt()
            practicalPercentTextView.text=practicalProgressBar.progress.toString() + "%"
        if (practicalProgressBar.progress == 100) practicalMedal.setImageResource(R.drawable.ic_golden_medal)
    }

    @SuppressLint("SetTextI18n")
    private fun getPercent(){

        val percent1 = db.getQuizTruePercent()
        val percent2 = db.getExamTruePercent()
        val test= (percent1 + percent2) /30
        testProgressBar.progress=test.toFloat()
        examPercent.text="$test%"

        when (test) {
            in 1..50 -> {
                examPercent.setTextColor(resources.getColor(R.color.trikyRed))
                testProgressBar.progressBarColor = resources.getColor(R.color.trikyRed)
                avrage_txt2.text=getString(R.string.week_avrage_text2)
            }
            in 51..79 -> {
                examPercent.setTextColor(resources.getColor(R.color.golden))
                testProgressBar.progressBarColor = resources.getColor(R.color.golden)
                avrage_txt2.text=getString(R.string.normal_avrage_text2)
            }
            in 80..100 -> {
                examPercent.setTextColor(resources.getColor(R.color.light_green))
                testProgressBar.progressBarColor = resources.getColor(R.color.light_green)
                avrage_txt2.text=getString(R.string.strong_avrage_text2)
            }
        }

    }

    private fun LastSeen(){
        val sharedpref =requireActivity().getSharedPreferences("lastSeen", Context.MODE_PRIVATE)
         ids =sharedpref.getInt("id",1)
         type=sharedpref.getBoolean("courseType",true)
        if (type){
            val lastseen=db.getPracticalCourseById(ids.toString())
            if (lastseen.size>0){
                lastSeen_name.text=lastseen[0].title
                lastSeen_img.setImageBitmap(lastseen[0].image)
            }
        }else{
            val lastseen=db.getTheoryCourseById(ids.toString())
            if (lastseen.size>0){
                lastSeen_name.text=lastseen[0].title
                lastSeen_img.setImageBitmap(lastseen[0].image)
            }
        }
    }

    private fun loadLastSeenCourse(){
        when(type){
            true -> loadFragment(ShowCourseFragment(
                true,
                ids.toString(),
                arrayListOf("1","2"),
                null
            ))
            else -> loadFragment(ShowCourseFragment(
                false,
                ids.toString(),
                arrayListOf("1","2"),
                null
            ))
        }

    }



    private fun verifyGoldenAccount():Boolean{
        val sharedpref =requireActivity().getSharedPreferences("appAccount", Context.MODE_PRIVATE)
        return sharedpref.getBoolean("golden", false)
    }
    private fun verifyFullAccount():Boolean{
        val sharedpref =requireActivity().getSharedPreferences("appAccount", Context.MODE_PRIVATE)
        return sharedpref.getBoolean("full", false)
    }


    private fun checkAccountAndOpenGoldenTest(){
        if (Hawk.get("fullAccount",0) == 20 || Hawk.get("goldenAccount" , 0) == 10){
            loadFragment(GoldenTestFragment())
        }else{
            alertDialog(resources.getString(R.string.AccessToGoldenAlert))
        }
    }



    private fun alertDialog(alert_title: String) {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_locked_section, null)

        val title = dialogLayout.findViewById<TextView>(R.id.alert_txt_title)
        val btn_ok = dialogLayout.findViewById<TextView>(R.id.btn_alert_ok)

        title.text = alert_title

        btn_ok.setOnClickListener{
            builder.dismiss()
            bottomNavigationView.selectedItemId=R.id.more_nav
        }


        builder.setView(dialogLayout)
        builder.show()
    }



    override fun onBackPressed(): Boolean {
        requireActivity().finishAndRemoveTask()
        return true
    }

}