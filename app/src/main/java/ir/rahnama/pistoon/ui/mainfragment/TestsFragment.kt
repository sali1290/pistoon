package ir.rahnama.pistoon.ui.mainfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.ui.ListOfTestFragment


class TestsFragment : BaseFragment(){


    lateinit var bottom_navigation:BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tests, container, false)

        val base_test_cardview =view.findViewById<CardView>(R.id.base_test_cardview)
        val advanced_test_cardview =view.findViewById<CardView>(R.id.advanced_test_cardview)
        bottom_navigation=requireActivity().findViewById(R.id.bottom_navigation)
        bottom_navigation.visibility=View.VISIBLE


        base_test_cardview.setOnClickListener{loadFragment(ListOfTestFragment(false))}
        advanced_test_cardview.setOnClickListener{loadFragment(ListOfTestFragment(true))}


        return view
    }


    private fun loadFragment(fragment : Fragment){
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun onBackPressed(): Boolean {
        bottom_navigation.selectedItemId=R.id.home_nav
        return true
    }

}
