package ir.rahnama.pistoon.model

import android.graphics.Bitmap

data class  CourseListModel(val id : Long ,val type : Long, val image : Bitmap ,val title : String ,val page1 : String , val page2 :String,
                           val page3 :String ,val page4:String,val page5:String,val page6:String,
                           val course_is_done : Long , val quiz_is_done : Long) {



    /* constructor(title: String,page1: String,page2: String,page3: String,page4: String,page5: String,page6: String,course_is_done: Long,quiz_is_done: Long)
             :this(0,title, page1, page2, page3, page4, page5, page6, course_is_done, quiz_is_done)*/

}