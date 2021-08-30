package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.apiModel.BestRecordModel

class BestRecordAdapter (var context: Context ,private var recordsList:MutableList<BestRecordModel>)
    : RecyclerView.Adapter<BestRecordAdapter.MyViewHolder>() {










    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.best_record_item_model, parent, false))
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        when(holder.adapterPosition){

            in 0 .. 2 -> {
                holder.medal.setImageResource(R.drawable.ic_golden_medal)
                holder.record_sate_color_layout.background=context.resources.getDrawable(R.color.golden)
            }

            in 3 .. 10 -> {
                holder.medal.setImageResource(R.drawable.ic_silver_medal)
                holder.record_sate_color_layout.background=context.resources.getDrawable(R.color.gray)
            }

            else -> {
                holder.medal.setImageResource(R.drawable.ic_boronz_medal)
                holder.record_sate_color_layout.background=context.resources.getDrawable(R.color.boronz)
            }



        }


        holder.record.text=recordsList[position].player_record.toString()
        holder.txt_position_number.text=(position+1).toString()
        holder.player_name_txt.text=recordsList[position].player_name

    }

    override fun getItemCount(): Int {
        return recordsList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        val medal = itemView.findViewById<ImageView>(R.id.best_record_medal)
        val record = itemView.findViewById<TextView>(R.id.txt_game_record_counter)
        val txt_position_number = itemView.findViewById<TextView>(R.id.txt_position_number)
        val player_name_txt = itemView.findViewById<TextView>(R.id.player_name_txt)
        val record_sate_color_layout = itemView.findViewById<LinearLayout>(R.id.record_sate_color_layout)


    }





}











