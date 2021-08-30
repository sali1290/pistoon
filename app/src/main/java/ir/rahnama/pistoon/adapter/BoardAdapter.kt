package ir.rahnama.pistoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.BoardModel


class BoardAdapter(var context: Context,private var boradList: List<BoardModel>, private var showModel : Boolean, private var Listener : RecyclerViewActionListener ) : RecyclerView.Adapter<BoardAdapter.MyViewHolder>() {








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.board_item_model, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (showModel) holder.boardsTitleTextView.text = boradList[position].title
        holder.boardsImageVIew.setImageBitmap(boradList[position].image)


        holder.boardsImageVIew.setOnClickListener{
            Listener.onViewClicked(holder.adapterPosition)
        }


    }

    override fun getItemCount(): Int {
        return boradList.size

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var boardsTitleTextView: TextView = itemView.findViewById(R.id.boardsTitleTextView)
        var boardsImageVIew: ImageView = itemView.findViewById(R.id.boardImagesView)

    }
    
    interface RecyclerViewActionListener {
        fun onViewClicked(clickedItemPosition: Int)
    }


}