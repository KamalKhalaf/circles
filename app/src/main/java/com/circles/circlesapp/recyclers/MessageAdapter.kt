package com.circles.circlesapp.recyclers

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.circles.circlesapp.R
import com.circles.circlesapp.chatlist.ChatRoom
import com.circles.circlesapp.helpers.core.Constants
import com.circles.circlesapp.helpers.utilities.ElapsedTime
import com.circles.circlesapp.messaging.view.MessagingActivity
import java.util.*


class MessageAdapter(private val messageLists: List<ChatRoom>) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        lateinit var imageView: ImageView
        lateinit var progressBarload: ProgressBar
        var time: TextView
        var lastMessage: TextView
        var rlContainer: RelativeLayout

        init {
            name = view.findViewById(R.id.userName)
            time = view.findViewById(R.id.time)
            imageView = view.findViewById(R.id.userImage)
            progressBarload = view.findViewById(R.id.progressBarload)
            lastMessage = view.findViewById(R.id.userLastMEssage)
            rlContainer = view.findViewById(R.id.rl_container)
        }
    }

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.messages, parent, false)

        context = parent.context

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chatRoom = messageLists[position]
        holder.name.text = chatRoom.title
        holder.time.text = ElapsedTime.getFromDate(Date(chatRoom.created_at))
        holder.lastMessage.text = chatRoom.last_message.message_body
        if (chatRoom.image != null && !chatRoom.image.equals("")) {
            Glide.with(context!!).load(chatRoom.image).listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    holder.progressBarload!!.setVisibility(View.GONE)
                    return false

                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    holder.progressBarload!!.setVisibility(View.GONE)
                    return false
                }
            }).apply(RequestOptions.circleCropTransform()).into(holder.imageView)

        } else {
            holder.progressBarload!!.setVisibility(View.GONE)
            Glide.with(context!!).load(R.drawable.sign_up_profile).into(holder.imageView)
        }
        holder.rlContainer.setOnClickListener {
            val intent = Intent(context, MessagingActivity::class.java)
            intent.putExtra(Constants.CHAT_ROOM_KEY, chatRoom)
            startActivity(this.context!!, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return messageLists.size
    }


}