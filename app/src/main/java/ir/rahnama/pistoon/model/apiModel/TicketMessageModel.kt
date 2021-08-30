package ir.rahnama.pistoon.model.apiModel

import com.google.gson.annotations.SerializedName

data class TicketMessageModel(
    @SerializedName("message") val message:String,
    @SerializedName("time") val time:String,
    @SerializedName("message_type") val message_type:Int)