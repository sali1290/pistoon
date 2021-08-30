package ir.rahnama.pistoon.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.ListOfTestAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.model.AllTestModel
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment


class ListOfTestFragment(private var testType:Boolean) : BaseFragment() , ListOfTestAdapter.MoveTestID {


    private lateinit var recyclerView : RecyclerView
    private lateinit var db : MyDatabase
    private lateinit var adapter: ListOfTestAdapter
    private lateinit var bottom_nav: BottomNavigationView
    private var FRAGMENT_NAME_IN_BACK_STACK="test_list"

    private var isShowLockIc= true



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list_of_test, container, false)

         bottom_nav=requireActivity().findViewById(R.id.bottom_navigation)
        bottom_nav.visibility=View.GONE
        recyclerView=view.findViewById(R.id.list_test_recycler)
        val Exam_btn_back=view.findViewById<ImageView>(R.id.Exam_btn_back)
        db= MyDatabase(requireContext())
        recyclerView.layoutManager= GridLayoutManager(requireActivity(), 2)

        Hawk.init(requireActivity()).build()

        if (Hawk.get("fullAccount",0) == 20){
            isShowLockIc=false
        }

        if (testType){
            val testList =db.getExamList()
            adapter= ListOfTestAdapter(requireActivity(),testList,this,isShowLockIc)
            recyclerView.adapter=adapter
        }else{
            val testList =db.getquizList()
            adapter= ListOfTestAdapter(requireActivity(),testList,this,false)
            recyclerView.adapter=adapter
        }




        Exam_btn_back.setOnClickListener{requireActivity().supportFragmentManager.popBackStack()}


        return view
    }

    override fun getID(testPosition: Int) {

        if(testType){

            if (!isShowLockIc){
                fragmentInit(db.getExamTests(testPosition),true , testPosition)
            }else{
                alertDialog(resources.getString(R.string.AccessToExamAlert))
            }
        }else {
            fragmentInit(db.getQuizTests(testPosition) , false,testPosition)
        }

    }


    private fun fragmentInit (data : MutableList<AllTestModel>, type:Boolean, testId:Int) {
        val testFrahment = ShowTestFragment( data , type , testId , false)
        val fragmentManager  : FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(FRAGMENT_NAME_IN_BACK_STACK)
        fragmentTransaction.replace(R.id.container,testFrahment).commit()

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
            bottom_nav.selectedItemId=R.id.more_nav
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
        }


        builder.setView(dialogLayout)
        builder.show()
    }


    override fun onBackPressed(): Boolean {
        requireActivity().supportFragmentManager.popBackStack()
        return true
    }

}