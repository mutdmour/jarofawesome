package org.mutasem.jarofawesome;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;

import java.lang.reflect.Field;

public class EntryWriterActivity
        extends AppCompatActivity
        implements GetTitleFragment.GetTitleListener{

    protected LinedEditText linedEditText;
    protected boolean input;
    protected boolean update = false;
    private static final String saved_content = "saved_content";
    private static Entry entry;
    private static String title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_writer);
        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        input = false;

        LinearLayout layout = (LinearLayout) findViewById(R.id.lined_edit_text);
        linedEditText = new LinedEditText(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        linedEditText.setLayoutParams(lp);
        linedEditText.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        linedEditText.setGravity(Gravity.TOP);
        linedEditText.setTextSize(TypedValue.DENSITY_DEFAULT,getResources().getDimension(R.dimen.entry_content_size));
        linedEditText.setWatcher();
        layout.addView(linedEditText);
        if (intent.hasExtra(EntryReaderActivity.extraEntry2Edit)){
            update = true;
            Bundle extra = intent.getBundleExtra(EntryReaderActivity.extraEntry2Edit);
            //Log.d("writer",extra.getString(null));
            entry = extra.getParcelable(MainMenuActivity.bundleKey);
            linedEditText.setText(entry.getContent());
            title = entry.getTitle();
        }
        if (savedInstanceState != null){
            linedEditText.setText(savedInstanceState.getString(saved_content));
        }
//        tintView(linedEditText,
//                ContextCompat.getColor(
//                        getApplicationContext(),
//                        R.color.colorPrimaryLight
//                )
//        );

        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(linedEditText,R.drawable.color_cursor);
        } catch (Exception e){

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInput();
            }
        });

    }

    /**
     * Set backgroundTint to {@link View} across all targeting platform level.
     * @param view the {@link View} to tint.
     * @param color color used to tint.
     */
    public static void tintView(View view, int color) {
        final Drawable d = view.getBackground();
        final Drawable nd = d.getConstantState().newDrawable();
        nd.setColorFilter(
                AppCompatDrawableManager.getPorterDuffColorFilter(
                        color,
                        PorterDuff.Mode.SRC_IN
                )
        );
        //nd.applyTheme(R.style.MyEditTextTheme);
        view.setBackground(nd);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(saved_content,linedEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public boolean saveInput(){
        String text = linedEditText.getText().toString();
        if (text.trim().length() > 0){
            DialogFragment dialog = new GetTitleFragment();
            dialog.show(getSupportFragmentManager(),"GetTitleFragment");
        } else {
            Snackbar.make(linedEditText, getResources().getString(R.string.no_input_message), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        }

//        if (saved) {
//            Snackbar.make(view, getResources().getString(R.string.saved_message), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//        } else {
//            Snackbar.make(view, getResources().getString(R.string.no_input_message), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//        }
//        String text = linedEditText.getText().toString();
//        if (text.length() > 0) {
//            if (update){
//                entry.setContent(text);
//                update = false;
//            } else {
//            }
//
//            return true;
//        }
        return false;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newTitle) {
//        Log.d("onDialogPositiveClick",newTitle);
        if (newTitle != null) {
            if (newTitle.trim().length() > 0) {
                if (update == true) {
                    entry.setTitle(newTitle);
                    entry.setContent(linedEditText.getText().toString());
                    MainMenuActivity.db.updateRow(entry);
                    update = false;
                    this.title = null;
                } else {
                    MainMenuActivity.db.insertRow(linedEditText.getText().toString(), newTitle);
                }
                linedEditText.setText("");
            }
        } else {
            //dialog.show(getSupportFragmentManager(),"GetTitleFragment");
            //keep dialog open
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public String getOldTitle(){
        if (title == null){
            return "";
        }
        return title;
    }
}
