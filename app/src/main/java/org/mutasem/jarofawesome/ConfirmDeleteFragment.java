package org.mutasem.jarofawesome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mutasemdmour on 6/13/16.
 */
public class ConfirmDeleteFragment extends DialogFragment{

    public interface ConfirmDeleteListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public int setDeleteMessage();
    }

    ConfirmDeleteListener mListener;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.d("onCreateDialog","sup!");
        try {
            mListener = (ConfirmDeleteListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                + "must implement ConfirmDeleteListener"
            );
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity()
        );
        builder.setMessage(
                mListener.setDeleteMessage()
        ).setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mListener.onDialogPositiveClick(ConfirmDeleteFragment.this);
                    }
                }
        ).setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mListener.onDialogNegativeClick(ConfirmDeleteFragment.this);
                    }
                }
        );
        return builder.create();
    }
}
