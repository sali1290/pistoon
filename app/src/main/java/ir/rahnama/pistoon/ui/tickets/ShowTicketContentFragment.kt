package ir.rahnama.pistoon.ui.tickets

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.ShowTicketChatAdapter
import ir.rahnama.pistoon.model.apiModel.TicketMessageModel
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ShowTicketContentFragment(private var ticketId: Int, private var ticketState: Int) : Fragment() {


    private lateinit var closed_ticket_layout:CardView
    private lateinit var btn_close_ticket:ImageView
    private lateinit var ticket_chat_page_coordinator:CoordinatorLayout
    private lateinit var input_text_layout:ConstraintLayout
    private lateinit var ticket_chat_page_btn_back:ImageView
    private lateinit var btn_send_message:ImageView
    private lateinit var edt_input_message:EditText
    private lateinit var addNewMessage_txt:TextView
    private lateinit var chatRecycler:RecyclerView
    private lateinit var adapter:ShowTicketChatAdapter
    //api
    private lateinit var apiService:ApiInterface

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_ticket_content, container, false)

        //api interface
         apiService= ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        closed_ticket_layout=view.findViewById(R.id.closed_ticket_layout)
        btn_close_ticket=view.findViewById(R.id.btn_close_ticket)
        ticket_chat_page_coordinator=view.findViewById(R.id.ticket_chat_page_coordinator)
        ticket_chat_page_btn_back=view.findViewById(R.id.ticket_chat_page_btn_back)
        btn_send_message=view.findViewById(R.id.btn_send_message)
        addNewMessage_txt=view.findViewById(R.id.addNewMessage_txt)
        edt_input_message=view.findViewById(R.id.edt_input_message)
        chatRecycler=view.findViewById(R.id.show_ticket_chat_recycler)
        input_text_layout=view.findViewById(R.id.input_text_layout)

        //init adapter
        adapter= ShowTicketChatAdapter(requireActivity())


        if (ticketState==1){
            input_text_layout.visibility=View.INVISIBLE
            closed_ticket_layout.visibility=View.VISIBLE
            btn_close_ticket.visibility=View.INVISIBLE
        }

        MessageSeen()//message from support has  seen

        getAllMessages()

















        btn_send_message.setOnClickListener{
            if (edt_input_message.text.isEmpty()){
                Toast.makeText(requireActivity(), "لطفا شرح مشکل را بنویسید", Toast.LENGTH_SHORT).show()
            }else{
                SendMessage(ticketId, edt_input_message.text.toString())
                edt_input_message.text.clear()

            }

        }


        btn_close_ticket.setOnClickListener{CloseTicketAlertDialog()}
        ticket_chat_page_btn_back.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }





    private fun CloseTicketAlertDialog() {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_close_ticket, null)

        val close_ticket_btn_close = dialogLayout.findViewById<TextView>(R.id.close_ticket_btn_close)
        val close_ticket_btn_cancel = dialogLayout.findViewById<TextView>(R.id.close_ticket_btn_cancel)


        close_ticket_btn_cancel.setOnClickListener{ builder.dismiss() }

        close_ticket_btn_close.setOnClickListener{
            CloseTicket(ticketId)
            builder.dismiss()
        }


        builder.setView(dialogLayout)
        builder.show()
    }


    private fun CloseTicket(ticket_id: Int){
        val call: Call<Void> = apiService.closeTicket(ticket_id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(
                    ticket_chat_page_coordinator,
                    "بستن تیکت با مشکل روبرو شد ",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        })


    }


    private fun SendMessage(ticket_id: Int, message: String){

        val call: Call<Void> = apiService.sendMessage(ticket_id, message)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    UserSendNewMessage(message)
                    if (chatRecycler.adapter?.itemCount == null ){ getAllMessages() }
                    Toast.makeText(requireActivity(), "ارسال شد", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireActivity(), "مشکل در ارسال پیام  ", Toast.LENGTH_SHORT).show()

            }


        })


    }


    private fun getAllMessages(){

        val call: Call<MutableList<TicketMessageModel>> = apiService.getAllMessages(ticketId)
        call.enqueue(object : Callback<MutableList<TicketMessageModel>> {
            override fun onResponse(
                call: Call<MutableList<TicketMessageModel>>,
                response: Response<MutableList<TicketMessageModel>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        addNewMessage_txt.visibility=View.GONE
                        chatRecycler.adapter = adapter
                        chatRecycler.layoutManager = LinearLayoutManager(requireContext())
                        adapter.ShowMessage(response.body()!!)
                        if (chatRecycler.adapter?.itemCount != null ){
                            chatRecycler.scrollToPosition(chatRecycler.adapter!!.itemCount - 1)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<MutableList<TicketMessageModel>>, t: Throwable) {
                addNewMessage_txt.visibility=View.VISIBLE
            }


        })

    }


    private fun MessageSeen(){

        val call: Call<Void> = apiService.MessageSeen(ticketId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.i("message" , response.message())
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireActivity(), " عدم دسترسی به اینترنت  ", Toast.LENGTH_SHORT).show()
            }


        })


    }




    @SuppressLint("SimpleDateFormat")
    private fun UserSendNewMessage(message:String){
        val df: DateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        val date: String = df.format(Calendar.getInstance().time)
        adapter.AddNewMessage(TicketMessageModel(message, date , 0))
        if (chatRecycler.adapter?.itemCount != null ){
            chatRecycler.scrollToPosition(chatRecycler.adapter!!.itemCount - 1)
        }
    }



}