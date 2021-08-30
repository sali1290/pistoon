package ir.rahnama.pistoon.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rahnama.pistoon.R
import ir.rahnama.pistoon.adapter.SavedQuizShowAdapter
import ir.rahnama.pistoon.data.MyDatabase
import ir.rahnama.pistoon.model.AllTestModel


class QuestionSavedFragment : Fragment() , SavedQuizShowAdapter.DeleteSavedQuiz {


    private lateinit var db: MyDatabase
    private lateinit var adapter:SavedQuizShowAdapter
    private lateinit var data:MutableList<AllTestModel>
    private lateinit var recycler_view:RecyclerView
    private lateinit var txt_empty:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question_saved, container, false)


         txt_empty=view.findViewById(R.id.txt_empty)
         recycler_view = view.findViewById(R.id.saved_quiz_recycler)
         db = MyDatabase(requireContext())



        loadData()



        return view
    }

    override fun DeleteSavedQuizId(quizid: String) {
        db.deleteSavedQuiz(quizid)
        loadData()
    }


    private fun loadData() {
        data = db.getSavedQuiz()
        adapter = SavedQuizShowAdapter(requireActivity(),data,this)
        recycler_view.adapter=adapter
        recycler_view.layoutManager= LinearLayoutManager(requireActivity())
        adapter.notifyDataSetChanged()

        if (data.size > 0){
            txt_empty.visibility=View.GONE
            recycler_view.visibility=View.VISIBLE
        }else{
            txt_empty.visibility=View.VISIBLE
            recycler_view.visibility=View.GONE
        }


    }

}