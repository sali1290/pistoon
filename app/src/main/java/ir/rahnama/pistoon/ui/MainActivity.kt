package ir.rahnama.pistoon.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import com.zarinpal.ewallets.purchase.ZarinPal
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.ui.mainfragment.*
import ir.rahnama.pistoon.webService.ApiClient
import ir.rahnama.pistoon.webService.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {


    // lateinit var viewpager : ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { ViewPumpContextWrapper.wrap(it) })
    }


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        bottomNavigationView = findViewById(R.id.bottom_navigation)



        //secure key values
        Hawk.init(this).build()
        Hawk.put("Unknown",resources.getString(R.string.url))


        loadFragment(HomeFragment())



        bottomNavigationView.selectedItemId = R.id.home_nav
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.more_nav -> {
                    loadFragment(MoreFragment())
                }
                R.id.tests_nav -> {
                    loadFragment(TestsFragment())
                }
                R.id.course_nav -> {
                    loadFragment(CourseFragment())
                }
                R.id.home_nav -> {
                    loadFragment(HomeFragment())
                }
            }
            true
        }




        val intenttest:Intent=intent
        val data = intenttest.data
        if (data != null) {
            ZarinPal.getPurchase(this).verificationPayment(data) { isPaymentSuccess, refID, paymentRequest ->
                if (isPaymentSuccess) {
                    //een ja bayad save beshe be een ke taraf ya accounte golden ya acc fullacess
                    alertDialog()
                    val df: DateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
                    val date: String = df.format(Calendar.getInstance().time)
                    when(reStoreTypeForVerifyAccType()){
                        1 ->{
                            Hawk.put("fullAccount",20)
                            Hawk.put("payNumber",refID)
                            Hawk.put("payTime",date)
                            savePaymentInfo(Hawk.get("userPhone"),refID,1)
                        }
                        2 ->{
                            Hawk.put("goldenAccount",10)
                            Hawk.put("payNumber",refID)
                            Hawk.put("payTime",date)
                            savePaymentInfo(Hawk.get("userPhone"),refID,2)
                        }
                    }
                } else {
                    Toast.makeText(this, resources.getString(R.string.FailedPaymentAlert), Toast.LENGTH_SHORT).show()
                }
            }
        }





    }











    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()

    }


    private fun alertDialog() {
        val builder = AlertDialog.Builder(this).create()
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alertdialog_success_peyment, null)
        val btn = dialogLayout.findViewById<TextView>(R.id.btn_succes_payment)

        btn.setOnClickListener { builder.dismiss() }

        builder.setView(dialogLayout)
        builder.show()
    }


    private fun savePaymentInfo(userPhone:String,paymentNumber:String,acc_type:Int){
        val discountCode=getDiscountCode()
        val apiService: ApiInterface = ApiClient.getApiClient(Hawk.get("Unknown"))!!.create(ApiInterface::class.java)
        val call: Call<Void> = apiService.savePaymentInfo(userPhone,paymentNumber,discountCode!!,acc_type)
        call.enqueue(object : Callback<Void> {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, resources.getString(R.string.CantAccessToNet), Toast.LENGTH_SHORT).show()

            }

        })


    }




    override fun onBackPressed() {

        val fragmentList=supportFragmentManager.fragments
        var backHanlde = false
        for (fragment in fragmentList){
            if (fragment is BaseFragment){
                backHanlde=fragment.onBackPressed()
                if (backHanlde){
                    break
                }
            }
        }
        if(!backHanlde) {
            super.onBackPressed()
        }
    }



    private fun reStoreTypeForVerifyAccType():Int{
        val sharedpref =this.getSharedPreferences("afterPay", Context.MODE_PRIVATE)
        return sharedpref.getInt("type", 0)
    }


    private fun getDiscountCode(): String? {
        val sharedpref =this.getSharedPreferences("discountCode", Context.MODE_PRIVATE)
        return sharedpref.getString("code", "null")
    }


}





