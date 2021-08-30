package ir.rahnama.pistoon.model.apiModel

import com.google.gson.annotations.SerializedName

data class TicketListModel(
    @SerializedName ("id") val id:Int,
    @SerializedName("phone") val phone:String,
    @SerializedName("ticket_title")val ticket_title:String,
    @SerializedName("ticket_state")val ticket_state :Int,
    @SerializedName("message_notify")val message_notify :Int)
