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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by mutasemdmour on 6/15/16.
 */
public class GetTitleFragment extends DialogFragment {
    public interface GetTitleListener {
        public void onDialogPositiveClick(DialogFragment dialog, String newTitle);
        public void onDialogNegativeClick(DialogFragment dialog);
        public String getOldTitle();
    }

    private GetTitleListener mListener;
    private View inflated;
    private static String KEY_CURR_TITLE = "org.mutasem.jarofawesome.GetTitleFragment.currTitle";

    public static GetTitleFragment newInstance(String title) {
        GetTitleFragment frag = new GetTitleFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        frag.setArguments(args);
        return frag;
    }


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
        String currTitle = mListener.getOldTitle();
        if (savedInstanceState != null){
            currTitle = savedInstanceState.getString(KEY_CURR_TITLE);
        }
        editText.setText(currTitle);
        builder.setView(inflated)
                .setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        EditText editText = (EditText) inflated.findViewById(R.id.title_input);
                        String newTitle = editText.getText().toString();
                        Log.d("newTitle",newTitle);
                        mListener.onDialogPositiveClick(GetTitleFragment.this, newTitle);
                        editText.setText("");
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
//        return builder.create();
        Dialog d = builder.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText editText = (EditText) inflated.findViewById(R.id.title_input);
        editText.requestFocus();
    }


}
