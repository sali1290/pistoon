package ir.rahnama.pistoon.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.PracticalCourseListAdapter
import ir.rahnama.pistoon.data.MyDatabase


class ShowListOfPracticalCourseFragment : Fragment() , PracticalCourseListAdapter.ItemClickListener{


    private lateinit var db : MyDatabase
    private lateinit var CourseListRecyclerView : RecyclerView
    private lateinit var bottomNavigationView : BottomNavigationView
    private var fullAccount=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_list_of_practical_course, container, false)

        CourseListRecyclerView =view.findViewById(R.id.practical_course_recycler)
        val practical_course_btn_back=view.findViewById<ImageView>(R.id.practical_course_btn_back)
        bottomNavigationView =requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility=View.GONE

        Hawk.init(requireActivity()).build()

        if (Hawk.get("fullAccount",0) == 20){
            fullAccount=true
        }


        getPracticalCourse()

        practical_course_btn_back.setOnClickListener{
            val fm : FragmentManager=requireActivity().supportFragmentManager
            fm.popBackStack()
        }


        return view
    }



    private fun getPracticalCourse(){

        db= MyDatabase(requireContext())
        val courseList = db.getPracticalCourse()
        val adapter = PracticalCourseListAdapter(requireContext(), courseList,this,fullAccount)
        CourseListRecyclerView.adapter=adapter
        CourseListRecyclerView.layoutManager= LinearLayoutManager(requireContext())

    }


    private fun loadFragment(fragment: Fragment){

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

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
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
        }


        builder.setView(dialogLayout)
        builder.show()
    }


    override fun onItemClick(id: Int) {
        if (fullAccount){
            when(id){
                1 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("77","98"), null))
                2 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("92","225"), null))
                3 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("112","57"), null))
                4 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("211","43"), null))
                5 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("2","221"), null))
                6 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("73","70"), null))
                7 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("1","244"), null))
                8 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("56","76"), null))
            }
        }else{
            when(id){
                1 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("77","98"), null))
                2 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("92","225"), null))
                3 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("112","57"), null))
                4 -> loadFragment(ShowCourseFragment(true, id.toString(), arrayListOf("211","43"), null))
                else -> alertDialog(resources.getString(R.string.AccessToCourseAlert))
            }
        }

    }


}