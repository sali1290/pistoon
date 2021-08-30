package ir.rahnama.pistoon.model.apiModel

import com.google.gson.annotations.SerializedName

data class BestRecordModel(
    @SerializedName("id") val id: Int,
    @SerializedName("player_name") val player_name: String,
    @SerializedName("player_record") val player_record: Int
)
