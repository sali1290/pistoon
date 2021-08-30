package ir.rahnama.pistoon.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.SavedViewPagerAdapter


class SavedListFragment : Fragment() {

    private lateinit var viewpager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_list, container, false)

         viewpager=view.findViewById(R.id.saved_viewpager)
        val tabDots=view.findViewById<TabLayout>(R.id.tabDots)
        val saved_btn_back=view.findViewById<ImageView>(R.id.saved_btn_back)
        val bottom_navigation=requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_navigation.visibility=View.GONE


        val fragments = arrayListOf(CourseSavedFragment(), QuestionSavedFragment())
        val adapter = SavedViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle,fragments)
        viewpager.adapter=adapter
        TabLayoutMediator(tabDots, viewpager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "آموزش"
                }
                1 -> {
                    tab.text = "آزمون"
                }
            }
        }.attach()




        saved_btn_back.setOnClickListener{
            val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
            requireActivity().supportFragmentManager.popBackStack()
            fragmentManager.remove(this).commit()
        }


        return view
    }





}