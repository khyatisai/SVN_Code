package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.quiz


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout

import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.quiz.QuizQuestionAdapter
import com.unfpa.appsistenciamaternaunfpa.api_controller.API_Controller
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.QuizResponse
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.popup_quiz_result.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class QuizFragment : androidx.fragment.app.Fragment(), Listener {


    lateinit var quizId: String
    lateinit var categoryId: String
    lateinit var quizName: String
    lateinit var adapter: QuizQuestionAdapter
    lateinit var quizJSON: JSONObject
    lateinit var respnseQuiz: JSONObject
    var correctCount = 0
    var totalCount = 0
    var boolResult = 0
    lateinit var lstQuizQuestionsRecycler: androidx.recyclerview.widget.RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_quiz, container, false)
        try {
            setHasOptionsMenu(true)
            quizId = this.arguments!!.getString("questionId").toString()
            categoryId = this.arguments!!.getString("categoryId").toString()
            quizName = this.arguments!!.getString("quizName").toString()
            quizJSON = JSONObject(this.arguments!!.getString("rawJSON"))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = quizName

            setQuestion(view)
            btnDone.setOnClickListener {
                //Toast.makeText(activity, "Under development....", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.VISIBLE
                //Logging firebase screen
                AppUtils.trackScreen(Constant.USER_QUIZ_RESPOND, activity!!)
                submitQuiz()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setQuestion(rootView: View) {
        try {
            val quizRequestDAO = MhealthRoomDatabase.getAppDataBase(activity!!.applicationContext)!!.quizRequestDAO()
            val quizRequest = quizRequestDAO.getQuizQuizIdWise(categoryId, quizId)
            val jsonQuiz = JSONObject(quizRequest.jsonQuiz)
            val jsonQuestionList: JSONArray = jsonQuiz.getJSONArray("field_srh_quiz_question_export")

            lstQuizQuestionsRecycler = rootView.findViewById(R.id.lstQuizQuestionsRecycler)
            lstQuizQuestionsRecycler.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)
            adapter = QuizQuestionAdapter()
            lstQuizQuestionsRecycler.adapter = adapter

            adapter.setQuizList(jsonQuestionList)

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

    fun submitQuiz() {
        try {
            correctCount = 0
            respnseQuiz = JSONObject()

            var correctAnsId = JSONArray()
            val listResponse = adapter.getResponse()
            for (i in 0 until listResponse.size) {
                if (listResponse.get(i).isTrue) {
                    correctCount += 1
                    correctAnsId.put(listResponse.get(i).questionId)
                }
            }
            //passing criteria
            val passingPer = quizJSON.getString("field_passing_criteria").toInt()
            totalCount = quizJSON.getJSONArray("field_srh_quiz_question_export").length()
            val marksSecured = (correctCount * 100) / totalCount

            if (passingPer <= marksSecured) {
                boolResult = 1
            }
            val sharedPreference = activity!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            respnseQuiz.put("title", JSONObject().put("value", quizName))
            respnseQuiz.put("field_srh_quiz", JSONObject().put("value", quizId))
            respnseQuiz.put(
                "field_respondent_age_group",
                JSONObject().put("value", sharedPreference.getString(Constant.PREF_AGE_GROUP, ""))
            )
            respnseQuiz.put(
                "field_respondent_country",
                JSONObject().put("value", sharedPreference.getString(Constant.PREF_COUNTRY, ""))
            )
            /*respnseQuiz.put(
                "field_respondent_unique_id",
                JSONObject().put("value", "6b9cb3b7-3519-4fe1-acf7-69183581270c")
            )*/
            respnseQuiz.put(
                "field_respondent_unique_id",
                JSONObject().put("value", AppUtils.getUUID(activity?.applicationContext!!))
            )
            respnseQuiz.put(
                "field_respondent_gender",
                JSONObject().put("value", sharedPreference.getString(Constant.PREF_GENDER, ""))
            )
            respnseQuiz.put("field_quiz_status", JSONObject().put("value", boolResult).put("format", "boolean"))
            respnseQuiz.put("field_srh_quiz_question", JSONObject().put("value", correctAnsId))
            //API call
            API_Controller.postWithToken(context!!.applicationContext, respnseQuiz, EndPoints.URL_POST_QUIZ, this)

        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            e.printStackTrace()
        }
    }

    override fun onComplete(response: String) {

        val quizResponseDAO = MhealthRoomDatabase.getAppDataBase(context!!)!!.quizResponseDAO()
        if (response != null || !TextUtils.isEmpty(response)) {
            quizResponseDAO.insert(QuizResponse(categoryId, quizId, respnseQuiz.toString(), 1, boolResult))
        } else {
            quizResponseDAO.insert(QuizResponse(categoryId, quizId, respnseQuiz.toString(), 0, boolResult))
        }
        progressBar.visibility = View.GONE
        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.popup_quiz_result)
            dialog.txtScore.text = correctCount.toString() + " / " + totalCount.toString()
        }

        if (boolResult == 1) {
            if (dialog != null) {
                dialog.txtFaliled.visibility = View.INVISIBLE
                dialog.txtPassed.visibility = View.VISIBLE
                dialog.btnTryAgain.visibility = View.GONE
                dialog.imgQuizResult.setImageResource(R.drawable.ic_quiz_passed_big)
            }

        } else {
            if (dialog != null) {
                dialog.txtFaliled.visibility = View.VISIBLE
                dialog.txtPassed.visibility = View.INVISIBLE
                dialog.btnTryAgain.visibility = View.VISIBLE
                dialog.imgQuizResult.setImageResource(R.drawable.ic_quiz_failed_big)
            }

        }
        if (dialog != null) {
            dialog.txtClose.setOnClickListener {
                dialog.dismiss()
                getActivity()?.onBackPressed();
            }
            dialog.btnTryAgain.setOnClickListener {
                if (dialog != null) {
                    dialog.dismiss()
                }
                /*  val manager = activity?.supportFragmentManager
                  val ft = manager?.beginTransaction()
                  ft?.detach(this)?.attach(this)?.commit();*/
                lstQuizQuestionsRecycler.setAdapter(null);
                lstQuizQuestionsRecycler.setLayoutManager(null);
                lstQuizQuestionsRecycler.getRecycledViewPool().clear();
                lstQuizQuestionsRecycler.swapAdapter(adapter, false);
                lstQuizQuestionsRecycler.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)
                adapter.notifyDataSetChanged()
            }
            dialog.show()
        }

    }

}
