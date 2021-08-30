package ir.rahnama.pistoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.GuideLineModel

class GuideLineAdapter(var context: Context , private var guideList : List<GuideLineModel>) :RecyclerView.Adapter<GuideLineAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideLineAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.guidline_item_model, parent, false))
    }

    override fun onBindViewHolder(holder: GuideLineAdapter.MyViewHolder, position: Int) {
       holder.guideTitle.text=guideList[position].title
        holder.guideText.text=guideList[position].text
        holder.guideImage.setImageBitmap(guideList[position].image)
    }

    override fun getItemCount(): Int {
       return guideList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var guideTitle = itemView.findViewById<TextView>(R.id.guidline_title_txt)
            var guideText = itemView.findViewById<TextView>(R.id.guidline_body_txt)
            var guideImage = itemView.findViewById<ImageView>(R.id.guidline_imageview)

    }



}