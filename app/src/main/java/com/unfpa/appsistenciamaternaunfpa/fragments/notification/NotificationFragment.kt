package com.unfpa.appsistenciamaternaunfpa.fragments.notification

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.patient.NotificationAdapter
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import kotlinx.android.synthetic.main.fragment_notification.*
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.custom_toast_layout.*
import org.json.JSONObject
import java.util.ArrayList


/**
 * Created by KhyatiShah on 12/13/2019.
 */
class NotificationFragment : androidx.fragment.app.Fragment() {

    private val p = Paint()
    val getNotification = EndPoints.URL_HEROKU + "/api/v1/notification"
    var title: ArrayList<String> = ArrayList()
    var text: ArrayList<String> = ArrayList()
    var hour: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_notification, container, false)
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.Notifications)

            llProgressBarNotification.visibility = View.VISIBLE

            val jsonobj2 = JSONObject()

            val getMyUserId = AppUtils.getDataUser(this!!.activity!!)

            jsonobj2.put("userid", getMyUserId)

            val quee = Volley.newRequestQueue(this!!.activity!!)
            val reqq = JsonObjectRequest(
                Request.Method.POST, getNotification, jsonobj2,
                Response.Listener { response ->

                    var dataString  = response.getString("notifications")
                    if (dataString != "") {
                        var dataArray  = response.getJSONArray("notifications")
                        lstNotification.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this!!.activity!!)

                        for (i in 0 until dataArray.length()) {
                            title.add(dataArray.getJSONObject(i).getString("title"))
                            text.add(dataArray.getJSONObject(i).getString("text"))
                            hour.add(dataArray.getJSONObject(i).getString("hour"))
                        }

                        linearLayoutNoArticle.visibility = View.GONE
                        lstNotification.visibility = View.VISIBLE
                        val customAdapter = NotificationAdapter(this!!.activity!!, title, text, hour)
                        lstNotification.adapter = customAdapter


                    } else {
                        linearLayoutNoArticle.visibility = View.VISIBLE
                        lstNotification.visibility = View.GONE
                    }

                    llProgressBarNotification.visibility = View.GONE

                },
                Response.ErrorListener { error ->
                    val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
                    context?.let { it1 ->
                        AppUtils.createCustomToast(AppUtils.getVolleyError(error, this!!.activity!!), it1, layoutToast, "warning")
                    }
                    llProgressBarNotification.visibility = View.GONE
                    Log.d("error","validando error"+error)
                })
            quee.add(reqq)
            //val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            // itemTouchHelper.attachToRecyclerView(lstNotification)
        } catch (e: Exception) {
            e.printStackTrace()
            llProgressBarNotification.visibility = View.GONE
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

    fun showNoItemLayout() {
        linearLayoutNoArticle.visibility = View.VISIBLE
        lstNotification.visibility = View.GONE
    }
    /*var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT
        //or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // Toast.makeText(activity, "on Move", Toast.LENGTH_SHORT).show()
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            Toast.makeText(activity, "on Swiped ", Toast.LENGTH_SHORT).show()
            val position = viewHolder.adapterPosition

            if (direction == ItemTouchHelper.LEFT) {
                *//* val deletedModel = imageModelArrayList!![position]
                 adapter!!.removeItem(position)*//*
                // showing snack bar with Undo option
                val snackbar = Snackbar.make(
                    this@NotificationFragment!!.activity!!.window.decorView.rootView,
                    " removed from Recyclerview!",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("UNDO") {
                    // undo is selected, restore the deleted item
                    // adapter!!.restoreItem(deletedModel, position)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            } else {
                *//*val deletedModel = imageModelArrayList!![position]
                adapter!!.removeItem(position)*//*
                // showing snack bar with Undo option
                val snackbar = Snackbar.make(
                    this@NotificationFragment!!.activity!!.window.decorView.rootView,
                    " removed from Recyclerview!",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("UNDO") {
                    // undo is selected, restore the deleted item
                    // adapter!!.restoreItem(deletedModel, position)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            val icon: Bitmap
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                val itemView = viewHolder.itemView
                val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                val width = height / 3

                if (dX > 0) {
                    p.color = ContextCompat.getColor(this@NotificationFragment!!.activity!!, R.color.white);
                    val background =
                        RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                    c.drawRect(background, p)
                    icon = BitmapFactory.decodeResource(resources, R.drawable.ic_del)
                    val icon_dest = RectF(
                        itemView.left.toFloat() + width,
                        itemView.top.toFloat() + width,
                        itemView.left.toFloat() + 2 * width,
                        itemView.bottom.toFloat() - width
                    )
                    c.drawBitmap(icon, null, icon_dest, p)
                } else {
                    p.color = ContextCompat.getColor(this@NotificationFragment!!.activity!!, R.color.white);
                    val background = RectF(
                        itemView.right.toFloat() + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    )
                    c.drawRect(background, p)
                    icon = BitmapFactory.decodeResource(resources, R.drawable.ic_del)
                    val icon_dest = RectF(
                        itemView.right.toFloat() - 2 * width,
                        itemView.top.toFloat() + width,
                        itemView.right.toFloat() - width,
                        itemView.bottom.toFloat() - width
                    )
                    c.drawBitmap(icon, null, icon_dest, p)
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }


    }
*/

}