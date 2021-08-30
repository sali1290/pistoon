package ir.rahnama.pistoon.model

import android.graphics.Bitmap

data class GuideLineModel(val id : Long , val title : String , val text : String , val image : Bitmap){



    constructor(title: String,text: String,image: Bitmap) : this(0, title, text, image)


}
