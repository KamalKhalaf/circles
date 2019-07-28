package com.circles.circlesapp.qr;



import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ScanQrLayoutBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.settings.callBacks.QrCallBack;
import com.circles.circlesapp.settings.viewModels.qr.QrViewModel;

public class ScanQrActivity extends AppCompatActivity implements QrCallBack {
    private CodeScanner mCodeScanner;
    private ScanQrLayoutBinding layoutBinding;
    private int customerId;
    private String customerSurname;
    private SharedPrefHelper helper;
    private int userId;
    private QrViewModel viewModel;
    public static void start(Context context){
        Intent intent=new Intent(context,ScanQrActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding= DataBindingUtil.setContentView(this,R.layout.scan_qr_layout);
        setContentView(layoutBinding.getRoot());
        helper = new SharedPrefHelper(this);
        userId = helper.getUserId();
        viewModel= ViewModelProviders.of(this).get(QrViewModel.class);
        viewModel.attachView(this);
        mCodeScanner = new CodeScanner(this, layoutBinding.scannerView);
        mCodeScanner.setDecodeCallback(result ->
            runOnUiThread(()->{
              //  Toast.makeText(ScanQrActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                Log.d("scanprofile",result.getText());
                String[] words=result.getText().split(" ");

                if(words.length > 0) {
                    customerId = Integer.valueOf(words[0]);
                    customerSurname = new String(words[1]);

                    viewModel.reqFollowOrUnFollow(this ,customerSurname ,userId , customerId);
                }
            })
        );
        layoutBinding.scannerView.setOnClickListener(e ->
                mCodeScanner.startPreview());
    }

    @Override
    public void onSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        MyServiceInterceptor.scanFail=msg;
        finish();
    }

    @Override
    public ScanQrActivity getQrActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
