package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.models.QuestionResponse
import org.json.JSONArray


class QuizQuestionAdapter() :
    androidx.recyclerview.widget.RecyclerView.Adapter<QuizQuestionAdapter.QuizQuestionViewHolder>() {

    private lateinit var context: Context
    private lateinit var quizList: JSONArray
    private val lstResponse: MutableList<QuestionResponse> = mutableListOf<QuestionResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizQuestionViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        context = parent.context
        return QuizQuestionViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: QuizQuestionViewHolder, position: Int) {
        val questionJson = quizList.getJSONObject(position)

        //Question text
        holder.textViewName.text = questionJson.get("title").toString()

        //setting response
        val response = QuestionResponse(questionJson.get("id").toString(), false)
        lstResponse.add(response)

        //adding options
        val arrOptions = questionJson.getJSONArray("options")
        for (i in 0 until arrOptions.length()) {
            val radioButton = RadioButton(context)
            val params =
                RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            radioButton.setButtonDrawable(R.drawable.selector_radio_button_quiz)
            params.setMargins(0, 15, 0, 15)
            radioButton.setLayoutParams(params)
            radioButton.setPadding(15, 15, 0, 15)
            radioButton.compoundDrawablePadding = 15
            radioButton.tag = arrOptions.getJSONObject(i).getString("id")
            radioButton.text = arrOptions.getJSONObject(i).getString("title")
            holder.radioGroupOptions.addView(radioButton)
            holder.radioGroupOptions.setOnCheckedChangeListener { group, checkedId ->
                val selectedButton: RadioButton = group.findViewById(checkedId)
                val idCorrectAns = questionJson.getJSONObject("answer").getString("id")
                //lstResponse[position].isTrue = (selectedButton.tag) == idCorrectAns  //simplify version of below code
                if ((selectedButton.tag).equals(idCorrectAns)) {
                    lstResponse.get(position).isTrue = true
                } else {
                    lstResponse.get(position).isTrue = false
                }
                //Toast.makeText(context, lstResponse.get(position).isTrue.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return quizList.length()
    }

    class QuizQuestionViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val textViewName = itemView.findViewById(R.id.txtQuestionName) as TextView
        val radioGroupOptions = itemView.findViewById(R.id.radioGroupOptions) as RadioGroup

    }

    fun setQuizList(quizList: JSONArray) {
        this.quizList = quizList
        notifyDataSetChanged()
    }
   

    fun getResponse(): List<QuestionResponse> {
        return lstResponse
    }
}
