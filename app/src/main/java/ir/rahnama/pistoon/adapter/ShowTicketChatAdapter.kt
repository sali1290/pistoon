package ir.rahnama.pistoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.apiModel.TicketMessageModel

class ShowTicketChatAdapter(var context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val USER_MESSAGE_LAYOUT = 0
    private val SUPPORT_MESSAGE_LAYOUT = 1

    private var Message : MutableList<TicketMessageModel> = arrayListOf()

    fun ShowMessage(MessageList:MutableList<TicketMessageModel>){
        this.Message=MessageList
        notifyDataSetChanged()
    }

    fun AddNewMessage(item:TicketMessageModel){
        Message.add(item)
        notifyItemInserted(Message.size - 1)
    }



    override fun getItemViewType(position: Int): Int {

        return if(Message[position].message_type==0){
            USER_MESSAGE_LAYOUT
        }else{
            SUPPORT_MESSAGE_LAYOUT
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when(viewType){
            USER_MESSAGE_LAYOUT -> UserMessagesViewHolder(inflater.inflate(R.layout.ticket_chat_model_user, parent, false))
            else -> SupportMessagesViewHolder(inflater.inflate(R.layout.ticket_chat_model_support, parent, false))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder.itemViewType==USER_MESSAGE_LAYOUT){
            holder as UserMessagesViewHolder
            holder.User_Message_time.text=Message[position].time
            holder.User_Message_Body.text=Message[position].message
        }else{
            holder as SupportMessagesViewHolder
            holder.Support_Message_Time.text=Message[position].time
            holder.Support_Message_Body.text=Message[position].message
        }


    }



    inner class UserMessagesViewHolder (var itemView: View):RecyclerView.ViewHolder(itemView){
        var User_Message_time = itemView.findViewById<TextView>(R.id.User_Message_time)
        var User_Message_Body = itemView.findViewById<TextView>(R.id.User_Message_Body)
    }


    inner class SupportMessagesViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var Support_Message_Time = itemView.findViewById<TextView>(R.id.Support_Message_Time)
        var Support_Message_Body = itemView.findViewById<TextView>(R.id.Support_Message_Body)
    }



    override fun getItemCount(): Int {
        return Message.size
    }




}
