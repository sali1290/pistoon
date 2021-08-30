package ir.rahnama.pistoon.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.GuideLineAdapter
import ir.rahnama.pistoon.data.MyDatabase


class BoardCourseList : Fragment() {


    lateinit var db : MyDatabase
    private lateinit var guiderecycler : RecyclerView
    private lateinit var guideline_frame : FrameLayout
    private lateinit var texttitle : TextView
    private lateinit var bottom_navigation : BottomNavigationView

    private var backCount = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_board_course_list, container, false)

        val board_cardview = view.findViewById<CardView>(R.id.Board_course_cardview)
        val guideline_cardview = view.findViewById<CardView>(R.id.GuideLine_cardview)
        val image_back = view.findViewById<ImageView>(R.id.img_back)
        val gudline_ic_lock = view.findViewById<ImageView>(R.id.gudline_ic_lock)
        texttitle = view.findViewById(R.id.txt_title)
        guideline_frame = view.findViewById(R.id.guideline_frame)
        guiderecycler=view.findViewById(R.id.guideline_recycler)
         bottom_navigation =requireActivity().findViewById(R.id.bottom_navigation)
        bottom_navigation.visibility=View.GONE


        //secure key values
        Hawk.init(requireActivity()).build()


        if (Hawk.get("fullAccount",0) == 20 ){
            gudline_ic_lock.visibility=View.INVISIBLE
        }




        board_cardview.setOnClickListener{loadFragment(ShowBoardFragment())}

        guideline_cardview.setOnClickListener{showGuidline()}

        image_back.setOnClickListener{

            if (backCount == 1){
                onBackPressStyle()
                backCount=0
            }else{
                popUpLastFragment()
            }




        }


        return view
    }


    private fun popUpLastFragment(){

        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.popBackStack()


    }


    private fun loadFragment(fragment : Fragment){

        val fm : FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
        fm.replace(R.id.container,fragment)
        fm.addToBackStack(null)
        fm.commit()

    }


    private fun showAllGuideLine(){
        guideline_frame.visibility=View.VISIBLE
        texttitle.text= getString(R.string.guideline_course)
        backCount=1
        db= MyDatabase(requireContext())
        val guideList = db.getAllGuideLine()
        val adapter = GuideLineAdapter(requireActivity(),guideList)
        guiderecycler.adapter=adapter
        guiderecycler.layoutManager= LinearLayoutManager(requireActivity())



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
            bottom_navigation.selectedItemId=R.id.more_nav
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
        }


        builder.setView(dialogLayout)
        builder.show()
    }


    private fun showGuidline(){

        if (Hawk.get("fullAccount",0) == 20){
            showAllGuideLine()
        }else{
            alertDialog(resources.getString(R.string.AccessToGuidLine))
        }

    }



    private fun onBackPressStyle(){
        texttitle.text= getString(R.string.Course_list)
        guideline_frame.visibility=View.GONE
    }

}