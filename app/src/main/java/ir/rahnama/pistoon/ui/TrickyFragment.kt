package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.TrickyTestAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.model.AllTestModel
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment
import ir.rahnama.pistoon.ui.mainfragment.HomeFragment


class TrickyFragment : BaseFragment() , TrickyTestAdapter.RecyclerViewActionListener {

    private lateinit var db : MyDatabase
    private lateinit var trickyRecycler : RecyclerView
    private lateinit var adapter : TrickyTestAdapter
    private lateinit var tricklist : MutableList<AllTestModel>
    private lateinit var bottomNavigation : BottomNavigationView
    private lateinit var def_txt:TextView
    private lateinit var delete_layout : ConstraintLayout
    private lateinit var parent_layout_tricky : ConstraintLayout
    private lateinit var select_all_btn1 : ImageView
    private lateinit var select_all_btn2 : ImageView
    private lateinit var delete_btn : ImageView
    private lateinit var close_btn : ImageView
    private lateinit var count_item_select : TextView
    private var checkAll : Boolean = false
     private var allItemSelectedItem :MutableList<Int>?=null


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tricky, container, false)

        trickyRecycler =view.findViewById(R.id.tricky_recycler)
        val tricky_btn_back=view.findViewById<ImageView>(R.id.tricky_btn_back)
         def_txt=view.findViewById(R.id.def_txt)
        delete_layout=view.findViewById(R.id.delete_option_layout)
        parent_layout_tricky=view.findViewById(R.id.parent_layout_tricky)
        delete_btn=view.findViewById(R.id.img_delete)
        close_btn=view.findViewById(R.id.img_close)
        count_item_select=view.findViewById(R.id.item_selec_count_txt)
         bottomNavigation=requireActivity().findViewById(R.id.bottom_navigation)
        select_all_btn1=view.findViewById(R.id.img_selectAll1)
        select_all_btn2=view.findViewById(R.id.img_selectAll2)
        bottomNavigation.visibility=View.GONE
        db= MyDatabase(requireContext())




        loadTricky()




        tricky_btn_back.setOnClickListener{loadFragment(HomeFragment())}





        select_all_btn1.setOnClickListener{

                adapter.selectAll()
                select_all_btn1.visibility=View.INVISIBLE
                select_all_btn2.visibility=View.VISIBLE
                checkAll=true

        }

        select_all_btn2.setOnClickListener{
            adapter.unselectall()
            select_all_btn2.visibility=View.INVISIBLE
            select_all_btn1.visibility=View.VISIBLE
            checkAll=false
        }


        close_btn.setOnClickListener{
            select_all_btn2.visibility=View.INVISIBLE
            select_all_btn1.visibility=View.VISIBLE
            checkAll=false
            closeLayoutAnimation()
        }



        delete_btn.setOnClickListener{

            if (checkAll) {
                try {
                    db.deleteAllTricky()
                    closeLayoutAnimation()
                    loadTricky()
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
            if (allItemSelectedItem!!.size>0 && !checkAll){
                try {
                    for (i in allItemSelectedItem!!) db.deleteTrickyById(i.toString())
                    closeLayoutAnimation()
                    loadTricky()
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }



        }


        return view
    }









    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.remove(this)
        fragmentTransaction.commit()
    }

    @SuppressLint("RtlHardcoded")
    override fun OnLongClick(itemSelected: MutableList<Int>, itemSelectedCount: Int) {

        if (itemSelected.size>=0){
            this.allItemSelectedItem=itemSelected
            count_item_select.text=itemSelectedCount.toString()
            val transition: Transition = Slide(Gravity.LEFT)
            transition.addTarget(R.id.delete_option_layout)
            transition.duration = 500
            TransitionManager.beginDelayedTransition(parent_layout_tricky, transition)
            delete_layout.visibility = View.VISIBLE

        }


    }

    private fun loadTricky(){
        tricklist=db.getTrickyQuiz()
        if (tricklist.size==0) {
            trickyRecycler.visibility=View.INVISIBLE
            def_txt.visibility=View.VISIBLE
        }else{
            def_txt.visibility=View.INVISIBLE
            adapter= TrickyTestAdapter(requireActivity(), tricklist, this)
            trickyRecycler.adapter=adapter
            trickyRecycler.layoutManager= LinearLayoutManager(requireActivity())
            adapter.notifyDataSetChanged()
        }
    }


    private fun closeLayoutAnimation(){
        val transition: Transition = Slide(Gravity.END)
        transition.addTarget(R.id.delete_option_layout)
        transition.duration = 500
        TransitionManager.beginDelayedTransition(parent_layout_tricky, transition)
        delete_layout.visibility = View.GONE
        adapter.clearPrevData()
    }


    override fun onBackPressed(): Boolean {
        loadFragment(HomeFragment())
        return true
    }

}