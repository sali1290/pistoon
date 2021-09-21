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
import ir.rahnama.pistoon.model.CourseListModel


class PracticalCourseListAdapter(private var context: Context,
                                 private var courseList: List<CourseListModel>,
                                 private var listener : ItemClickListener) : RecyclerView.Adapter<PracticalCourseListAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.course_item_model, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.courceTitleTextView.text = courseList[position].title


        holder.courceBanner.setImageBitmap(courseList[position].image)

        val isTrue:Long = 1

        if (courseList[position].course_is_done == isTrue) {
            holder.courceIsDone.setTextColor(context.resources.getColor(R.color.isDoneGreen))
        }
        if (courseList[position].quiz_is_done==isTrue) {
            holder.quizIsDone.setTextColor(context.resources.getColor(R.color.isDoneGreen))
            holder.courceMedalCheck.setImageResource(R.drawable.ic_golden_medal)

        }


       /* if (!fullAccount){
            when(position){
                4 -> holder.test_list_lock_ic.visibility=View.VISIBLE
                5 -> holder.test_list_lock_ic.visibility=View.VISIBLE
                6 -> holder.test_list_lock_ic.visibility=View.VISIBLE
                7 -> holder.test_list_lock_ic.visibility=View.VISIBLE

            }
        }
*/



        holder.list_item_cardview.setOnClickListener{
            listener.onItemClick(courseList[position].id.toInt())
        }

    }

    override fun getItemCount(): Int {
        return courseList.size
        //be sharti een return dorost hast ke eenja toole titlee ha ba content haye har page yeki bashe !!!
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var courceBanner: ImageView =  itemView.findViewById(R.id.courceBannerImageView)
        var courceTitleTextView: TextView = itemView.findViewById(R.id.courceTitleTextView)
        var courceMedalCheck: ImageView =itemView.findViewById(R.id.medalImagesView)
        var quizIsDone: TextView =  itemView.findViewById(R.id.queizIsDoneText)
        var courceIsDone: TextView =  itemView.findViewById(R.id.courceIsDoneText)
        var list_item_cardview : CardView = itemView.findViewById(R.id.list_item_cardview)
       /* var test_list_lock_ic : ImageView = itemView.findViewById(R.id.test_list_lock_ic)*/




    }



    interface ItemClickListener{
        fun onItemClick(id:Int)
    }

}


