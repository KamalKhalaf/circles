package com.circles.circlesapp.addgroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ActivityWebViewBinding;

public class WebViewActivity extends Activity {
    ActivityWebViewBinding b;
    private String successUrl = "http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com/com.circles/public/onGroupPaymentSuccess";
    String paypal_url;
    private String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paypal_url = getIntent().getStringExtra("url");
        b = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        // b.toolbar.title.setText("Payment");
        b.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                currentUrl = url;
                Log.d("onPageCommitVisible", "onPageCommitVisible: " + url);
                if (url.contains("onGroupPaymentSuccess")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("success", true);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
        b.webView.loadUrl(paypal_url);
    }

    public static void start(Context context, String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("url", url);
        context.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (paypal_url.equals(currentUrl)) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("success", false);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            b.webView.loadUrl(paypal_url);
        }
    }
}
