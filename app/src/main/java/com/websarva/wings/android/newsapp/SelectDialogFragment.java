package com.websarva.wings.android.newsapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class SelectDialogFragment extends DialogFragment {
    // Dialogでどれを選んだかを判別させる
    public boolean selectFlag_dialog = false;

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanceState){
        // DialogBuilderの生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        // Dialogに表示するリスト
        builder.setItems(R.array.select_dialog, (dialogInterface, which) -> {
            Log.d("test2", String.valueOf(which));
            if (which == 0){
                selectFlag_dialog = true;
            }
            // NewsAppActivityに値を渡す
            NewsAppActivity activity = (NewsAppActivity)getActivity();
            assert activity != null;
            activity.onFragmentResult(selectFlag_dialog);
        });
        // Dialogの生成
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
