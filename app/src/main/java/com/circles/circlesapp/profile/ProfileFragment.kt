package com.circles.circlesapp.profile

import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.circles.circlesapp.Home
import com.circles.circlesapp.R
import com.circles.circlesapp.databinding.FragmentProfileBinding
import com.circles.circlesapp.helpers.SharedPrefHelper
import com.circles.circlesapp.helpers.core.ApiResponseCallBack
import com.circles.circlesapp.helpers.core.Constants
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.profile.model.FollowerList
import com.circles.circlesapp.qr.ProfileQrFragment
import com.circles.circlesapp.settings.ui.SettingsActivity
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {
    lateinit var accessToken: String
    //    val myNonNullActivity: Context = context!!
    lateinit var tokenType: String
    val intents: Intent = Intent()
    var user: ProfileData? = null
    private var mRefreshLayout: RefreshLayout? = null
    private var isFirstEnter = false
    private var followerNum = 0
    private var follownigNum = 0
    private var ivCelabrity: ImageView? = null
    private var mProgressDialog: ProgressDialog? = null
    private lateinit var b: FragmentProfileBinding
    private var prefHelper: SharedPrefHelper? = null

    companion object {
        fun newInstance(tokenType: String, accsToken: String): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            args.putString("ACCESS_TOKEN", accsToken)
            args.putString("TOKEN_TYPE", tokenType)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            accessToken = arguments!!.getString("ACCESS_TOKEN")
            tokenType = arguments!!.getString("TOKEN_TYPE")
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        b = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)
        val scrollView = b.nestedscroll
        scrollView.isFillViewport = true
        val fragmentAdapter = MyPagerAdapter(this.childFragmentManager, tokenType, accessToken)
        var tabLayout: TabLayout = b.tabsMain
        var viewPager: ViewPager = b.viewpagerMain
        ivCelabrity = b.ivCelabrity

        prefHelper = SharedPrefHelper(activity)
        mRefreshLayout = b.refreshLayout
        mRefreshLayout!!.setOnRefreshListener({
            getProfileData(b)
            isFirstEnter = true
        })

        ivCelabrity!!.setOnClickListener {

        }

        b.layoutfolower.setOnClickListener({
            if (followerNum != 0) {
                getFollowerList()
            }
        })

        b.ivCelabrity.setOnClickListener {
            (activity as Home).gotoNearByMap()
        }

        mProgressDialog = ProgressDialog(getContext())
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.setMessage("loading ..... ")


        b.layoutfolowing.setOnClickListener({
            if (follownigNum != 0) {
                getFollowingList()
            }
        })

        val toolbar = activity!!.findViewById<View>(R.id.toolbar) as Toolbar?

        val userImage = toolbar?.findViewById<CircleImageView>(R.id.logo)
        userImage!!.setImageDrawable(activity!!.resources.getDrawable(R.drawable.qr_code))


        tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorPrimary))
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0!!.position) {
                    0 -> UserPostsFragment.newInstance(tokenType, accessToken)
                    1 -> UserLikedPostsFragment.newInstance(tokenType, accessToken)
                }
            }
        })
        //  val dialog = ProgressDialog.show(context, "", "loading profile Data")

        val record_img = activity!!.findViewById<FloatingActionButton>(R.id.floatingActionButton)
