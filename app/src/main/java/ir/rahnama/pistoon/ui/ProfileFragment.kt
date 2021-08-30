package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.model.apiModel.VerifyAccountModel
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : BaseFragment() {


    private lateinit var newUser_addName_layout:TextInputLayout
    private lateinit var newUser_addPhone_layout:TextInputLayout
    private lateinit var newUser_validCode_layout:TextInputLayout

    private lateinit var newUser_addName:TextInputEditText
    private lateinit var newUser_addPhone:TextInputEditText
    private lateinit var newUser_validCode:TextInputEditText
    private lateinit var send_validationCode:LinearLayout
    private lateinit var sumbit_code_txt:TextView
    private lateinit var userPhone_textview:TextView
    private lateinit var userName_textview:TextView
    private lateinit var account_show_txt:TextView
    private lateinit var btn_copy_paynumber:ImageView
    private lateinit var profile_btn_back:ImageView
    private lateinit var txt_showPayNumber:TextView
    private lateinit var txt_showPayTime:TextView
    private lateinit var add_newUser_parent_layout:ConstraintLayout
    private lateinit var account_show_layout:ConstraintLayout
    private lateinit var add_newUser_MAin_parent_layout:ConstraintLayout


    private var phoneValid=false
    private var nameValid=false
    private var codeValid=false
    private var lastbtnState=false



    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        newUser_addName_layout=view.findViewById(R.id.newUser_addName_layout)
        newUser_addPhone_layout=view.findViewById(R.id.newUser_addPhone_layout)
        newUser_validCode_layout=view.findViewById(R.id.newUser_validCode_layout)
        newUser_addName=view.findViewById(R.id.newUser_addName)
        newUser_addPhone=view.findViewById(R.id.newUser_addPhone)
        newUser_validCode=view.findViewById(R.id.newUser_validCode)
        send_validationCode=view.findViewById(R.id.send_validationCode)
        sumbit_code_txt=view.findViewById(R.id.sumbit_code_txt)
        userName_textview=view.findViewById(R.id.userName_textview)
        userPhone_textview=view.findViewById(R.id.userPhone_textview)
        account_show_txt=view.findViewById(R.id.account_show_txt)
        account_show_layout=view.findViewById(R.id.account_show_layout)
        txt_showPayNumber=view.findViewById(R.id.txt_showPayNumber)
        txt_showPayTime=view.findViewById(R.id.txt_showPayTime)
        profile_btn_back=view.findViewById(R.id.profile_btn_back)
        btn_copy_paynumber=view.findViewById(R.id.btn_copy_payNumber)
        add_newUser_parent_layout=view.findViewById(R.id.add_newUser_parent_layout)
        add_newUser_MAin_parent_layout=view.findViewById(R.id.add_newUser_MAin_parent_layout)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility=View.GONE

        Hawk.init(requireActivity()).build()



        initPage()

        newUser_addPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newUser_addPhone_layout.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
                if (newUser_addPhone.text!!.length < 11) {
                    newUser_addPhone_layout.isErrorEnabled = true
                    newUser_addPhone_layout.error =resources.getString(R.string.InvalidText)
                    phoneValid = false
                } else {
                    newUser_addPhone_layout.isErrorEnabled = false
                    phoneValid = true
                }
            }

        })


        newUser_addName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newUser_addName_layout.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
                if (newUser_addName.text.toString().isEmpty()) {
                    newUser_addName_layout.isErrorEnabled = true
                    newUser_addName_layout.error = "الزامی"
                    nameValid = false
                } else {
                    nameValid = true
                    newUser_addName_layout.isErrorEnabled = false
                }
            }
        })

        newUser_validCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newUser_addPhone_layout.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
                if (newUser_addPhone.text!!.length < 5) {
                    newUser_addPhone_layout.isErrorEnabled = true
                    newUser_addPhone_layout.error =resources.getString(R.string.InvalidText)
                    codeValid = false
                } else {
                    newUser_addPhone_layout.isErrorEnabled = false
                    codeValid = true
                }
            }
        })



        send_validationCode.setOnClickListener {
            if (phoneValid && nameValid && !lastbtnState){
                requestToSendValidationCode()
                lastbtnState=true
            }else{
                verifyValidationCode(
                    newUser_addPhone.text.toString(),
                    newUser_validCode.text.toString(),
                    newUser_addName.text.toString()
                )
            }
        }


        btn_copy_paynumber.setOnClickListener {

            val cm: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val txtcopy: String = txt_showPayNumber.text.toString()
            val clipData = ClipData.newPlainText("text", txtcopy)
            cm.setPrimaryClip(clipData)
            Toast.makeText(requireActivity(), "کپی شد", Toast.LENGTH_SHORT).show()

        }

        profile_btn_back.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()

        }

        return view
    }





    private fun requestToSendValidationCode(){



        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call: Call<Void> = apiService.sendValidationCode(newUser_addPhone.text.toString())
        call.enqueue(object : Callback<Void> {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    newUser_validCode_layout.visibility = View.VISIBLE
                    send_validationCode.background =
                        requireContext().getDrawable(R.drawable.indicator_done_shape)
                    sumbit_code_txt.text =resources.getString(R.string.sumbit_btn)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireActivity(), "لطفا دوباره تلاش کنید", Toast.LENGTH_SHORT)
                    .show()

            }

        })


    }

    private fun verifyValidationCode(userPhone: String, Code: String, userName: String){



        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call: Call<Int> = apiService.verifyValidationCode(userPhone, Code, userName)
        call.enqueue(object : Callback<Int> {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    if (response.body() == 1) {
                        verifyAccountType(userPhone,userName)
                    } else {
                        newUser_validCode_layout.isErrorEnabled = true
                        newUser_validCode_layout.error = "کد نامعتبر است"
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })


    }

    private fun verifyAccountType(userPhone: String,userName: String){



        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call: Call<List<VerifyAccountModel>> = apiService.verifyAccount(userPhone)
        call.enqueue(object : Callback<List<VerifyAccountModel>> {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onResponse(call: Call<List<VerifyAccountModel>>, response: Response<List<VerifyAccountModel>>) {
                if (response.isSuccessful) {
                    if (response.body()!![0].acc_type == 1){
                        Hawk.put("fullAccount",20)
                    }else if (response.body()!![0].acc_type == 2 ){
                        Hawk.put("goldenAccount",10)
                    }
                    Hawk.put("logIn", 1)
                    Hawk.put("userName", userName)
                    Hawk.put("userPhone", userPhone)
                    HideSignInLayoutAnimation()
                    initPage()
                }
            }

            override fun onFailure(call: Call<List<VerifyAccountModel>>, t: Throwable) {
                Toast.makeText(requireActivity(), "لطفا دوباره تلاش کنید", Toast.LENGTH_SHORT)
                    .show()
            }

        })


    }



    private fun HideSignInLayoutAnimation(){
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.addTarget(R.id.add_newUser_parent_layout)
        transition.duration = 500
        android.transition.TransitionManager.beginDelayedTransition(
            add_newUser_MAin_parent_layout,
            transition
        )
        add_newUser_parent_layout.visibility = View.GONE
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initPage(){

        if (Hawk.get("logIn", 0) == 0){
            add_newUser_parent_layout.visibility=View.VISIBLE
        }else{

            userName_textview.text=Hawk.get("userName")
            userPhone_textview.text=Hawk.get("userPhone")
            txt_showPayNumber.text=Hawk.get("payNumber")
            txt_showPayTime.text=Hawk.get("payTime")

            if (Hawk.get("fullAccount", 0) == 20 ){
                account_show_txt.text=resources.getString(R.string.FullAccountTitle_txt)
                account_show_layout.background=requireContext().resources.getDrawable(R.color.light_blue)
            }else{
                if (Hawk.get("goldenAccount", 0) == 10){
                    account_show_txt.text=resources.getString(R.string.GoldenAccountTitle_txt)
                    account_show_layout.background=requireContext().resources.getDrawable(R.color.golden)
                }

            }


        }








    }



}

