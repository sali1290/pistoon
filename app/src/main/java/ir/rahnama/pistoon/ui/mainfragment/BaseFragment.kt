package ir.rahnama.pistoon.ui.mainfragment

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {


    //overwriting back press in all fragment
    open fun onBackPressed(): Boolean {
        return false
    }



}