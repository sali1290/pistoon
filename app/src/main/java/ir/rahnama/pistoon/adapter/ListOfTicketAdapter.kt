package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.apiModel.TicketListModel


class ListOfTicketAdapter(
    var context: Context,
    private var listener: OpenTicket
) :
    RecyclerView.Adapter<ListOfTicketAdapter.MyViewHolder>() {

    private var ticketList :MutableList<TicketListModel> = arrayListOf()

    fun setItems(NewticketList: MutableList<TicketListModel>) {
        ticketList = NewticketList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater.inflate(R.layout.list_ticket_item_model, parent, false))
    }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.ticket_list_title.text=ticketList[position].ticket_title

        if (ticketList[position].ticket_state==1){
            holder.ticket_state.text=context.getString(R.string.inactive_ticket)
            holder.ticket_state.setTextColor(context.resources.getColor(R.color.trikyRed))
            holder.ticket_state_layout.background=context.resources.getDrawable(R.color.trikyRed)
        }

        if(ticketList[position].message_notify==1){
            holder.txt_new_message.visibility=View.VISIBLE
        }

        holder.ticket_list_cardview.setOnClickListener{
            listener.openTicket(ticketList[position].id, ticketList[position].ticket_state)
        }

    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    interface OpenTicket {
        fun openTicket(id: Int, State: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ticket_list_title: TextView = itemView.findViewById(R.id.ticket_list_title)
        var ticket_state: TextView = itemView.findViewById(R.id.ticket_state)
        var ticket_state_layout: LinearLayout = itemView.findViewById(R.id.ticket_state_layout)
        var ticket_list_cardview: CardView = itemView.findViewById(R.id.ticket_list_cardview)
        var txt_new_message: TextView = itemView.findViewById(R.id.txt_new_message)

    }


}
