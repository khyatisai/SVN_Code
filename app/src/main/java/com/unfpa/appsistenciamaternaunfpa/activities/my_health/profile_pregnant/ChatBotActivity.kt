package com.unfpa.appsistenciamaternaunfpa.activities.my_health.profile_pregnant

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.codepalace.chatbot.utils.Time
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.chatbot.MessagingAdapter
import com.unfpa.appsistenciamaternaunfpa.models.MessageBot
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.bot.BotResponse
import com.unfpa.appsistenciamaternaunfpa.utils.bot.Constants
import kotlinx.android.synthetic.main.activity_bot_message.*
import kotlinx.coroutines.*
import java.lang.Exception

class ChatBotActivity : AppCompatActivity() {
    private val TAG = "ChatBotActivity"

    //You can ignore this messageList if you're coming from the tutorial,
    // it was used only for my personal debugging
    var messagesList = mutableListOf<MessageBot>()
    private var toolbar: Toolbar? = null

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Un Asistente Virtual de Profamilia.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot_message)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Asistente Virtual"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })
        val MyNameUser = AppUtils.getDataCompleteNomUser(this)

        customBotMessage("¡Hola $MyNameUser! Hoy estás hablando con ${botList[0]}\r\n \r\n" +
                "Puedes seleccionar una de las siguientes opciones para obtener tu respuesta.\r\n" )

        recyclerView()

        clickEvents()

    }

    private fun clickEvents() {

        //Send a message
        btn_send.setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter(this)
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(MessageBot(message, Constants.SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(MessageBot(message, Constants.SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }
    public fun sendMessage2(messageFromClick: String) {
        val message = messageFromClick
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(MessageBot(message, Constants.SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(MessageBot(message, Constants.SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun callPhone(field_contact_number: String) {
        try {
            val i = Intent(Intent.ACTION_DIAL)
            val number = "0123456789"
            i.data = Uri.parse("tel:$field_contact_number")
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)

                //Adds it to our local list
                messagesList.add(MessageBot(response, Constants.RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(MessageBot(response, Constants.RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                rv_messages.scrollToPosition(adapter.itemCount - 1)

                if(response.contains("Telefono:")){
                    callPhone("22701531")
                }

                //Starts Google
                when (response) {
                    Constants.OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    Constants.OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("buscar")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(MessageBot(message, Constants.RECEIVE_ID, timeStamp))
                adapter.insertMessage(MessageBot(message, Constants.RECEIVE_ID, timeStamp))

                messagesList.add(MessageBot("Horario de atención", Constants.RECEIVE_ID, timeStamp))
                adapter.insertMessage(MessageBot("Horario de atención", Constants.RECEIVE_ID, timeStamp))

                messagesList.add(MessageBot("Clínica de Profamilia", Constants.RECEIVE_ID, timeStamp))
                adapter.insertMessage(MessageBot("Clínica de Profamilia", Constants.RECEIVE_ID, timeStamp))

                messagesList.add(MessageBot("Llamar a Profamilia", Constants.RECEIVE_ID, timeStamp))
                adapter.insertMessage(MessageBot("Llamar a Profamilia", Constants.RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}