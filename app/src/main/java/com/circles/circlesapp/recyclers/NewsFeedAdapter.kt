package com.circles.circlesapp.recyclers

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.chibde.visualizer.LineBarVisualizer
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.AppConstants
import com.circles.circlesapp.helpers.MySeekBarCallback
import com.circles.circlesapp.helpers.SeekBarUtil
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor.userId
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener
import com.circles.circlesapp.helpers.utilities.ElapsedTime
import com.circles.circlesapp.home.NewsFeedData
import com.circles.circlesapp.loginsignup.LoginSignUpActivity
import com.circles.circlesapp.retrofit.RetrofitClient
import com.jsibbold.zoomage.ZoomageView
import java.security.AccessController.getContext
import java.util.*
import kotlin.math.log


class NewsFeedAdapter(private val items: List<NewsFeedData>, val activity: Context, val context: Context, val listener: CustomOnClickListener<NewsFeedData>)
    : RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder>(), MySeekBarCallback {


    var tokenType = MyServiceInterceptor.tokenType

    var accessToken = MyServiceInterceptor.accessToken
    var viewHolder: MyViewHolder? = null
    lateinit var imageZoom: ZoomageView
    lateinit var dialog: Dialog
    lateinit var currentSeekBar: SeekBar
    lateinit var visualizerrr: LineBarVisualizer
    lateinit var currentPlay: ImageView
    lateinit var currentPause: ImageView
    lateinit var currentSeekTv: TextView
    private var currentPostId: Int = 0;
    private var currentSeekBarPosition: Int = 0;
    private var currentHeardCount: String = ""

//    internal var numOfItem = -1
//    internal var numOfItem2 = -1


    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.post_item, parent, false)
        viewHolder = MyViewHolder(view)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: NewsFeedAdapter.MyViewHolder, position: Int) {
        val feedData = items[position]


//        if ((position == numOfItem) and (position != numOfItem2)){
//
//
//            holder.play.visibility = GONE
//            holder.pause.visibility = VISIBLE
//
//        }else{
//            holder.play.visibility = VISIBLE
//            holder.pause.visibility = GONE
//        }


//        Glide.with(context).asBitmap().load(feedData.image).into(holder.postImage)
        if (feedData.image == null || feedData.image.equals("")) {
            holder.postImage.setBackgroundColor(context.resources.getColor(R.color.gray))
        } else {
            Glide.with(context).load(feedData.image).listener(object : com.bumptech.glide.request.RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    holder.postImage.setBackgroundColor(context.resources.getColor(R.color.gray))
                    return false

                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: com.bumptech.glide.load.DataSource, isFirstResource: Boolean): Boolean {

                    return false
                }
            }).into(holder.postImage)
        }

        if (AppConstants.post_from_splash != null && AppConstants.post_from_splash != 0) {

            holder.mainCard.setBackgroundColor(context.resources.getColor(R.color.gray))
            holder.btnEditOrDeletePost.setBackgroundColor(context.resources.getColor(R.color.gray))
            AppConstants.post_from_splash = 0

            Handler().postDelayed({
                holder.mainCard.setBackgroundColor(context.resources.getColor(R.color.white))
                holder.btnEditOrDeletePost.setBackgroundColor(context.resources.getColor(R.color.white))
            }, 2500)
        }

        holder.seekBar.progress = 0;
        holder.play.visibility = VISIBLE
        holder.pause.visibility = GONE
        holder.userName.text = feedData.fullName
        if (feedData.title == null) {
            holder.audioTitleTv.visibility = GONE
        } else {
            holder.audioTitleTv.text = feedData.title
        }
        holder.text.text = feedData.text
        holder.commentNumber.text = feedData.comments_count
        holder.likeNumber.text = feedData.likes.toString()
        holder.sharedNumber.text = feedData.shares_count
        holder.hearedNumber.text = feedData.heard_count
        holder.time_stamp.text = ElapsedTime.getFromDate(Date(feedData.createdAt))
        if (feedData.isLike) {
            holder.like.setImageDrawable(context.resources.getDrawable(R.drawable.liked))
        } else {
            holder.like.setImageDrawable(context.resources.getDrawable(R.drawable.like))
        }
        if (feedData.isShared) {
            holder.share.setImageDrawable(context.resources.getDrawable(R.drawable.shared))
            holder.sharedLinearLayout.visibility = View.VISIBLE
            holder.sharedUserName.text = feedData.sharedFrom.username
            Glide.with(context).asBitmap().load(feedData.sharedFrom.profileImage).into(holder.shareduserImageVew)
        } else {
            holder.share.setImageDrawable(context.resources.getDrawable(R.drawable.share))
            holder.sharedLinearLayout.visibility = View.GONE
        }
        if (feedData.userId == MyServiceInterceptor.userId) {
            holder.btnEditOrDeletePost.visibility = VISIBLE
        } else {
            holder.btnEditOrDeletePost.visibility = GONE
        }

        holder.btnEditOrDeletePost.setOnClickListener {
            listener.onclick(it, feedData)
        }

