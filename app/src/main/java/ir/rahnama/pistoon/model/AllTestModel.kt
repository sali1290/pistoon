package ir.rahnama.pistoon.model

import android.graphics.Bitmap

data class AllTestModel(val id : Long , val title : String , val answer1 : String , val answer2 : String , val answer3 : String , val answer4 : String , val true_answer : Long , val image : Bitmap?=null)