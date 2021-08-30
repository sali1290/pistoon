package ir.rahnama.pistoon.model

import android.graphics.Bitmap

data class SavedCourseModel(val id :Long,val course_type :String , val title:String ,val body:String , val pageNum : Long , val image : Bitmap)

