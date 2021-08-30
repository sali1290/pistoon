package ir.rahnama.pistoon.ui.tickets

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.ListOfTicketAdapter
import ir.rahnama.pistoon.model.apiModel.TicketListModel
import ir.rahnama.pistoon.ui.ProfileFragment
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListOfTicketFragment : Fragment() , ListOfTicketAdapter.OpenTicket{


    private lateinit var TicketListRecyclerView : RecyclerView

    private lateinit var btn_add_new_ticket:ImageView
    private lateinit var add_ticket_btn_back:ImageView
    private lateinit var ticket_coordinator_layout:CoordinatorLayout
    private lateinit var firstTimeAddNewTicket:LinearLayout
    private lateinit var adapter:ListOfTicketAdapter
    private lateinit var refreshData:SwipeRefreshLayout



    private var userPhoneNumber :String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_of_ticket, container, false)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility=View.GONE
        btn_add_new_ticket=view.findViewById(R.id.btn_add_new_ticket)
        add_ticket_btn_back=view.findViewById(R.id.add_ticket_btn_back)
        ticket_coordinator_layout=view.findViewById(R.id.ticket_coordinator_layout)
        TicketListRecyclerView=view.findViewById(R.id.TicketListRecyclerView)
        firstTimeAddNewTicket=view.findViewById(R.id.firstTimeAddNewTicket)
        refreshData=view.findViewById(R.id.refreshData)

        //init adapter
        adapter = ListOfTicketAdapter(requireActivity(), this)


        Hawk.init(requireActivity()).build()

         userPhoneNumber = Hawk.get("userPhone")
        if ( Hawk.contains("userPhone") ) {
            firstTimeAddNewTicket.visibility=View.GONE
            requestForTicketList(userPhoneNumber!!)
        }








        refreshData.setOnRefreshListener {

           if (Hawk.contains("userPhone")){
               requestForTicketList(userPhoneNumber!!)
           }
            Handler().postDelayed({
                if (refreshData.isRefreshing) {
                    refreshData.isRefreshing = false
                }
            }, 1000)
        }








        btn_add_new_ticket.setOnClickListener{addTicketAlertDialog()}
        add_ticket_btn_back.setOnClickListener{popBackStack()}
        firstTimeAddNewTicket.setOnClickListener{addTicketAlertDialog()}



        return view
    }



    private fun addTicketAlertDialog() {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.add_new_ticket_alertdialog, null)
        val spinner =dialogLayout.findViewById<Spinner>(R.id.add_ticket_spinner)
        val txt_show_toLogin =dialogLayout.findViewById<TextView>(R.id.txt_show_toLogin)
        val spinner_layout =dialogLayout.findViewById<ConstraintLayout>(R.id.spinner_layout)
        val btn_add_ticket=dialogLayout.findViewById<Button>(R.id.add_ticket_btn_create)
        val add_ticket_edt_title=dialogLayout.findViewById<TextInputEditText>(R.id.add_ticket_edt_title)
        val title_textinputLayout=dialogLayout.findViewById<TextInputLayout>(R.id.title_textinputLayout)

        var toDoLogIn=false

        if (Hawk.contains("logIn")){
            txt_show_toLogin.visibility=View.INVISIBLE
        }else{
            title_textinputLayout.visibility=View.INVISIBLE
            spinner_layout.visibility=View.INVISIBLE
            btn_add_ticket.text="ساخت حساب"
            toDoLogIn=true
        }

        val items = arrayOf("مشکلات فنی", "مشکلات پرداخت", "محتوای برنامه", "سایر")
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter



        var titleValid=false

        add_ticket_edt_title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                title_textinputLayout.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
                if (add_ticket_edt_title.text.toString().isEmpty()) {
                    title_textinputLayout.isErrorEnabled = true
                    title_textinputLayout.error = "موضوع تیکت را وارد کنید"
                    titleValid = false
                } else {
                    title_textinputLayout.isErrorEnabled = false
                    titleValid = true
                }
            }
        })












        btn_add_ticket.setOnClickListener{


            if (toDoLogIn){
                loadFragment(ProfileFragment())
                builder.dismiss()
            }else{
                if (titleValid){
                    requestForAddTicket(
                        Hawk.get("userName"),
                        Hawk.get("userPhone"),
                        add_ticket_edt_title.text.toString(),
                        spinner.selectedItemPosition.toString()
                    )
                    builder.dismiss()
                }else{
                    Toast.makeText(requireActivity(), "لطفا اطلاعات را به صورت صحیح وارد کنید", Toast.LENGTH_SHORT).show()
                }
            }





        }

        builder.setView(dialogLayout)
        builder.show()
    }

    private fun popBackStack(){
        val fragmentManager: FragmentTransaction =requireActivity().supportFragmentManager.beginTransaction()
        requireActivity().supportFragmentManager.popBackStack()
        fragmentManager.remove(this).commit()
    }



    private fun requestForTicketList(phone: String) {
        if (Hawk.contains("userPhone")){
            val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
            val call : Call<MutableList<TicketListModel>> =apiService.getTicketList(phone)
            call.enqueue(object : Callback<MutableList<TicketListModel>> {
                override fun onResponse(
                    call: Call<MutableList<TicketListModel>>,
                    response: Response<MutableList<TicketListModel>>
                ) {
                    Log.i("test", response.toString())
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            TicketListRecyclerView.adapter = adapter
                            TicketListRecyclerView.layoutManager =
                                LinearLayoutManager(requireContext())
                            adapter.setItems(response.body()!!)
                        }

                    }
                }

                override fun onFailure(call: Call<MutableList<TicketListModel>>, t: Throwable) {
                    Snackbar.make(
                        ticket_coordinator_layout,
                        "مطمئنی اینترنتت وصله؟ ",
                        Snackbar.LENGTH_LONG
                    ).show()
                    Log.i("test", t.message.toString())
                }
            })

        }

    }


    private fun requestForAddTicket(name: String, phone: String, title: String, category: String){

        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call:Call<Void> = apiService.addTicket(name, phone, title, category)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    requestForTicketList(userPhoneNumber!!)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(
                    ticket_coordinator_layout,
                    "مطمئنی اینترنتت وصله؟ ",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        })


    }







    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun openTicket(id: Int, State: Int) {
        loadFragment(ShowTicketContentFragment(id, State))
    }




}


