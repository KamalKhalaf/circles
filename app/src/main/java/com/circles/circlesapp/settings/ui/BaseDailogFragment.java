package com.circles.circlesapp.settings.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseDailogFragment extends DialogFragment implements View.OnClickListener {
    public View view;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(setContentView(), null, false);
        builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        dialog = builder.create();
        viewReady(savedInstanceState);
        iniViews();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(setContentView(), null, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        iniViews();
        return view;
    }

    public void viewReady(Bundle bundle) {
    }

    public <t extends View> t bind(@IdRes int id) {
        return view.findViewById(id);
    }

    public void click(@IdRes int... ids) {
        for (int id : ids) {
            bind(id).setOnClickListener(this);
        }
    }

    public void click(@Nullable View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @LayoutRes
    public abstract int setContentView();

    public abstract void iniViews();


}
