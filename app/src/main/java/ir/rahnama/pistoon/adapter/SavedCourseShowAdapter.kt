package ir.rahnama.pistoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.SavedCourseModel

class SavedCourseShowAdapter(var context: Context,private var courselist:MutableList<SavedCourseModel> , private var listener : SavedCourseListener)
    : RecyclerView.Adapter<SavedCourseShowAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCourseShowAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.saved_course_item_model, parent, false))
    }

    override fun onBindViewHolder(holder: SavedCourseShowAdapter.MyViewHolder, position: Int) {

        holder.image.setImageBitmap(courselist[position].image)
        holder.title.text=courselist[position].title
        holder.body.text = courselist[position].body




        holder.delete_page.setOnClickListener{
            listener.deletebookmarkPage(courselist[position].course_type,courselist[position].id.toString(),courselist[position].pageNum.toString())
        }

        holder.load_saved_page.setOnClickListener{

         when(courselist[position].course_type){

             "1" -> listener.openSavedCoursePage(false,courselist[position].id.toString(),courselist[position].pageNum.toInt())
             "2" -> listener.openSavedCoursePage(true,courselist[position].id.toString(),courselist[position].pageNum.toInt())

         }


        }

    }

    override fun getItemCount(): Int {
        return courselist.size
    }


    inner class MyViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){

        var image : ImageView = itemview.findViewById(R.id.saved_image)
        var title : TextView = itemview.findViewById(R.id.saved_title)
        var body : TextView = itemview.findViewById(R.id.saved_body_text)
        var delete_page : ImageView = itemview.findViewById(R.id.delete_saved_course_page)
        var load_saved_page : CardView = itemview.findViewById(R.id.load_saved_page)

    }


    interface SavedCourseListener{
        fun deletebookmarkPage(courseType:String,courseId: String, coursePage: String)
        fun openSavedCoursePage(courseType:Boolean,CourseId:String,CoursePage:Int)
    }
}