package ir.rahnama.pistoon.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rahnama.pistoon.R


class AboutUsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_about_us, container, false)

        val btn_open_site=view.findViewById<Button>(R.id.open_site_btn)
        val btn_back=view.findViewById<ImageView>(R.id.btn_back_about_us)
        val bottom_nav=requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_nav.visibility=View.GONE

        btn_open_site.setOnClickListener{

            val url = "http://pistoon.iranswan.ir/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


        btn_back.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }


        return view
    }


}