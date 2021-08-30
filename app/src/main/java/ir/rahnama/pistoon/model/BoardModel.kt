package ir.rahnama.pistoon.model

import android.graphics.Bitmap


data class BoardModel(val id : Long , val type: String , val title : String , val image : Bitmap){


    constructor(type: String,title: String,image: Bitmap) : this(0, type, title, image)


}
