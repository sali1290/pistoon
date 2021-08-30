package ir.rahnama.pistoon.ui.game

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.Transition
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.orhanobut.hawk.Hawk
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.BestRecordAdapter
import ir.rahnama.pistoon.model.apiModel.BestRecordModel
import ir.rahnama.pistoon.ui.mainfragment.BaseFragment
import ir.rahnama.pistoon.ui.mainfragment.HomeFragment
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainPageOfGameFragment : BaseFragment() {


    private lateinit var btn_game_info : ImageView
    private lateinit var btn_back_toHome : ImageView
    private lateinit var btn_start_game : ConstraintLayout
    private lateinit var btn_show_records : ConstraintLayout
    private lateinit var main_game_page_layout : ConstraintLayout
    private lateinit var btn_hide_gameRecord_page : LinearLayout
    private lateinit var framLayout_show_records : FrameLayout
    private lateinit var your_score_txt : TextView
    private lateinit var game_page_txt_username : TextView
    private lateinit var btn_volume_off : ToggleButton
    private lateinit var Best_record_recyclerView : RecyclerView
    private lateinit var bottom_navigation : BottomNavigationView
    private var volumeState = false
    private var backpressState=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_main_page_of_game, container, false)


         btn_game_info = view.findViewById(R.id.btn_game_info)
        btn_back_toHome = view.findViewById(R.id.btn_back_toHome)
         btn_start_game= view.findViewById(R.id.btn_start_game)
        btn_show_records= view.findViewById(R.id.btn_show_records)
        framLayout_show_records= view.findViewById(R.id.framLayout_show_records)
        main_game_page_layout= view.findViewById(R.id.main_game_page_layout)
        game_page_txt_username= view.findViewById(R.id.game_page_txt_username)
         your_score_txt= view.findViewById(R.id.txt_your_score)
        btn_hide_gameRecord_page= view.findViewById(R.id.btn_hide_gameRecord_page)
        Best_record_recyclerView= view.findViewById(R.id.Best_record_recyclerView)
        btn_volume_off= view.findViewById(R.id.btn_volume_off)

         bottom_navigation= requireActivity().findViewById(R.id.bottom_navigation)
        bottom_navigation.visibility=View.GONE


        Hawk.init(requireActivity()).build()

        //best record
        your_score_txt.text=getBestRecordFromMemory().toString()
        //user name
        game_page_txt_username.text=getUserName()

        if (getUserName() != ""){
            btn_show_records.visibility=View.VISIBLE
        }

        btn_start_game.setOnClickListener{

            if (getUserName() == ""){
                AddUserNameAlertDialog()
            }else{
                StartGame()
            }

           }



        btn_show_records.setOnClickListener {
            GetBestRecords()
            val transition: Transition = Slide(Gravity.BOTTOM)
            transition.addTarget(R.id.framLayout_show_records)
            transition.duration = 500
            android.transition.TransitionManager.beginDelayedTransition(main_game_page_layout, transition)
            framLayout_show_records.visibility = View.VISIBLE
            backpressState=true

        }




        btn_volume_off.isChecked=getvolumeSettingFromMemory()
        btn_volume_off.setOnClickListener{
            if (getvolumeSettingFromMemory()){ volumOff(false)
            }else{ volumOff(true)}
        }


        btn_game_info.setOnClickListener{
            infoAlertdialog(getString(R.string.game_info),getString(R.string.game_info_text))
        }


        btn_hide_gameRecord_page.setOnClickListener { HideRecordPage() }

        btn_back_toHome.setOnClickListener{backToHome()}


        return view
    }


    private fun HideRecordPage(){
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.addTarget(R.id.framLayout_show_records)
        transition.duration = 500
        android.transition.TransitionManager.beginDelayedTransition(main_game_page_layout, transition)
        framLayout_show_records.visibility = View.GONE
        backpressState=false
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction: androidx.fragment.app.FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun volumOff(volume:Boolean){
        val sharedpref =requireActivity().getSharedPreferences("volumeOff", Context.MODE_PRIVATE)
        val editor = sharedpref.edit()
        editor.putBoolean("volume", volume)
        editor.apply()
    }

    private fun getvolumeSettingFromMemory() :Boolean{
        val sharedpref =requireActivity().getSharedPreferences("volumeOff", Context.MODE_PRIVATE)
        return sharedpref.getBoolean("volume",false)
    }



    private fun alertDialog(alert_title: String) {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_locked_section, null)

        val title = dialogLayout.findViewById<TextView>(R.id.alert_txt_title)
        val btn_ok = dialogLayout.findViewById<TextView>(R.id.btn_alert_ok)

        title.text = alert_title

        btn_ok.setOnClickListener{
            builder.dismiss()
            bottom_navigation.selectedItemId=R.id.more_nav
        }


        builder.setView(dialogLayout)
        builder.show()
    }

    private fun infoAlertdialog(alert_title: String, alert_body: String) {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_more_page_model, null)

        val title = dialogLayout.findViewById<TextView>(R.id.alertdialog_txt_title)
        val body = dialogLayout.findViewById<TextView>(R.id.alertdialog_txt_body)

        title.text = alert_title
        body.text = alert_body

        builder.setNegativeButton(resources.getString(R.string.Ok_txt)) { _, i -> }

        builder.setView(dialogLayout)
        builder.show()
    }


    private fun getBestRecordFromMemory():Int{
        val sharedpref =requireActivity().getSharedPreferences("gameRecord", Context.MODE_PRIVATE)
        return sharedpref.getInt("record",0)
    }


    override fun onResume() {
        btn_volume_off.isChecked=getvolumeSettingFromMemory()
        super.onResume()
    }


    private fun gameFreeCounter(count: Int){
        val sharedpref =requireActivity().getSharedPreferences("gameCounter", Context.MODE_PRIVATE)
        val editor = sharedpref.edit()
        editor.putInt("counter", count)
        editor.apply()
    }



    private fun twoTimePlayWithoutAccount():Int{
        val sharedpref =requireActivity().getSharedPreferences("gameCounter", Context.MODE_PRIVATE)
        return sharedpref.getInt("counter", 0)
    }



    private fun StartGame(){

        if (Hawk.get("fullAccount",0) == 20){
             loadFragment(GameFragment())
        }else{

            if (twoTimePlayWithoutAccount()==2){
                alertDialog(resources.getString(R.string.AccessToGameAlert))
            }else{
                gameFreeCounter(twoTimePlayWithoutAccount() + 1)
                loadFragment(GameFragment())
            }


        }



    }


    private fun AddUserNameAlertDialog() {
        val builder = AlertDialog.Builder(requireActivity()).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_add_user_name, null)

        val input_userName = dialogLayout.findViewById<TextInputEditText>(R.id.user_name_edt_input)
        val input_userName_layout = dialogLayout.findViewById<TextInputLayout>(R.id.user_name_input_layout)
        val btn_sumbit_username = dialogLayout.findViewById<Button>(R.id.btn_sumbit_username)


        input_userName.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                input_userName_layout.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }


        })




        btn_sumbit_username.setOnClickListener{


            val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
            val call : Call<Int> =apiService.CheckGameUserName(input_userName.text.toString())
            call.enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    Log.i("test", response.toString())
                    if (response.isSuccessful) {
                        if (response.body() == 0) {
                            input_userName_layout.isErrorEnabled = true
                            input_userName_layout.error = " تکراری  "
                        } else {
                            saveUserName(input_userName.text.toString())
                            builder.dismiss()
                            btn_show_records.visibility=View.VISIBLE
                            game_page_txt_username.text=input_userName.text.toString()
                            Toast.makeText(requireActivity(), resources.getString(R.string.SuccessToSave), Toast.LENGTH_SHORT).show()
                            SendUserNameAndRecordForFirstTime()
                        }

                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(requireActivity(), resources.getString(R.string.CantAccessToNet), Toast.LENGTH_SHORT).show()
                }
            })




        }


        builder.setView(dialogLayout)
        builder.show()
    }


    fun saveUserName(userName:String){
        val sharedpref =requireActivity().getSharedPreferences("gameRecord", Context.MODE_PRIVATE)
            sharedpref.edit().putString("user_name", userName).apply()
    }

    private fun getUserName(): String? {
        val sharedpref =requireActivity().getSharedPreferences("gameRecord", Context.MODE_PRIVATE)
        return sharedpref.getString("user_name","")
    }


    private fun GetBestRecords(){
        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call : Call<MutableList<BestRecordModel>> =apiService.GetBestRecords()
        call.enqueue(object : Callback<MutableList<BestRecordModel>> {
            override fun onResponse(
                call: Call<MutableList<BestRecordModel>>,
                response: Response<MutableList<BestRecordModel>>
            ) {

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val adapter = BestRecordAdapter(requireActivity(), response.body()!!)
                        Best_record_recyclerView.adapter = adapter
                        Best_record_recyclerView.layoutManager =
                            LinearLayoutManager(requireContext())

                    }


                }


            }

            override fun onFailure(call: Call<MutableList<BestRecordModel>>, t: Throwable) {
                Toast.makeText(requireActivity(), resources.getString(R.string.CantAccessToNet), Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun SendUserNameAndRecordForFirstTime(){
        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call : Call<Void> =apiService.AddGameRecordForFirstTime(getUserName()!!)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }

    private fun backToHome(){
        val fragmentTransaction: androidx.fragment.app.FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, HomeFragment())
        fragmentTransaction.remove(this)
        fragmentTransaction.commit()
    }

    override fun onBackPressed(): Boolean {
       if (backpressState){
           HideRecordPage()
       }else{
           backToHome()
       }
        return true
    }






}