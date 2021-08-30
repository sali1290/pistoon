package ir.rahnama.pistoon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.GoldenTestsAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment

class GoldenTestFragment : BaseFragment()  {


    private lateinit var db : MyDatabase
    private lateinit var goldenTestRecyclerView :RecyclerView
    private lateinit var framlayout : FrameLayout
    private lateinit var title : TextView
    private var backMode : Boolean = false
    private lateinit var bottomnavigation :BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_golden_test, container, false)



        db= MyDatabase(requireActivity())
        goldenTestRecyclerView =view.findViewById(R.id.golden_recycler)
         framlayout = view.findViewById(R.id.framlayout)
         title = view.findViewById(R.id.txt_title)
        bottomnavigation=requireActivity().findViewById(R.id.bottom_navigation)
        bottomnavigation.visibility=View.GONE


        val btn_back = view.findViewById<ImageView>(R.id.btn_back)
        val btn_part_1 = view.findViewById<ConstraintLayout>(R.id.layout_part_one)
        val btn_part_2 = view.findViewById<ConstraintLayout>(R.id.layout_part_two)
        val btn_part_3 = view.findViewById<ConstraintLayout>(R.id.layout_part_three)
        val btn_part_4 = view.findViewById<ConstraintLayout>(R.id.layout_part_four)
        val btn_part_5 = view.findViewById<ConstraintLayout>(R.id.layout_part_five)
        val btn_part_6 = view.findViewById<ConstraintLayout>(R.id.layout_part_six)


        btn_part_1.setOnClickListener{
            loadGoldenTest("1","بخش یکم")
            backMode=true
        }
        btn_part_2.setOnClickListener{
            loadGoldenTest("2","بخش دوم")
            backMode=true
        }
        btn_part_3.setOnClickListener{
            loadGoldenTest("3","بخش سوم")
            backMode=true
        }
        btn_part_4.setOnClickListener{
            loadGoldenTest("4","بخش چهارم")
            backMode=true
        }
        btn_part_5.setOnClickListener{
            loadGoldenTest("5","بخش پنجم")
            backMode=true
        }
        btn_part_6.setOnClickListener{
            loadGoldenTest("6","بخش ششم")
            backMode=true
        }



        btn_back.setOnClickListener{

            if(backMode) {
                framlayout.visibility=View.INVISIBLE
                title.text="سوالات طلایی"
                backMode=false
            }else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }


        return view
    }


    private fun getGoldenTest(part : String) {
        try {
            val goldenTest = db.getGoldenTest(part)
            val adapter = GoldenTestsAdapter(requireActivity(), goldenTest)
            goldenTestRecyclerView.adapter=adapter
            goldenTestRecyclerView.layoutManager= LinearLayoutManager(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(requireActivity(), resources.getString(R.string.CantAccessToNet), Toast.LENGTH_SHORT).show()
        }


    }


    private fun loadGoldenTest(part : String , titleName :String ){
        framlayout.visibility = View.VISIBLE
        title.text=titleName
        getGoldenTest(part)



    }

    override fun onBackPressed(): Boolean {
        if(backMode) {
            framlayout.visibility=View.INVISIBLE
            title.text=resources.getString(R.string.GoldenTestTitle_txt)
            backMode=false
        }else {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return true
    }




}