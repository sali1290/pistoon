package ir.rahnama.pistoon.webService

import ir.rahnama.pistoon.model.apiModel.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {



    @POST("AddTicket.php")
    @FormUrlEncoded
    fun addTicket( @Field("name") name :String , @Field("phone") phone :String , @Field("title") title :String , @Field("ticket_category") category :String ):Call<Void>


    @POST("get_ticket_list.php")
    @FormUrlEncoded
    fun getTicketList( @Field("phone") phone :String):Call<MutableList<TicketListModel>>



    @POST("close_ticket.php")
    @FormUrlEncoded
    fun closeTicket( @Field("ticket_id") ticket_id :Int):Call<Void>


    @POST("user_message.php")
    @FormUrlEncoded
    fun sendMessage( @Field("ticket_id") ticket_id :Int , @Field("message") message :String ):Call<Void>


    @POST("get_all_message.php")
    @FormUrlEncoded
    fun getAllMessages( @Field("ticket_id") ticket_id :Int):Call<MutableList<TicketMessageModel>>


    @POST("support_message_seen.php")
    @FormUrlEncoded
    fun MessageSeen( @Field("ticket_id") ticket_id :Int):Call<Void>

    @POST("validation_discount_code.php")
    @FormUrlEncoded
    fun ValidationDiscountCode( @Field("code") dis_code :String):Call<List<DiscountCodeModel>>


    @GET("get_best_record.php")
    fun GetBestRecords():Call<MutableList<BestRecordModel>>

    @POST("add_game_record.php")
    @FormUrlEncoded
    fun UpdateGameRecord(@Field("player_name") player_name :String, @Field("record") record :Int):Call<Void>

    @POST("add_user_name.php")
    @FormUrlEncoded
    fun AddGameRecordForFirstTime(@Field("player_name") player_name :String):Call<Void>


    @POST("check_game_user_name.php")
    @FormUrlEncoded
    fun CheckGameUserName( @Field("player_name") player_name :String):Call<Int>


    @POST("sendValidationCode.php")
    @FormUrlEncoded
    fun sendValidationCode( @Field("phone") phone :String):Call<Void>

    @POST("verifyValidationCode.php")
    @FormUrlEncoded
    fun verifyValidationCode( @Field("phone") phone :String , @Field("code") code :String , @Field("name") name :String):Call<Int>

    @POST("savePaymentInfo.php")
    @FormUrlEncoded
    fun savePaymentInfo( @Field("phone") phone :String , @Field("pay_number") pay_number :String , @Field("discount_code") discount_code :String, @Field("acc_type") acc_type :Int):Call<Void>


    @POST("verifyAccount.php")
    @FormUrlEncoded
    fun verifyAccount( @Field("phone") phone :String):Call<List<VerifyAccountModel>>


}