package com.circles.circlesapp.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.circles.circlesapp.R
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener
import com.circles.circlesapp.home.NewsFeedData
import com.circles.circlesapp.home.NewsFeedResponse
import com.circles.circlesapp.recyclers.NewsFeedAdapter
import com.circles.circlesapp.retrofit.RetrofitClient
import com.facebook.FacebookSdk.getApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserPostsFragment : Fragment(), CustomOnClickListener<NewsFeedData> {
    override fun onclick(v: View?, model: NewsFeedData?) {
        if (v!!.id == R.id.userImage) {
            UserProfileActivity.start(activity, model!!.userId)
        }
    }

    private var items = ArrayList<NewsFeedData>()
    var recyclerView: RecyclerView? = null
    private var mAdapter: NewsFeedAdapter? = null
    lateinit var accessToken: String
    lateinit var tokenType: String

    companion object {
        fun newInstance(tokenType: String, accessToken: String): UserPostsFragment {
            val args = Bundle()
            val fragment = UserPostsFragment()
            args.putString("ACCESS_TOKEN", accessToken)
            args.putString("TOKEN_TYPE", tokenType)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            accessToken = arguments!!.getString("ACCESS_TOKEN")
            tokenType = arguments!!.getString("TOKEN_TYPE")
            getUserPosts(tokenType!!, accessToken!!)
        }
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.user_posts, container, false)

        recyclerView = view.findViewById(R.id.postsRecycler) as RecyclerView
        val mLayoutManager = LinearLayoutManager(getApplicationContext())
        mAdapter = NewsFeedAdapter(items, activity!!, getApplicationContext(), this)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        return view
    }

    interface postsCallBack {
        fun start()
        fun sucess()
        fun failed(msg: String?)
    }

    private fun getUserPosts(tokenType: String, accessToken: String) {

        var call: Call<NewsFeedResponse> = RetrofitClient
                .getInstance()
                .api
                .getUserPosts(MyServiceInterceptor.getAuth())

        call.enqueue(object : Callback<NewsFeedResponse> {
            override fun onFailure(call: Call<NewsFeedResponse>?, t: Throwable?) {
                Toast.makeText(getApplicationContext(), t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<NewsFeedResponse>?, response: Response<NewsFeedResponse>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()

                try {
                    if (response.code() == 300) {
                        Log.e("UserPost : ", "Done without Data")
                        var newsFeedResponse: NewsFeedResponse = response.body()!!
                        Toast.makeText(activity, newsFeedResponse.message, Toast.LENGTH_LONG).show()
                    } else if (response.code() == 400) {
                        Toast.makeText(activity, response.message() + response.code(), Toast.LENGTH_LONG).show()

                    } else if (response.code() == 200) {
                        var newsFeedResponse: NewsFeedResponse = response.body()!!
                        items.addAll(newsFeedResponse.data)

                        mAdapter!!.notifyDataSetChanged()
                        val s = ""
                    }
                } catch (e: Exception) {
                }
            }

        })


    }

    fun userPosts(token: String, userId: Int, lisenter: postsCallBack) {
        lisenter.start()
        var call: Call<NewsFeedResponse> = RetrofitClient
                .getInstance()
                .api
                .userPosts(token, userId)

        call.enqueue(object : Callback<NewsFeedResponse> {
            override fun onFailure(call: Call<NewsFeedResponse>?, t: Throwable?) {
                lisenter.failed(t?.message)
            }

            override fun onResponse(call: Call<NewsFeedResponse>?, response: Response<NewsFeedResponse>?) {
                val s = ""
                var se = response!!.isSuccessful
                var codee = response.code()

                try {
                    if (response.code() == 300) {
                        Log.e("UserPost : ", "Done without Data")
                        var newsFeedResponse: NewsFeedResponse = response.body()!!
                        lisenter.failed(newsFeedResponse.message)
                    } else if (response.code() == 400) {
                        lisenter.failed(response.message())
                    } else if (response.code() == 200) {
                        var newsFeedResponse: NewsFeedResponse = response.body()!!
                        items.addAll(newsFeedResponse.data)
                        lisenter.sucess()
                        mAdapter!!.notifyDataSetChanged()
                        val s = ""
                    }
                } catch (e: Exception) {
                }
            }

        })


    }

}