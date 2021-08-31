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
        texttitle = view.findViewById(R.id.txt_title)
        guideline_frame = view.findViewById(R.id.guideline_frame)
        guiderecycler=view.findViewById(R.id.guideline_recycler)
         bottom_navigation =requireActivity().findViewById(R.id.bottom_navigation)
        bottom_navigation.visibility=View.GONE

        val x = null
        val l = listOf(x)


        //secure key values
        Hawk.init(requireActivity()).build()


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




    private fun showGuidline(){

            showAllGuideLine()

    }



    private fun onBackPressStyle(){
        texttitle.text= getString(R.string.Course_list)
        guideline_frame.visibility=View.GONE
    }

}