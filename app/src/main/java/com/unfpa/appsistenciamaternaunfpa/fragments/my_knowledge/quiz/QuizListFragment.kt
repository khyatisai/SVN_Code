package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.quiz.QuizListAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import kotlinx.android.synthetic.main.fragment_quiz_list.*

/**
 * Created by KhyatiShah on 9/26/2019.
 */
class QuizListFragment : androidx.fragment.app.Fragment() {

    lateinit var categoryId: String
    lateinit var categoryName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_quiz_list, container, false)
        try {
            setHasOptionsMenu(true)
            categoryId = this.arguments!!.getString("categoryId").toString()
            categoryName = this.arguments!!.getString("categoryName").toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = categoryName
            val quizRequestDAO = MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.quizRequestDAO()
            val listQuiz = quizRequestDAO.getQuizCategoryWise(categoryId)
            //val listQuiz = quizRequestDAO.getAllQuiz()
            val lstQuiz: androidx.recyclerview.widget.RecyclerView = view.findViewById(R.id.lstQuiz)
            lstQuiz.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)
            val adapter = QuizListAdapter(activity!!)
            lstQuiz.adapter = adapter
            adapter.setQuizList(listQuiz)
            if (listQuiz.isEmpty()) {
                lstQuiz.visibility = View.GONE
                txtNoItems.visibility = View.VISIBLE
            } else {
                lstQuiz.visibility = View.VISIBLE
                txtNoItems.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.home).isVisible = true
        }
    }
}