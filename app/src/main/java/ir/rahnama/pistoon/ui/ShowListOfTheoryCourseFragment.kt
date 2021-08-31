package ir.rahnama.pistoon.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.TheoryCourseListAdapter
import ir.rahnama.pistoon.data.MyDatabase

class ShowListOfTheoryCourseFragment : Fragment() , TheoryCourseListAdapter.ItemClickListener{


    private lateinit var db : MyDatabase
    private lateinit var CourseListRecyclerView : RecyclerView
    private lateinit var bottomNavigationView : BottomNavigationView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_list_of_course, container, false)

        CourseListRecyclerView =view.findViewById(R.id.theory_course_recycler)
        val theory_course_btn_back =view.findViewById<ImageView>(R.id.theory_course_btn_back)
        bottomNavigationView =requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility=View.GONE

        Hawk.init(requireActivity()).build()

        getTheoryCourse()

        theory_course_btn_back.setOnClickListener{
            val fm : FragmentManager =requireActivity().supportFragmentManager
            fm.popBackStack()
        }

        return view
    }


    private fun getTheoryCourse(){

        try {
            db= MyDatabase(requireContext())
            val courseList = db.getTheoryCourse()
            val adapter = TheoryCourseListAdapter(requireContext(), courseList,this)
            CourseListRecyclerView.adapter=adapter
            CourseListRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(requireActivity(), "مشکلی رخ داده است", Toast.LENGTH_SHORT).show()
        }


    }



    private fun loadFragment(fragment: Fragment){

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    override fun onItemClick(id: Int) {
            when(id){
                1 -> loadFragment(ShowCourseFragment(false, id.toString(), arrayListOf("107","201"), null))
                2 -> loadFragment(ShowCourseFragment(false, id.toString(), arrayListOf("60","198"), null))
                3 -> loadFragment(ShowCourseFragment(false, id.toString(), arrayListOf("216","84"), null))
                4 -> loadFragment(ShowCourseFragment(false, id.toString(), arrayListOf("167","245"), null))
            }
    }
}