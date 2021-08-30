package ir.rahnama.pistoon.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.BoardModel

class GameAdapter (
        var context: Context ,
        private var boardlist:MutableList<BoardModel> ,
        private var listener : SendDataToFragment
):RecyclerView.Adapter<GameAdapter.MyViewHolder>() {




    fun updateData(){
       boardlist.shuffle()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.game_item_model, parent, false))
    }
    override fun onBindViewHolder(holder: GameAdapter.MyViewHolder, position: Int) {
       holder.image1.setImageBitmap(boardlist[0].image)
       holder.image2.setImageBitmap(boardlist[1].image)
       holder.image3.setImageBitmap(boardlist[2].image)
        val pos = arrayOf(0,1,2)
        pos.shuffle()
        listener.sendData(boardlist[pos[0]].title)
        when (boardlist[pos[0]].id) {
            boardlist[0].id -> { listener.SendPostion(1) }
            boardlist[1].id -> { listener.SendPostion(2) }
            boardlist[2].id -> { listener.SendPostion(3) }
        }
    }

    override fun getItemCount(): Int {
       return 1
    }
    inner class MyViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        var image1: ImageView = itemView.findViewById(R.id.game_iamge1)
        var image2: ImageView = itemView.findViewById(R.id.game_iamge2)
        var image3: ImageView = itemView.findViewById(R.id.game_iamge3)
    }

    interface SendDataToFragment{
        fun sendData(name:String)
        fun SendPostion(position:Int)
    }
}