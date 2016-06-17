package org.mutasem.jarofawesome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by mutasemdmour on 6/15/16.
 */
public class GetTitleFragment extends DialogFragment {
    public interface GetTitleListener {
        public void onDialogPositiveClick(DialogFragment dialog, String title);
        public void onDialogNegativeClick(DialogFragment dialog);
        public String getOldTitle();
    }

    GetTitleListener mListener;
    View inflated;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mListener = (GetTitleListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + "must implement GetTitleListener"
            );
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity()
        );
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        inflated = inflater.inflate(R.layout.fragment_title_get, null);
        EditText editText = (EditText) inflated.findViewById(R.id.title_input);
        editText.setText(mListener.getOldTitle());
        builder.setView(inflated)
                .setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        EditText editText = (EditText) inflated.findViewById(R.id.title_input);
                        String title = editText.getText().toString();
                        mListener.onDialogPositiveClick(GetTitleFragment.this, title);
                    }
                }
        ).setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mListener.onDialogNegativeClick(GetTitleFragment.this);
                    }
                }
        );
        return builder.create();
    }

}
