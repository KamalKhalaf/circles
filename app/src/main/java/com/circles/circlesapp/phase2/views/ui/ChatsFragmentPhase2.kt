package com.circles.circlesapp.phase2.views.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.MyDividerItemDecoration
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.phase2.views.adapter.MessageAdapterPhase2
import com.facebook.shimmer.ShimmerFrameLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/*
*
* Created by Kamal Khalaf on 8/30/2019.
* Contact : kamal.khalaf56@gmail.com
*
*/

class ChatsFragmentPhase2 : Fragment() {

    companion object {
        fun newInstance(): ChatsFragmentPhase2 {
            val args = Bundle()
            val fragment = ChatsFragmentPhase2()
            fragment.setArguments(args)
            return fragment
        }
    }

    private var mShimmerViewContainer: ShimmerFrameLayout? = null

    lateinit var scrollToTop: LinearLayout
    lateinit var recyclerView: RecyclerView
    lateinit var no_items_layout: LinearLayout
    lateinit var mAdapter: MessageAdapterPhase2
    private var mRefreshLayout: SmartRefreshLayout? = null
    private var isFirstEnter = false


//    private lateinit var viewModel: ChatsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.chats, container, false)
//        viewModel = ViewModelProviders.of(this).get(ChatsViewModel::class.java)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        no_items_layout = view.findViewById(R.id.no_items_layout) as LinearLayout
        prepareRecycleView()
        mShimmerViewContainer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(MyDividerItemDecoration(activity, LinearLayoutManager.VERTICAL, 16))

//        viewModel.getChatRooms()

        mRefreshLayout = view.findViewById(R.id.refreshLayout) as SmartRefreshLayout
        mRefreshLayout!!.setOnRefreshListener({
            isFirstEnter = true
            recyclerView!!.setVisibility(View.GONE)
            mShimmerViewContainer!!.setVisibility(View.VISIBLE)
            mShimmerViewContainer!!.startShimmerAnimation()
//            viewModel.getChatRooms()
        })

        recyclerView.adapter = mAdapter
        if (isFirstEnter) {
            mRefreshLayout!!.finishRefresh()
//            isFirstEnter = false
        }
        recyclerView!!.setVisibility(View.VISIBLE)
        recyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        no_items_layout.setVisibility(View.GONE);
        mShimmerViewContainer!!.stopShimmerAnimation()
        mShimmerViewContainer!!.setVisibility(View.GONE)
//        viewModel.mutableLDResp.observe(this, Observer {
//            if (it != null) {
//                mAdapter = MessageAdapter(it)
//                if (isFirstEnter) {
//                    mRefreshLayout!!.finishRefresh()
//                    isFirstEnter = false
//                }
//                recyclerView!!.setVisibility(View.VISIBLE)
//                recyclerView.adapter = mAdapter
//                mAdapter.notifyDataSetChanged()
//                no_items_layout.setVisibility(View.GONE);
//                mShimmerViewContainer!!.stopShimmerAnimation()
//                mShimmerViewContainer!!.setVisibility(View.GONE)
//            }else{
//                no_items_layout.setVisibility(View.VISIBLE);
//                mShimmerViewContainer!!.stopShimmerAnimation()
//                mShimmerViewContainer!!.setVisibility(View.GONE)
//            }
//        })


//        val record_img = activity!!.findViewById<ImageView>(R.id.floatingActionButton)
//        /* val text=activity!!.findViewById<TextView>(R.id.actionName)
//         text.visibility=View.GONE*/
//        activity!!.findViewById<TextView>(R.id.actionName).setText(getString(R.string.post_now))
//        record_img.setImageResource(R.drawable.add_post_record_white)
//        //record_img.visibility=View.GONE
//        record_img.setOnClickListener {
//            Log.d("test", "post Voice")
//        }

//        val toolbar = activity!!.findViewById<View>(R.id.toolbar) as Toolbar?
//        lateinit var profile_img: CircleImageView
//        profile_img = toolbar?.findViewById<CircleImageView>(R.id.logo)!!


        val prefHelper = SharedPrefHelper(activity)


//        val userImage = toolbar?.findViewById<CircleImageView>(R.id.logo)
//        val progressBarload = toolbar?.findViewById<ProgressBar>(R.id.progressBarload)
//        if (prefHelper.profileImage != null) {
//            Glide.with(this).asBitmap()
//                    .load(prefHelper.profileImage)
//                    .into(userImage)
//        } else {
//            Glide.with(this).load(R.drawable.sign_up_profile).into(userImage)
//        }
        scrollToTop = view.findViewById(R.id.scrollToTop) as LinearLayout
        scrollToTop!!.setOnClickListener({
            scrollToTop!!.visibility = View.GONE
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
                        scrollToTop!!.visibility = View.VISIBLE
                    } else {
                        scrollToTop!!.visibility = View.GONE
                    }
                }
            }
        })

        return view

    }

    fun prepareRecycleView() {
        val mLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView!!.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
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