//        val navigation = activity!!.findViewById<FloatingActionButton>(R.id.navigation)
        activity!!.findViewById<TextView>(R.id.actionName).setText(getString(R.string.post_now))
        record_img.setImageResource(R.drawable.add_post_record_white)
        record_img.setOnClickListener {
            Log.d("test", "post Voice")
        }
        b.facebook.setOnClickListener {
            goToUrl("fb", user!!.social_accounts.facebook)
        }
        b.instagram.setOnClickListener {
            goToUrl("in", user!!.social_accounts.instagram)
        }
        b.linkedin.setOnClickListener {
            goToUrl("ld", user!!.social_accounts.linkedin)
        }
        b.twitter.setOnClickListener {
            goToUrl("tw", user!!.social_accounts.twitter)
        }
        b.youtube.setOnClickListener {
            goToUrl("yt", user!!.social_accounts.whatsapp)
        }
        b.snapchat.setOnClickListener {
            goToUrl("sn", user!!.social_accounts.whatsapp)
        }




        b.fabSettings.setOnClickListener {
            SettingsActivity.start(context)
        }
        try {
            activity!!.findViewById<ImageView>(R.id.logo).setOnClickListener {
                ProfileQrFragment.getInstance(user?.username, user?.profile_image).show(childFragmentManager, "ProfileQrFragment")
            }
        } catch (e: IllegalStateException) {
        }
        return b.root
    }

    private fun getFollowingList() {

        mProgressDialog!!.show()
        ProfileRepo().getUserFollowing(object : ApiResponseCallBack<FollowerList> {
            override fun success(s: FollowerList?) {
                mProgressDialog!!.dismiss()
                var gson = Gson()
                var jsonString = gson.toJson(s)
                val Following = FollowerOrFollowingFragment.follInstance(jsonString, "following")
                showFragment(Following)
            }

            override fun fail(message: String?) {
                mProgressDialog!!.dismiss()
                Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun getFollowerList() {
        mProgressDialog!!.show()
        ProfileRepo().getUserFollower(object : ApiResponseCallBack<FollowerList> {
            override fun success(s: FollowerList?) {
                mProgressDialog!!.dismiss()
                var gson = Gson()
                var jsonString = gson.toJson(s)
                val Following = FollowerOrFollowingFragment.follInstance(jsonString, "follower")
                showFragment(Following)
            }

            override fun fail(message: String?) {
                mProgressDialog!!.dismiss()
                Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show()

            }

        })

    }

    private fun getProfileData(b: FragmentProfileBinding) {
        ProfileRepo().getUserDetails(object : ApiResponseCallBack<ProfileData> {
            override fun success(s: ProfileData?) {
                //     dialog.dismiss()

                if (isFirstEnter) {
                    mRefreshLayout!!.finishRefresh()
                    isFirstEnter = false
                }


                user = s
                b.age.text = s!!.getAge()
                b.location.text = s.getLoc()
                b.isVerified.setVisibility(View.VISIBLE)
                b.followersNumber.text = s.followers
                b.followingNumbers.text = s.following

                prefHelper!!.addData(Constants.user_full_name , s.first_name + " " + s.last_name)

                if (prefHelper!!.getBoolean(Constants.is_celebrity)) {
                    b.layoutCelibrity.setVisibility(View.VISIBLE)
                }

                followerNum = s.followers.toInt()
                follownigNum = s.following.toInt()

                b.userName.text = s.getName()
                if (user!!.social_accounts.facebook != null)
                    b.facebook.visibility = View.VISIBLE

                if (user!!.social_accounts.instagram != null)
                    b.instagram.visibility = View.VISIBLE

                if (user!!.social_accounts.linkedin != null)
                    b.linkedin.visibility = View.VISIBLE

                if (user!!.social_accounts.twitter != null)
                    b.twitter.visibility = View.VISIBLE

                if (user!!.social_accounts.youtube != null)
                    b.youtube.visibility = View.VISIBLE

                if (user!!.social_accounts.whatsapp != null)
                    b.snapchat.visibility = View.VISIBLE

                if (s.profile_image != null && !s.profile_image.equals("") && context != null) {
                    Glide.with(context!!).asBitmap().load(s.profile_image).into(b.profilePicture)
                }


                b.userUniqueName.text = s.getUsername()
                b.userDescription.text = s.description
                if (s.age.equals("0")) {
                    b.age.visibility = View.INVISIBLE
                    b.separator.visibility = View.INVISIBLE
                }
                if ((s.city == null || s.city.equals("")) && (s.country == null || s.country.equals("0"))) {
                    b.location.visibility = View.INVISIBLE
                    b.separator.visibility = View.INVISIBLE
                }

            }

            override fun fail(message: String?) {
                //      dialog.dismiss()
                if (isFirstEnter) {
                    mRefreshLayout!!.finishRefresh()
                    isFirstEnter = false
                }
//                Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goToUrl(type: String?, s: String?) {

        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        if (type.equals("fb")) {
            openURL.data = Uri.parse("https://www.facebook.com/" + s)
        } else if (type.equals("in")) {
            openURL.data = Uri.parse("https://www.instagram.com/" + s)
        } else if (type.equals("ld")) {
            openURL.data = Uri.parse("https://www.linkedin.com/in/" + s)
        } else if (type.equals("tw")) {
            openURL.data = Uri.parse("https://twitter.com/" + s)
        } else if (type.equals("yt")) {
            openURL.data = Uri.parse("https://www.youtube.com/channel/" + s)
        } else if (type.equals("sn")) {
            openURL.data = Uri.parse("https://www.snap.com/en-US/" + s)
        }
        startActivity(openURL)

    }

    override fun onResume() {
        super.onResume()

        getProfileData(b)

        if (MyServiceInterceptor.scanFail != null) {
            Toast.makeText(context!!, MyServiceInterceptor.scanFail, Toast.LENGTH_SHORT)
            ProfileQrFragment.getInstance(user?.username, user?.profile_image).show(childFragmentManager, "ProfileQrFragment")
            MyServiceInterceptor.scanFail = null
        }
    }

    private fun showFragment(fragment: Fragment) {
        activity!!.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "rageComicList")
                .addToBackStack(null)
                .commit()
    }

}