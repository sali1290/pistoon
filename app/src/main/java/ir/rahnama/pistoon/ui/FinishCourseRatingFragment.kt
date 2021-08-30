package ir.rahnama.pistoon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment


class FinishCourseRatingFragment(private var CourseType:Boolean, private var courseId:String , private var firstAnswer:Boolean?,private var secondAnswer:Boolean?) : BaseFragment() {


    //finish course item
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var txt_title : TextView
    private lateinit var txt_body : TextView
    private lateinit var backToCourse_btn : Button

    lateinit var db:MyDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_finish_course_rating_f_ragment, container, false)

        star1=view.findViewById(R.id.img_1star)
        star2=view.findViewById(R.id.img_2star)
        star3=view.findViewById(R.id.img_3star)
        txt_title=view.findViewById(R.id.finish_course_title)
        txt_body=view.findViewById(R.id.finish_course_body)
        backToCourse_btn=view.findViewById(R.id.btn_back_to_course)

        //init data base
        db= MyDatabase(requireContext())


        setUpFinishCourseLayout()


        backToCourse_btn.setOnClickListener{popBackStack()}


        return view
    }


    private fun popBackStack(){
        val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
        requireActivity().supportFragmentManager.popBackStack()
        fragmentManager.remove(this).commit()
    }

    private fun setUpFinishCourseLayout(){

        star1.visibility = View.VISIBLE
        when(CourseType){
            true -> db.setPracticalIsDone(courseId, "0", "1")
            false -> db.setTheoryIsDone(courseId, "0", "1")
        }

        if (firstAnswer == true && secondAnswer == true){
            star2.visibility = View.VISIBLE
            star3.visibility = View.VISIBLE
            txt_title.text= getString(R.string.finish_course_title3)
            txt_body.text=getString(R.string.finish_course_body3)
            when(CourseType){
                true -> db.setPracticalIsDone(courseId, "1", "1")
                false -> db.setTheoryIsDone(courseId, "1", "1") }
        }
        else if (firstAnswer == true || secondAnswer == true) {
            star2.visibility = View.VISIBLE
            txt_title.text= getString(R.string.finish_course_title2)
            txt_body.text=getString(R.string.finish_course_body2)
        }
    }



    override fun onBackPressed(): Boolean {
        popBackStack()
        return true
    }



}