package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.ShowCourseAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.model.AllTestModel
import ir.rahnama.pistoon.model.CourseListModel
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment
import ir.rahnama.pistoon.ui.saved.SavedListFragment


class ShowCourseFragment(
    private var reciveCourseType: Boolean,
    private var reciveCourseId: String,
    private var QuizIds: ArrayList<String>?,
    private var CoursePage: Int?
) : BaseFragment() , ShowCourseAdapter.OnClickedListener {


    private lateinit var db : MyDatabase
    private lateinit var adapter: ShowCourseAdapter
    private lateinit var recycler: RecyclerView



    private lateinit var indicator_page1:LinearLayout
    private lateinit var indicator_page2:LinearLayout
    private lateinit var indicator_page3:LinearLayout
    private lateinit var indicator_page4:LinearLayout
    private lateinit var indicator_page5:LinearLayout
    private lateinit var indicator_page6:LinearLayout
    private lateinit var indicator_page7:LinearLayout
    private lateinit var indicator_page8:LinearLayout
    private lateinit var parent_indicator_layout:LinearLayout


    private var default_margin = 0
    private var current_margin = 0

    private var first_Answer:Boolean? = null
    private var second_Answer:Boolean? = null

    private lateinit var btn_finish_course : ImageView
    private lateinit var back_btn : ImageView
    private lateinit var loading_finish_page_progressbar : ProgressBar






    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_show_course, container, false)


        btn_finish_course=view.findViewById(R.id.btn_finish_course)
        back_btn=view.findViewById(R.id.main_course_btn_baack)
        loading_finish_page_progressbar=view.findViewById(R.id.loading_finish_page_progressbar)

        //indicator item
         indicator_page1 = view.findViewById(R.id.page1)
         indicator_page2 = view.findViewById(R.id.page2)
         indicator_page3 = view.findViewById(R.id.page3)
         indicator_page4 = view.findViewById(R.id.page4)
         indicator_page5 = view.findViewById(R.id.page5)
         indicator_page6 = view.findViewById(R.id.page6)
         indicator_page7 = view.findViewById(R.id.page7)
         indicator_page8 = view.findViewById(R.id.page8)
        parent_indicator_layout = view.findViewById(R.id.parent_indicator_layout)
        //db to px
        default_margin= (16 * Resources.getSystem().displayMetrics.density).toInt()
        current_margin=(22 * Resources.getSystem().displayMetrics.density).toInt()
        //first page selected
        changeMargin(indicator_page1)
        //page selecyot animation

        recycler=view.findViewById(R.id.showCourseRecycler)
        val botttom_navigation=requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        botttom_navigation.visibility=View.GONE
        db= MyDatabase(requireContext())



        //init show course
        if(reciveCourseType){
            val course = db.getPracticalCourseById(reciveCourseId)
            LastSeenSave(true,course[0].id.toInt())
            val savedPage = db.getSavedCoursePageNumber("2",reciveCourseId)
            showCourse(course , savedPage)
        }else{
            val course = db.getTheoryCourseById(reciveCourseId)
            LastSeenSave(false,course[0].id.toInt())
            val savedPage = db.getSavedCoursePageNumber("1",reciveCourseId)
            showCourse(course , savedPage)
        }



        //init if pagenumber isnt null
        if(CoursePage != null){
            recycler.layoutManager!!.scrollToPosition(CoursePage!!)
            CurrentPositionSet(CoursePage!!)
            recycler.suppressLayout(true)
            parent_indicator_layout.getChildAt(CoursePage!!).background = requireContext().getDrawable(R.drawable.indicator_golden_selector)

        }


        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    CurrentPositionSet(position)
                    setGreenIndicator(position - 1)
                    if (position == 7) {
                        if (CoursePage == null ) {btn_finish_course.visibility = View.VISIBLE}
                        btn_finish_course.setOnClickListener {
                            btn_finish_course.visibility=View.INVISIBLE
                            loading_finish_page_progressbar.visibility=View.VISIBLE
                            loadFinishCourseRating()
                        }
                    }
                }
            }
        })


        back_btn.setOnClickListener{

            if (CoursePage == null){
                popBackStack()
            }else{
                LoadSavedCourseListFragment()
            }

           }



        return view
    }


    private fun loadFinishCourseRating(){
        val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.container,FinishCourseRatingFragment(reciveCourseType,reciveCourseId,first_Answer,second_Answer))
        fragmentManager.remove(this).commit()
    }

    private fun popBackStack(){
        val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
        requireActivity().supportFragmentManager.popBackStack()
        fragmentManager.remove(this).commit()
    }

    private fun showCourse(courselist: MutableList<CourseListModel> , savedPage :MutableList<Int>){
        val quizlist :MutableList<AllTestModel> = if (QuizIds != null ){
            db.getQuizForCourse(QuizIds!![0],QuizIds!![1])
        }else {
            //default values
            db.getQuizForCourse("1","2")
        }
        adapter=ShowCourseAdapter(requireContext(), courselist, quizlist, this , savedPage)
        recycler.adapter=adapter
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recycler)
        recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter.notifyDataSetChanged()

    }

    private fun changeMargin(layout: LinearLayout){
        val params: ViewGroup.LayoutParams? = layout.layoutParams
        params!!.height = current_margin
        params.width = current_margin
        layout.layoutParams = params
    }

    private fun defaultMargin(layout: LinearLayout){
        val params: ViewGroup.LayoutParams? = layout.layoutParams
        params!!.height = default_margin
        params.width = default_margin
        layout.layoutParams = params
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setGreenIndicator(position: Int){
          if (position>=0){

              if (position + 2 == 8 ){
                  parent_indicator_layout.getChildAt(7).background = requireContext().getDrawable(R.drawable.indicator_done_shape)
              }
              when(position){
                  3 -> {
                      when (first_Answer) {
                          false -> parent_indicator_layout.getChildAt(3).background = requireContext().getDrawable(R.drawable.indicator_false_answer)
                          true -> parent_indicator_layout.getChildAt(3).background = requireContext().getDrawable(R.drawable.indicator_done_shape)
                      }
                  }
                  6 -> {
                      when (second_Answer) {
                          false -> parent_indicator_layout.getChildAt(6).background = requireContext().getDrawable(R.drawable.indicator_false_answer)
                          true -> parent_indicator_layout.getChildAt(6).background = requireContext().getDrawable(R.drawable.indicator_done_shape)
                      }
                  }
                  else -> parent_indicator_layout.getChildAt(position).background = requireContext().getDrawable(R.drawable.indicator_done_shape)
              }

          }
    }

    private fun CurrentPositionSet(position: Int){
    val count: Int = parent_indicator_layout.childCount
    for (i in 0 until count) {
        defaultMargin(parent_indicator_layout.getChildAt(i) as LinearLayout)
    }
      changeMargin(parent_indicator_layout.getChildAt(position) as LinearLayout)


    }

    override fun firstAnswer(state: Boolean) {
      first_Answer=state
    }

    override fun secondAnswer(state: Boolean) {
        second_Answer=state
    }

    override fun bookmarkPage(courseId: String, coursePage: String) {
        when(reciveCourseType){
            false ->  db.SavedCoursePages("1",courseId,coursePage)
            true ->   db.SavedCoursePages("2",courseId,coursePage)
        }
    }

    override fun DeletebookmarkPage(courseId: String, coursePage: String) {
        //1 = theory 2 = practical
        when(reciveCourseType){
            false -> db.DeleteSavedCoursePages("1",courseId,coursePage)
            true -> db.DeleteSavedCoursePages("2",courseId,coursePage)
        }

    }




    private fun LastSeenSave(coursType:Boolean , id : Int){
        val sharedpref =requireActivity().getSharedPreferences("lastSeen", Context.MODE_PRIVATE)
        val editor = sharedpref.edit()
        editor.putBoolean("courseType", coursType)
        editor.putInt("id", id)
        editor.apply()
    }


    private fun LoadSavedCourseListFragment(){
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, SavedListFragment()).remove(this).commit()

    }

    override fun onBackPressed(): Boolean {

        if(CoursePage != null){
            LoadSavedCourseListFragment()
        }else{
            popBackStack()
        }

        return true
    }



}