//        holder.itemProfile.setOnClickListener(View.OnClickListener {
//
//            BroadcastHelper.sendInform(activity, "get_profile_data", feedData.userId.toString())
//        })
//        holder.itemProfile.setOnClickListener {
//            listener.onclick(it, feedData)
//        }
//        if (position == 0 && feedData.image != null && !feedData.image.equals("null"))
//            getDataListner.getData(feedData.image as String?)

        if (feedData.image != null && !feedData.image.equals("")) {

            holder.postImage.setOnClickListener(View.OnClickListener {

                val dialog = Dialog(activity, R.style.coupon_Dialog);
                dialog.setContentView(R.layout.dialog_zoom_in);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (!dialog.isShowing()) {
                    dialog.show();
                }

                val imageZoom = dialog.findViewById<ZoomageView>(R.id.myZoomageView)
                val progressBarload = dialog.findViewById<ProgressBar>(R.id.progressBarload)
                Glide.with(context)
                        .load(feedData.image)
                        .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {

                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                progressBarload.visibility = View.GONE
                                return false

                            }

                            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: com.bumptech.glide.load.DataSource, isFirstResource: Boolean): Boolean {
                                progressBarload.visibility = View.GONE
                                return false
                            }
                        }).into(imageZoom)

                val textView = dialog.findViewById<TextView>(R.id.close)
                textView.setOnClickListener { v1 ->
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }

            })
        }

        if (feedData.profileImage != null && !feedData.profileImage.equals("")) {
            Glide.with(context).asBitmap().load(feedData.profileImage).into(holder.userImage)

        } else {
            Glide.with(context)
                    .load(R.drawable.sign_up_profile)
                    .into(holder.userImage)
        }

        holder.visualizerrr.setColor(context.resources.getColor(R.color.av_dark_blue))
        holder.visualizerrr.setDensity(90f)
        holder.like.setOnClickListener {
            if (feedData.isLike) {
                holder.like.setImageDrawable(context.resources.getDrawable(R.drawable.like))
            } else {
                holder.like.setImageDrawable(context.resources.getDrawable(R.drawable.liked))
            }
            listener.onclick(it, feedData)
        }
        holder.share.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/html"
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, feedData.link)
            activity.startActivity(Intent.createChooser(sharingIntent, context.resources.getString(R.string.share)))
        }

        holder.visualizerrr.setColor(ContextCompat.getColor(context, R.color.av_dark_blue))
        holder.visualizerrr.setDensity(90f)
        holder.play.setOnClickListener {
            currentPostId = feedData.id;
            currentHeardCount = feedData.heard_count;
            currentSeekBarPosition = position;
            setupSeekBarUtil(holder).startmediaPlayer(feedData.voiceNote, holder.visualizerrr)
//            numOfItem = position
            holder.play.visibility = GONE
            holder.pause.visibility = VISIBLE
//            notifyDataSetChanged()

        }
        holder.pause.setOnClickListener {
            setupSeekBarUtil(holder).pauseMediaPlayer()
            holder.play.visibility = VISIBLE
            holder.pause.visibility = GONE

        }
        holder.addVoiceRecord.setOnClickListener {

            setupSeekBarUtil(holder).pauseMediaPlayer()
            listener.onclick(it, feedData)

        }
        holder.addCommentText.setOnClickListener {
            setupSeekBarUtil(holder).pauseMediaPlayer()
            listener.onclick(it, feedData)
        }
        holder.userImage.setOnClickListener { listener.onclick(it, feedData) }


    }

    private fun startmediaPlayer(voiceNote: String) {


    }

    private fun setupSeekBarUtil(holder: MyViewHolder): SeekBarUtil {
        currentSeekBar = holder.seekBar;
        visualizerrr = holder.visualizerrr;
        currentPlay = holder.play;
        currentPause = holder.pause;
        currentSeekTv = holder.timePLayed;
        val seekBarUtil: SeekBarUtil = SeekBarUtil.newInstance(holder.seekBar.toString(), this);
        return seekBarUtil
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var voice_path: String
        var userName: TextView
        var text: TextView
        var time_stamp: TextView
        var commentNumber: TextView
        var likeNumber: TextView
        var sharedNumber: TextView
        var hearedNumber: TextView
        var audioTitleTv: TextView
        var timePLayed: TextView
        var play: ImageView
        var pause: ImageView
        var share: ImageButton
        lateinit var sharelayout: ConstraintLayout
        var shareduserImageVew: ImageView
        var btnEditOrDeletePost: LinearLayout
        var postImage: ImageView
//        var progressBarload: ProgressBar
        var userImage: ImageView
        lateinit var itemProfile: RelativeLayout
        var sharedUserName: TextView
        var sharedLinearLayout: LinearLayout
        var seekBar: SeekBar
        var visualizerrr: LineBarVisualizer
        var like: ImageButton
        var addVoiceRecord: ImageButton
        var addCommentText: ImageButton
        var playNoteCard: CardView
        var mainCard: LinearLayout

        init {
            userImage = view.findViewById(R.id.userImage) as ImageView
            itemProfile = view.findViewById(R.id.itemProfile) as RelativeLayout
//            progressBarload = view.findViewById(R.id.progressBarload) as ProgressBar
            userName = view.findViewById(R.id.userName) as TextView
            text = view.findViewById(R.id.text_post) as TextView
            time_stamp = view.findViewById(R.id.time_stamp) as TextView
            audioTitleTv = view.findViewById(R.id.audioTitleTv) as TextView
            timePLayed = view.findViewById(R.id.timePLayed) as TextView
            playNoteCard = view.findViewById(R.id.play_note_card) as CardView
            mainCard = view.findViewById(R.id.mainCard) as LinearLayout
            commentNumber = view.findViewById(R.id.comment_number) as TextView
            likeNumber = view.findViewById(R.id.like_number) as TextView
            sharedNumber = view.findViewById(R.id.shares_number) as TextView
            hearedNumber = view.findViewById(R.id.heared_number) as TextView
            play = view.findViewById(R.id.play) as ImageView
            pause = view.findViewById(R.id.pause) as ImageView
            seekBar = view.findViewById(R.id.seekbar) as SeekBar
            visualizerrr = view.findViewById(R.id.visualizerrr) as LineBarVisualizer
            like = view.findViewById(R.id.like) as ImageButton
            share = view.findViewById(R.id.share) as ImageButton
            sharelayout = view.findViewById(R.id.sharelayout) as ConstraintLayout
            sharedLinearLayout = view.findViewById(R.id.sharedLinearLayout) as LinearLayout
            btnEditOrDeletePost = view.findViewById(R.id.btnEditOrDeletePost) as LinearLayout
            shareduserImageVew = view.findViewById(R.id.shareduserImageVew) as ImageView
            sharedUserName = view.findViewById(R.id.sharedUserName) as TextView
            postImage = view.findViewById(R.id.postImage) as ImageView
            addVoiceRecord = view.findViewById(R.id.addVoicetbtn) as ImageButton
            addCommentText = view.findViewById(R.id.addCommentbtn) as ImageButton
        }


    }

    override fun setSeekMax(max: Int) {
//        currentSeekBar.max = max


    }

    override fun setprogress(playerCurrentPosition: Int) {
//        currentSeekBar.progress = playerCurrentPosition
    }

    override fun finishSeekBar() {
        currentPlay.visibility = View.VISIBLE
        currentPause.visibility = View.GONE
        currentSeekTv.text = "0:00"
//        setprogress(0)
    }

    override fun updateSeekTv(currentSec: String?) {
        if (currentSec!!.contains("87:")) return
        currentSeekTv.text = currentSec

    }

    override fun callHearVoiceApi() {
        Log.d("", "callHearVoiceApi: " + "start call api")
        AndroidNetworking.post(RetrofitClient.BASE_URL + "hearVoiceNote")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json ")
                .addBodyParameter("id", currentPostId.toString())
                .build().getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        /*   Log.d("", "onResponse: $response")
                           val feedData = items.get(currentSeekBarPosition);
                           val hearedInt = feedData.heard_count.toInt();
                           feedData.heard_count=(hearedInt+1).toString();

                           notifyItemChanged(currentSeekBarPosition)*/

                        Log.d("", "callHearVoiceApi: " + response)
                    }

                    override fun onError(anError: ANError) {
                        Log.d("", "ANError: " + anError.errorBody)
                    }
                })
    }
}


