package ir.rahnama.pistoon.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ir.rahnama.pistoon.R


class ScreenSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_splash)


     Handler().postDelayed( { moveToNext() },2200)


    }


    private fun moveToNext(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}