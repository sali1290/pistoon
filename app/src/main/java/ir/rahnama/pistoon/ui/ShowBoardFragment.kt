package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView.VISIBLE
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.BoardAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.model.BoardModel


class ShowBoardFragment : Fragment(), BoardAdapter.RecyclerViewActionListener {



    private lateinit var db : MyDatabase
    private lateinit var BoardRecyclerView : RecyclerView
    private var showModel : Boolean = true
    private lateinit var spinnerCardview : CardView
    private lateinit var spinner : Spinner
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var adapter :BoardAdapter
    private var boardList : MutableList<BoardModel>? = null


    private lateinit var bottom_sheet_img : ImageView
    private lateinit var bottom_sheet_title : TextView

    private lateinit var bottom_sheet_first_layout : ConstraintLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomsheet_cons: ConstraintLayout






    @SuppressLint("NewApi", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_show_board, container, false)


        BoardRecyclerView =view.findViewById(R.id.board_recycler)
        spinnerCardview =view.findViewById(R.id.spinnerCardView)
        spinner =view.findViewById(R.id.spinner)
        bottomNavigationView =requireActivity().findViewById(R.id.bottom_navigation)
        val searchview = view.findViewById<SearchView>(R.id.searchView)
        val imageview_back = view.findViewById<ImageView>(R.id.imageviewBack)
        bottomsheet_cons=view.findViewById(R.id.bottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet_cons)
        bottom_sheet_first_layout=view.findViewById(R.id.firest_layout)
        bottom_sheet_img = view.findViewById(R.id.boardImagesView)
        bottom_sheet_title = view.findViewById(R.id.Bottom_sheet_board_title)
        val btn_board_state=view.findViewById<ToggleButton>(R.id.btn_test_model)
        val toggle_state_txt=view.findViewById<TextView>(R.id.toggle_state_txt)

        bottomNavigationView.visibility=View.GONE
//mesle sharedpref key value ba encrypt
        Hawk.init(requireActivity()).build()



        btn_board_state.setOnClickListener{
           if (Hawk.get("fullAccount",0) == 20){
               if (btn_board_state.isChecked) {
                   showModel=false
                   adapter = BoardAdapter(requireActivity(), boardList!!, showModel,this)
                   BoardRecyclerView.adapter=adapter
                   BoardRecyclerView.layoutManager= GridLayoutManager(requireActivity(), 3)
                   adapter.notifyDataSetChanged()
                   toggle_state_txt.text=getString(R.string.toggle_state1)
               }else{
                   showModel=true
                   adapter = BoardAdapter(requireActivity(), boardList!!, showModel,this)
                   BoardRecyclerView.adapter=adapter
                   BoardRecyclerView.layoutManager= GridLayoutManager(requireActivity(), 3)
                   adapter.notifyDataSetChanged()
                   toggle_state_txt.text=getString(R.string.toggle_state2)
               }
           }else{
               alertDialog(resources.getString(R.string.AccessToBoardAlert))
           }
        }


        setDropDownMenuItem()



        BoardRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                bottomSheetBehavior.state = STATE_COLLAPSED
            }

        })



        bottomSheetBehavior.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
               if (bottomSheetBehavior.state==STATE_COLLAPSED){


                   if (Hawk.get("fullAccount",0) == 20){
                       bottom_sheet_first_layout.visibility= VISIBLE
                   }else{
                       alertDialog("برای دسترسی کامل به حالت آموزشی لطفا اشتراک جامع را خریداری کنیذ")
                   }


               }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }


        })








        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View,
                position: Int, id: Long
            ) {
                val value: Int = spinner.selectedItemPosition

                if (value==0){
                    getAllBoard()
                    bottomSheetBehavior.state = STATE_COLLAPSED
                } else {
                      getBoardItem(value.toString())
                        bottomSheetBehavior.state= STATE_COLLAPSED
                    }


            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
            }
        }





        searchview.setOnSearchClickListener {
            spinnerCardview.visibility=View.GONE
        }

        searchview.setOnCloseListener {
            spinnerCardview.visibility = View.VISIBLE
            false
        }






        imageview_back.setOnClickListener{
            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()

        }



        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
               return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
              SearchInBoard(p0!!)
                return true
            }

        })




        return view
    }




    private fun getBoardItem(type: String){

        boardList!!.clear()
        db= MyDatabase(requireContext())
        boardList = db.getBoardByType(type)
        adapter = BoardAdapter(requireActivity(), boardList!!, showModel,this)
        BoardRecyclerView.adapter=adapter
        BoardRecyclerView.layoutManager= GridLayoutManager(requireActivity(), 3)
        adapter.notifyDataSetChanged()



    }


    private fun getAllBoard(){

        if (boardList!=null){
            boardList!!.clear()
        }
        db= MyDatabase(requireContext())
        boardList = db.getAllBoard()
        adapter = BoardAdapter(requireActivity(), boardList!!,showModel,this)
        BoardRecyclerView.adapter=adapter
        BoardRecyclerView.layoutManager= GridLayoutManager(requireActivity(), 3)
        adapter.notifyDataSetChanged()



    }





    private fun setDropDownMenuItem(){
        val items = arrayOf(
            "همه تابلوها",
            "تابلوهای اخباری",
            "تابلوهای اخطاری",
            "تابلوهای انتظامی",
            "برچسب ها",
            "تابلوهای مکمل",
            "تابلوهای راهنمای مسیر",
            "تابلوهای محلی"
        )
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        spinner.adapter = adapter

    }

    override fun onViewClicked(clickedItemPosition: Int) {

       if (Hawk.get("fullAccount",0) == 20){
           bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
           bottom_sheet_first_layout.visibility= VISIBLE
           bottom_sheet_img.setImageBitmap(boardList!![clickedItemPosition].image)
           bottom_sheet_title.text=(boardList!![clickedItemPosition].title)
       }else{
           alertDialog("برای دسترسی کامل به حالت آموزشی لطفا اشتراک جامع را خریداری کنیذ")
       }

    }



    private fun SearchInBoard(value : String){

        db= MyDatabase(requireContext())
       boardList =  db.SearchInBoard(value)
        adapter = BoardAdapter(requireActivity(), boardList!!, showModel,this)
        BoardRecyclerView.adapter=adapter
        BoardRecyclerView.layoutManager= GridLayoutManager(requireActivity(), 3)
        adapter.notifyDataSetChanged()

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

}



