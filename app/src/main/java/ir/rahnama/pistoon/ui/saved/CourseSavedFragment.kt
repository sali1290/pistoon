package ir.rahnama.pistoon.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.SavedCourseShowAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.ui.ShowCourseFragment

class CourseSavedFragment : Fragment() , SavedCourseShowAdapter.SavedCourseListener {


    private lateinit var recycler_view:RecyclerView
    private lateinit var txt_empty:TextView
    private lateinit var db:MyDatabase
    private lateinit var savePageAdapter:SavedCourseShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_saved, container, false)

         recycler_view = view.findViewById(R.id.saved_coursed_recycler)
         txt_empty = view.findViewById(R.id.saved_course_txt_empty)


        loadData()


        return view
    }




    private fun loadData(){

         db = MyDatabase(requireContext())
        val data = db.loadSavedCources()
         savePageAdapter = SavedCourseShowAdapter(requireActivity() , data , this)
        recycler_view.adapter= savePageAdapter
        recycler_view.layoutManager= LinearLayoutManager(requireActivity())

        if (data.size > 0){
            txt_empty.visibility=View.GONE
            recycler_view.visibility=View.VISIBLE
        }else{
            txt_empty.visibility=View.VISIBLE
            recycler_view.visibility=View.GONE
        }
    }

    override fun deletebookmarkPage(courseType: String, courseId: String, coursePage: String) {
        db.DeleteSavedCoursePages(courseType,courseId,coursePage)
        loadData()
    }

    override fun openSavedCoursePage(courseType: Boolean, CourseId: String, CoursePage: Int) {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, ShowCourseFragment(
                courseType,
                CourseId,
               null,
                CoursePage
            )
            ).remove(this).commit()

    }

}