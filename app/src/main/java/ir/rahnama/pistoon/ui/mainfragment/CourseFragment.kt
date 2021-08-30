package ir.rahnama.pistoon.ui.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.ui.BoardCourseList
import ir.rahnama.pistoon.ui.ShowListOfPracticalCourseFragment
import ir.rahnama.pistoon.ui.ShowListOfTheoryCourseFragment


class CourseFragment : BaseFragment()  {






    private lateinit var bottomNavigationView : BottomNavigationView



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_course, container, false)


        val theory_btn = view.findViewById<CardView>(R.id.theory_course_cardview)
        val practical_btn = view.findViewById<CardView>(R.id.practical_course_cardview)
        val board_btn = view.findViewById<CardView>(R.id.board_cardview)
         bottomNavigationView =requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility=View.VISIBLE



        theory_btn.setOnClickListener{
                loadFragment(ShowListOfTheoryCourseFragment())
        }
        practical_btn.setOnClickListener{
                loadFragment(ShowListOfPracticalCourseFragment())
        }
        board_btn.setOnClickListener{
           loadFragment(BoardCourseList())
        }

        return view
    }



    private fun loadFragment(fragment: Fragment){

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }


    override fun onBackPressed(): Boolean {
        bottomNavigationView.selectedItemId=R.id.home_nav
        return true
    }



}




