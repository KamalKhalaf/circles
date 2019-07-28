package com.circles.circlesapp.home

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.*
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.circles.circlesapp.Home
import com.circles.circlesapp.R
import com.circles.circlesapp.addgroup.MessageEvent
import com.circles.circlesapp.comments.PostCommentsFragment
import com.circles.circlesapp.databinding.FragmentHomeBinding
import com.circles.circlesapp.helpers.AppConstants
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener
import com.circles.circlesapp.helpers.utilities.PermissionUtils
import com.circles.circlesapp.messaging.view.EditPost
import com.circles.circlesapp.messaging.view.RecordAudioDialogFragment
import com.circles.circlesapp.profile.UserProfileActivity
import com.circles.circlesapp.recyclers.NewsFeedAdapter
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.jsibbold.zoomage.ZoomageView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class HomeFragment : Fragment(), CustomOnClickListener<NewsFeedData> {

    lateinit var profile_img: CircleImageView
    private var mLastClickTime: Long = 0
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mRefreshLayout: RefreshLayout? = null
    private var isFirstEnter = false


    val hearId: Int = 10;
    override fun onclick(v: View?, model: NewsFeedData?) {
        if (v!!.id == R.id.like) {
            viewModel.onLikeBtnclicked(model!!.id.toString())
        } else if (v.id == R.id.share) {
            viewModel.onShareBtnClicked(model!!.id.toString())
        } else if (v.id == R.id.addCommentbtn) {
            showFragment(PostCommentsFragment.newInstance(model!!.id, model.shares_count, model.heard_count))
        } else if (v.id == R.id.addVoicetbtn) {
            val audioDialogFragment = RecordAudioDialogFragment.newInstance(model!!.id)
            showFragment(audioDialogFragment)
        } else if (v.id == R.id.userImage) {

            if (MyServiceInterceptor.userId == model!!.userId) {
                (activity as Home).gotoProfile()
            } else {
                UserProfileActivity.start(activity, model!!.userId)
            }

        } else if (v.id == R.id.btnEditOrDeletePost) {
            editOrdeleteThePost(model)
        }
    }

    private fun editOrdeleteThePost(model: NewsFeedData?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()

        val bottomSheetLayout = layoutInflater.inflate(R.layout.cuastom_dialoug_for_sort, null)
        mBottomSheetDialog = BottomSheetDialog(Objects.requireNonNull<FragmentActivity>(activity))

        mBottomSheetDialog!!.setContentView(bottomSheetLayout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull<Window>(mBottomSheetDialog!!.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (!mBottomSheetDialog!!.isShowing()) {
            mBottomSheetDialog!!.show()
        }
        mBottomSheetDialog!!.setOnDismissListener({ dialog -> mBottomSheetDialog = null })

        val layoutEditPost = bottomSheetLayout.findViewById<RelativeLayout>(R.id.layoutEditPost)
        val layoutDeletePost = bottomSheetLayout.findViewById<RelativeLayout>(R.id.layoutDeletePost)

        layoutEditPost.setOnClickListener {
            mBottomSheetDialog!!.hide()

            editPost(model)
        }

        layoutDeletePost.setOnClickListener {
            mBottomSheetDialog!!.hide()

            deletePost(model!!.id.toString())
        }
    }

    private fun deletePost(postId: String) {
        viewModel.onDeletePost(postId)
    }

    private fun editPost(model: NewsFeedData?) {

        var gson = Gson()
        var jsonString = gson.toJson(model)
        val editPostfragment = EditPost.editPostInstance(jsonString)
        showFragment(editPostfragment)
    }

    private var items = ArrayList<NewsFeedData>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: NewsFeedAdapter? = null
    lateinit var accessToken: String
    val imageZoom: ZoomageView? = null
    lateinit var tokenType: String

    companion object {
        fun newInstance(tokenType: String, accessToken: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("ACCESS_TOKEN", accessToken)
            args.putString("TOKEN_TYPE", tokenType)
            fragment.arguments = args
            return fragment
        }
    }

    private fun showFragment(fragment: Fragment) {
        activity!!.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "rageComicList")
                .addToBackStack(null)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            accessToken = arguments!!.getString("ACCESS_TOKEN")
            tokenType = arguments!!.getString("TOKEN_TYPE")
        }

        super.onCreate(savedInstanceState)
    }

    private var mShimmerViewContainer: ShimmerFrameLayout? = null
    private lateinit var viewModel: HomeViewMode

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var b = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)
        b.setLifecycleOwner(this)

        if (!PermissionUtils.hasPermission(activity, Manifest.permission.RECORD_AUDIO)
                || !PermissionUtils.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionUtils.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            run {
                val d = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(d, 1)
            }
        }


        viewModel = ViewModelProviders.of(this).get(HomeViewMode::class.java)
        b.vm = viewModel
        viewModel.getNewsFeed()
        recyclerView = b.recyclerPostsHome
        val mLayoutManager = LinearLayoutManager(getApplicationContext())
        mAdapter = NewsFeedAdapter(items, activity!!, getApplicationContext(), this)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.addItemDecoration(DividerItemDecoration(this.context, VERTICAL))
        recyclerView!!.setItemAnimator(DefaultItemAnimator())

        mShimmerViewContainer = b.shimmerViewContainer


        b.scrollToTop!!.setOnClickListener({
            b.scrollToTop!!.visibility = View.GONE
            recyclerView!!.getLayoutManager()!!.smoothScrollToPosition(recyclerView!!, null, 0);
        })

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("SetTextI18n")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
//                    val num = Objects.requireNonNull<RecyclerView.LayoutManager>(mLayoutManager).getChildCount()
                    val num = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (num > 7) {
                        b.scrollToTop!!.visibility = View.VISIBLE
                    } else {
                        b.scrollToTop!!.visibility = View.GONE
                    }
                }
            }
        })


        viewModel.mutableLDResp.observe(this, Observer {
            items.clear()
            items.addAll(it!!)
            if (isFirstEnter) {
                mRefreshLayout!!.finishRefresh()
                isFirstEnter = false
            }


            if (AppConstants.post_from_splash != null && AppConstants.post_from_splash != 0) {

                var b: Boolean = false
                for ((index, value) in items.withIndex()) {
                    if (index != null) {

                        if (value.id == AppConstants.post_from_splash) {

                            b = true
                            recyclerView!!.getLayoutManager()!!.scrollToPosition(index);

                        }
                    }
                }

                if (!b)
                    mAdapter!!.notifyDataSetChanged()


                mShimmerViewContainer!!.stopShimmerAnimation()
                mShimmerViewContainer!!.setVisibility(View.GONE)

            } else {
                mAdapter!!.notifyDataSetChanged()

                mShimmerViewContainer!!.stopShimmerAnimation()
                mShimmerViewContainer!!.setVisibility(View.GONE)
            }
        })

        viewModel.messageMLD.observe(this, Observer {
            if (it != null) {
                if (mShimmerViewContainer!!.isAnimationStarted) {
                    mShimmerViewContainer!!.stopShimmerAnimation()
                    mShimmerViewContainer!!.setVisibility(View.GONE)
                }

                Toast.makeText(getApplicationContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        val record_img = activity!!.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        val toolbar = activity!!.findViewById<View>(R.id.toolbar) as Toolbar?
        profile_img = toolbar?.findViewById<CircleImageView>(R.id.logo)!!


        val prefHelper = SharedPrefHelper(activity)


        val userImage = toolbar?.findViewById<CircleImageView>(R.id.logo)
        val progressBarload = toolbar?.findViewById<ProgressBar>(R.id.progressBarload)
        if (prefHelper.profileImage != null) {
            Glide.with(this).asBitmap()
                    .load(prefHelper.profileImage)
                    .into(userImage)
        } else {
            Glide.with(this).load(R.drawable.sign_up_profile).into(userImage)
        }

        mRefreshLayout = b.refreshLayout
        mRefreshLayout!!.setOnRefreshListener({
            isFirstEnter = true
            mShimmerViewContainer!!.setVisibility(View.VISIBLE)
            mShimmerViewContainer!!.startShimmerAnimation()
            viewModel.getNewsFeed()
        })

        activity!!.findViewById<TextView>(R.id.actionName).setText(getString(R.string.post_now))
        record_img.setImageResource(R.drawable.add_post_record_white)
        activity!!.findViewById<TextView>(R.id.actionName).visibility = View.VISIBLE
        record_img.isEnabled = true
        record_img.setOnClickListener {
            Log.d("test", "post Voice")
            val audioDialogFragment = RecordAudioDialogFragment.newFeedsInstance()
            showFragment(audioDialogFragment)
        }
        return b.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty()) return
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // This method is called when the  permissions are given
        }

        if (requestCode == 1) {
            var allowed = true
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allowed = false
                }
            }
            if (!allowed) {
                Toast.makeText(activity, "you must allow permission ", Toast.LENGTH_SHORT).show()
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(postData: MessageEvent) {
        Log.d("onMessageEvent", postData.toString())
        if (postData.reqCode == 1995) {
            showFragment(AddTextCommentFragment.newInstance(postData.postId, false))
            return
        }
        if (postData.reqCode == 1996) {
            showFragment(AddTextCommentFragment.newInstance(postData.postId, true))
            return
        }
        if (postData.isComment) {
            viewModel.addComment(postData.postId, postData.postText, postData.imagePaths, postData.recordpath)
        } else if (postData.isUpdatePost) {
            mShimmerViewContainer!!.setVisibility(View.VISIBLE)
            mShimmerViewContainer!!.startShimmerAnimation()
            viewModel.updatePost(postData.id, postData.text, postData.image)
        } else {

            mShimmerViewContainer!!.setVisibility(View.VISIBLE)
            mShimmerViewContainer!!.startShimmerAnimation()
            viewModel.addPostRecord(postData.recordpath, postData.postText, postData.imagePaths)

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: AddReplyModel) {
        Log.d("onMessageEvent", event.toString())
        if (event.commentId > 0) {
            //todo  viewModel.addReply(event.commentId)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
        Log.d("onAttach", "");


    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach", "");
        EventBus.getDefault().unregister(this)

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer!!.startShimmerAnimation()
    }

    override fun onPause() {
        mShimmerViewContainer!!.stopShimmerAnimation()
        super.onPause()
    }
}