package ir.rahnama.pistoon.ui.font

import android.R
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


class Font : Application() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("shabnam.ttf")
                            .setFontAttrId(R.attr.font)
                            .build()
                    )
                )
                .build()
        )

    }


}