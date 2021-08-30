package ir.rahnama.pistoon.ui.mainfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import com.zarinpal.ewallets.purchase.ZarinPal
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.apiModel.DiscountCodeModel
import ir.rahnama.pistoon.ui.AboutUsFragment
import ir.rahnama.pistoon.ui.ProfileFragment
import ir.rahnama.pistoon.ui.tickets.ListOfTicketFragment
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoreFragment : BaseFragment() {


    private lateinit var bottom_navigation: BottomNavigationView

    private lateinit var ticket_cardviee:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        val call_us_cardview = view.findViewById<CardView>(R.id.call_us_cardview)
        val about_us_cardview = view.findViewById<CardView>(R.id.about_us_cardview)
        val manabe_cardview = view.findViewById<CardView>(R.id.manabe_cardview)
        val profile_cardview = view.findViewById<CardView>(R.id.profile_cardview)
        val money_cardview = view.findViewById<CardView>(R.id.money_cardview)
        val request_friend_cardview = view.findViewById<CardView>(R.id.request_friend_cardview)
        val txt_full_account = view.findViewById<TextView>(R.id.txt_full_acc)
        val txt_golden_account = view.findViewById<TextView>(R.id.txt_golden_acc)
        ticket_cardviee=view.findViewById(R.id.ticket_cardviee)

        val totalPayment = view.findViewById<LinearLayout>(R.id.totalAccess_Payment)
        val goldenTestPayment = view.findViewById<LinearLayout>(R.id.goldenTest_payment)

        bottom_navigation = requireActivity().findViewById(R.id.bottom_navigation)
        bottom_navigation.visibility=View.VISIBLE


        call_us_cardview.setOnClickListener { callUsWithEmail() }
        about_us_cardview.setOnClickListener { loadFragment(AboutUsFragment()) }
        manabe_cardview.setOnClickListener { alertDialog("منابع", getString(R.string.manabe_txt)) }
        request_friend_cardview.setOnClickListener {inviteFriends()}


        Hawk.init(requireActivity()).build()


        if (Hawk.get("fullAccount",0) == 20){
            totalPayment.isClickable=false
            totalPayment.isEnabled=false
            txt_full_account.text=resources.getString(R.string.ActiveAccount)
            //golden acc
            goldenTestPayment.isClickable=false
            goldenTestPayment.isEnabled=false
            txt_golden_account.text=resources.getString(R.string.ActiveAccount)
        }

        if (Hawk.get("goldenAccount",0) == 10){
            goldenTestPayment.isClickable=false
            goldenTestPayment.isEnabled=false
            txt_golden_account.text=resources.getString(R.string.ActiveAccount)
        }


        totalPayment.setOnClickListener{
          BuyAlertDialog(resources.getString(R.string.GetFullAccount) , 20000L , 1)
        }
        goldenTestPayment.setOnClickListener{
            BuyAlertDialog(resources.getString(R.string.GetGoldenAccount)  , 15000L , 2)
        }



        ticket_cardviee.setOnClickListener{loadFragment(ListOfTicketFragment())}

        profile_cardview.setOnClickListener{loadFragment(ProfileFragment())}


        money_cardview.setOnClickListener{
            Toast.makeText(requireActivity(), "به زودی فعال میشه :)  ", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun callUsWithEmail() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@iranswan.ir"))
        startActivity(Intent.createChooser(i, "ارسال ایمیل..."))
    }

    private fun inviteFriends() {
        val shareBody = "http://pistoon.iranswan.ir/"
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "دانلود اپلیکیشن آموزشی آیین نامه راهنمایی رانندگی")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "انتخاب شبکه اجتماعی..."))

    }

    override fun onBackPressed(): Boolean {
        bottom_navigation.selectedItemId = R.id.home_nav
        return true
    }

    private fun alertDialog(alert_title: String, alert_body: String) {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_more_page_model, null)

        val title = dialogLayout.findViewById<TextView>(R.id.alertdialog_txt_title)
        val body = dialogLayout.findViewById<TextView>(R.id.alertdialog_txt_body)

        title.text = alert_title
        body.text = alert_body

        builder.setNegativeButton("باشه") { _, i -> }

        builder.setView(dialogLayout)
        builder.show()
    }


    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }



    private fun payment(amount: Long  ,des :String) {
        try {
            val purchase = ZarinPal.getPurchase(requireActivity())
            val paymentRequest = ZarinPal.getPaymentRequest()
            paymentRequest.merchantID = resources.getString(R.string.MerChantCode)
            paymentRequest.amount = amount
            paymentRequest.setCallbackURL(resources.getString(R.string.PaymentCallBack))
            paymentRequest.description = des
            purchase.startPayment(paymentRequest) { status, _, _, intent ->
                if (status == 100) {
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "خطا در ایجاد درخواست", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun BuyAlertDialog(accName:String, accPrice:Long , accType:Int) {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_buy_account, null)


        val buy_acc_txt_accName = dialogLayout.findViewById<TextView>(R.id.buy_acc_txt_accName)
        val edt_discount_code = dialogLayout.findViewById<EditText>(R.id.edt_discount_code)
        val btn_check_discount_code = dialogLayout.findViewById<LinearLayout>(R.id.btn_check_discount_code)
        val txt_check_discount_code = dialogLayout.findViewById<TextView>(R.id.txt_check_discount_code)
        val txt_Last_Price = dialogLayout.findViewById<TextView>(R.id.txt_Last_Price)
        val btn_Buy_account = dialogLayout.findViewById<Button>(R.id.btn_Buy_account)
        val txt_show_discount_percent = dialogLayout.findViewById<TextView>(R.id.txt_show_discount_percent)
        val show_discount_percent_layout = dialogLayout.findViewById<ConstraintLayout>(R.id.show_discount_percent_layout)
        val txt_showPhoneNumber = dialogLayout.findViewById<TextView>(R.id.txt_showPhoneNumber)
        val txt_showUserName = dialogLayout.findViewById<TextView>(R.id.txt_showUserName)
        val add_useraccount_layout = dialogLayout.findViewById<LinearLayout>(R.id.add_useraccount_layout)


        var lastPrice :Long = accPrice

        var activeAccount = false
        //set text
        buy_acc_txt_accName.text=accName
        txt_Last_Price.text="$lastPrice تومان "

        if (Hawk.get("logIn" , 0) == 1 ){
            activeAccount=true
            txt_showUserName.text=Hawk.get("userName")
            txt_showPhoneNumber.text=Hawk.get("userPhone")
            add_useraccount_layout.visibility=View.INVISIBLE
        }

        edt_discount_code.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btn_check_discount_code.background=requireContext().getDrawable(R.drawable.indicator_done_shape)
                txt_check_discount_code.text=resources.getString(R.string.Survey_btn)
            }

            override fun afterTextChanged(p0: Editable?) {
            }


        })

        add_useraccount_layout.setOnClickListener {
            loadFragment(ProfileFragment())
            builder.dismiss()
        }


        btn_check_discount_code.setOnClickListener{

            btn_check_discount_code.visibility=View.INVISIBLE

            val apiService :ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
            val call: Call<List<DiscountCodeModel>> = apiService.ValidationDiscountCode(edt_discount_code.text.toString())
            call.enqueue(object : Callback<List<DiscountCodeModel>> {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onResponse(
                    call: Call<List<DiscountCodeModel>>,
                    response: Response<List<DiscountCodeModel>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                                btn_check_discount_code.visibility = View.VISIBLE
                                txt_check_discount_code.text = "ثبت شد"
                                lastPrice =  accPrice - (response.body()!![0].percent * accPrice) / 100
                                txt_Last_Price.text = "$lastPrice تومان "
                            show_discount_percent_layout.visibility=View.VISIBLE
                            txt_show_discount_percent.text="${response.body()!![0].percent}"
                            saveDiscountCode(edt_discount_code.text.toString())
                        }

                    } else {
                        btn_check_discount_code.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), resources.getString(R.string.CantAccessToNet), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DiscountCodeModel>>, t: Throwable) {
                    btn_check_discount_code.visibility = View.VISIBLE
                    btn_check_discount_code.visibility = View.VISIBLE
                    txt_check_discount_code.text =resources.getString(R.string.InvalidText)
                    btn_check_discount_code.background = requireContext().getDrawable(R.drawable.indicator_false_answer)
                    lastPrice=accPrice
                    show_discount_percent_layout.visibility=View.INVISIBLE
                    txt_Last_Price.text = "$lastPrice تومان "

                }


            })






        }



        btn_Buy_account.setOnClickListener {
            if(activeAccount){
                btn_Buy_account.visibility=View.INVISIBLE
                StoreAccTypeForVerifyAfterPay(accType)
                payment(lastPrice,accName)
            }else{
                Toast.makeText(requireActivity(),resources.getString(R.string.AddAccountToApp), Toast.LENGTH_LONG).show()
            }
        }






        builder.setView(dialogLayout)
        builder.show()
    }



    private fun StoreAccTypeForVerifyAfterPay(type:Int){
        val sharedPreferences =requireActivity().getSharedPreferences("afterPay", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("type", type).apply()
    }

    private fun saveDiscountCode(code:String){
        val sharedPreferences =requireActivity().getSharedPreferences("discountCode", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("code", code).apply()
    }

}