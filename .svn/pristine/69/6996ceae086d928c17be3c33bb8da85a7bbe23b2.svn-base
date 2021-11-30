package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.QuizRequest
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.quiz.QuizFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.item_quiz_list.view.*
import org.json.JSONObject

/**
 * Created by KhyatiShah on 9/26/2019.
 */
class QuizListAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var quizList: List<QuizRequest>
    private var activity: androidx.fragment.app.FragmentActivity = activity
    val quizResponseDAO = MhealthRoomDatabase.getAppDataBase(activity!!)!!.quizResponseDAO()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return QuizListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_quiz_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val quizViewHolder = viewHolder as QuizListViewHolder
        quizViewHolder.bindView(quizList.get(position))
    }

    override fun getItemCount(): Int = quizList.size
    inner class QuizListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(quiz: QuizRequest) {
            val quizJSON = JSONObject(quiz.jsonQuiz)
            itemView.txtQuizName.text = quizJSON.getString("title")
            itemView.txtQuizDesc.text = quizJSON.getString("field_description_export")
            val allQuizResponse = quizResponseDAO.getAllResponse(quizId = quiz.quizId, categoryId = quiz.categoryId)

            if (allQuizResponse.isEmpty()) {
                itemView.txtNumberOfAttempt.text = "0"
                itemView.txtTakeQuiz.text = activity.getString(R.string.TakeQuiz)
                itemView.txtNoResult.visibility = View.VISIBLE
                itemView.txtResultPass.visibility = View.GONE
                itemView.txtResultFailed.visibility = View.GONE
            } else {
                itemView.txtNumberOfAttempt.text = allQuizResponse.size.toString()
                itemView.txtTakeQuiz.text = activity.getString(R.string.TryAgain)
                for (i in 0 until allQuizResponse.size) {
                    if (allQuizResponse.get(i).result == 1) {
                        itemView.txtNoResult.visibility = View.GONE
                        itemView.txtResultPass.visibility = View.VISIBLE
                        itemView.txtResultFailed.visibility = View.GONE
                        break
                    } else {
                        itemView.txtNoResult.visibility = View.GONE
                        itemView.txtResultPass.visibility = View.GONE
                        itemView.txtResultFailed.visibility = View.VISIBLE
                    }
                }
            }
            itemView.llParent.setOnClickListener {
                //Logging firebase screen
                AppUtils.trackScreen(Constant.USER_QUIZ_TAKE, activity!!)
                var bundle = Bundle()
                bundle.putString("questionId", quiz.quizId)
                bundle.putString("categoryId", quiz.categoryId)
                bundle.putString("quizName", quizJSON.getString("title"))
                bundle.putString("rawJSON", quizJSON.toString())
                var frag = QuizFragment()
                frag.arguments = bundle
                AppUtils.addFragment(activity, frag, true, "")
            }
        }
    }

    fun setQuizList(quizList: List<QuizRequest>) {
        this.quizList = quizList
        notifyDataSetChanged()
    }
}