package org.mutasem.jarofawesome;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EntryReaderActivity
        extends AppCompatActivity
        implements ConfirmDeleteFragment.ConfirmDeleteListener{
    protected static String extraEntry2Edit = "org.mutasem.jarofawesome.entry2edit";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private TextView mContentView;
    private TextView mTitleView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private ActionBar actionBar;
    private Entry entry;
    private Bundle bundle;
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
//            Log.d("mDelayHideTouchListener","yup"+(motionEvent.getEventTime()-motionEvent.getDownTime()));
             if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP && (motionEvent.getEventTime() - motionEvent.getDownTime()) < 120){
//                Log.d("mDelayHideTouchListener","exit");
                toggle();
                 if (AUTO_HIDE && mVisible) {
//                     Log.d("mDelayHideTouchListener","hiding");
                     delayedHide(AUTO_HIDE_DELAY_MILLIS);
                 }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_reader);

        /* unpacking bundle with entry data from intent */
        Intent intent = getIntent();
        if (intent.hasExtra(MainMenuActivity.extraEntry)) {
            bundle = intent.getBundleExtra(MainMenuActivity.extraEntry);
            entry = bundle.getParcelable(MainMenuActivity.bundleKey);
        }

        /* setting texTview with text and scrolling */
        mTitleView = (TextView) findViewById(R.id.entry_title);
        mTitleView.setText(entry.getTitle());
        mContentView = (TextView) findViewById(R.id.entry_content);
        mContentView.setText(entry.getContent());
        mContentView.setMovementMethod(new ScrollingMovementMethod());

        /*is Action bar visible */
        mVisible = true;

        actionBar = getSupportActionBar();
        /* setting the color for the upArrow */
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
//        findViewById(
//                R.id.entry_title)
//                .setOnTouchListener(
//                        mDelayHideTouchListener
//                );
        findViewById(
                R.id.entry_content)
                .setOnTouchListener(
                        mDelayHideTouchListener
                );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_reader,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_entry:
                Intent intent = new Intent(
                        getApplicationContext(),
                        EntryWriterActivity.class
                );
                intent.putExtra(extraEntry2Edit,bundle);
                startActivity(intent);
                return true;
            case R.id.delete_entry:
                DialogFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getSupportFragmentManager(),"ConfirmDeleteFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);

    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * ConfirmDeleteFragment.ConfirmDeleteListener
     * What happens when user confirms deleting entry
    */
    public void onDialogPositiveClick(DialogFragment dialog){
        //delete entry
        MainMenuActivity.db.deleteRow(entry);
        //end ectivity
        Snackbar.make(mContentView,
                getString(R.string.deleted_one),
                Snackbar.LENGTH_LONG
        ).setAction("Action", null).show();
        finish();
    }

    /**
     * ConfirmDeleteFragment.ConfirmDeleteListener
     * What happens when user denies deleting entry
     */
    public void onDialogNegativeClick(DialogFragment dialog){
        Snackbar.make(mContentView,
                getString(R.string.deleted_nothing),
                Snackbar.LENGTH_LONG
        ).setAction("Action", null).show();
    }

    /**
     * ConfirmDeleteFragment.ConfirmDeleteListener
     * The message shown to the user
     */
    public int setDeleteMessage(){
        return R.string.delete_one_message;
    }
}
