package com.marias.countdown.screens.eventlist

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.marias.countdown.R
import com.marias.countdown.data.CountdownEvent
import com.marias.countdown.screens.newewent.NewEvent

import java.util.*

class EventListAdapter(
    val context: Context,
    var eventList: MutableList<CountdownEvent>,
    private val listener: Callback
) :
    RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private lateinit var recentlyDeletedItem: CountdownEvent
    private var recentlyDeletedItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount() = eventList.size

    fun removeAt(position: Int, view :CoordinatorLayout) {
        listener.deleteEvent(eventList[position])
        recentlyDeletedItem = eventList[position]
        recentlyDeletedItemPosition = position
        eventList.remove(eventList[position])
        showUndoSnackBar(view)
        notifyItemRemoved(position)
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CountdownEvent) {
            val title = itemView.findViewById<TextView>(R.id.eventTitleTextView)
            val countdown = itemView.findViewById<TextView>(R.id.eventCountdownTextView)
            title.text = item.title
            val currentTime = Calendar.getInstance()
            val targetTime = item.time - currentTime.timeInMillis
            val timer = object : CountDownTimer(targetTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val day = millisUntilFinished / (24 * 60 * 60 * 1000)
                    val hour = (millisUntilFinished % (24 * 60 * 60 * 1000)) / (1000 * 60 * 60)
                    val minute = (millisUntilFinished % (60 * 60 * 1000)) / (1000 * 60)
                    val second = (millisUntilFinished % (60 * 1000)) / 1000
                    val dayText =
                        context.resources.getQuantityText(R.plurals.days, day.toInt()).toString()
                    countdown.text = "$day $dayText  $hour : $minute : $second"
                }

                override fun onFinish() {
                    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(item.title)
                        .setContentText(context.getText(R.string.time_is_coming))
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_countdown_finished)
                    val mainIntent = Intent(context.applicationContext, MainActivity::class.java)
                    val activity =
                        PendingIntent.getActivity(
                            context.applicationContext,
                            MAIN_ACTIVITY_REQUEST_CODE,
                            mainIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    builder.setContentIntent(activity)
                    val newEventIntent = Intent(context.applicationContext, NewEvent::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        context.applicationContext,
                        NEW_EVENT_ACTIVITY_REQUEST_CODE,
                        newEventIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                    builder.addAction(
                        R.drawable.ic_add_black,
                        context.getString(R.string.add_new_event),
                        pendingIntent
                    )
                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(NOTIFICATION_ID, builder.build())
                    countdown.text = context.getText(R.string.time_is_coming)
                }
            }
            timer.start()
        }
    }
    private fun showUndoSnackBar(view :CoordinatorLayout){
        val undoSnackBar = Snackbar.make(view, context.getText(R.string.want_undo_delete), Snackbar.LENGTH_LONG)
        undoSnackBar.setAction(R.string.undo) { undoDelete() }
        undoSnackBar.show()
    }
    private fun undoDelete(){
        eventList.add(recentlyDeletedItemPosition, recentlyDeletedItem)
        listener.undoDelete(recentlyDeletedItem)
        notifyDataSetChanged()
    }

    companion object {
        const val CHANNEL_ID = "COUNTDOWN_CHANNEL_ID"
        const val MAIN_ACTIVITY_REQUEST_CODE = 1
        const val NEW_EVENT_ACTIVITY_REQUEST_CODE = 2
        const val NOTIFICATION_ID = 0
    }
}