package org.mutasem.jarofawesome;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity
        extends AppCompatActivity
        implements ConfirmDeleteFragment.ConfirmDeleteListener{

//    private Button putButton;
    private Button shuffleButton;
    protected static EntryDb db;
    protected static final String extraEntry = "org.mutasem.jarofawesome.entry";
    protected static final String bundleKey = "org.mutasem.jarofawesome.bundle";
//    private int count;
//    private Shaker shaker;
//    private SharedPreferences preferences;
    private Vibrator vibrator;
    private FragmentManager fragmentManager;
    private String FRAGMENT_TAG = "org.mutasem.jarofawesome.ShakeGifFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView titleTextView = new TextView(getApplicationContext());
        titleTextView.setText(
                getString(
                        R.string.main_menu_title
                )
        );

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        titleTextView.setLayoutParams(lp);
        titleTextView.setGravity(Gravity.CENTER_VERTICAL);
        titleTextView.setTextSize(20);
        titleTextView.setTextAppearance(R.style.mainMenuTitle);
        toolbar.addView(titleTextView);
        setSupportActionBar(toolbar);

        vibrator = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        shuffleButton = (Button) findViewById(R.id.shuffle_button);
//        fragmentManager = getSupportFragmentManager();
//        final ShakeGIFFragment fragment = new ShakeGIFFragment();
//        RelativeLayout view = (RelativeLayout) findViewById(R.id.gif_container);
//        MyGIFView gifView = new MyGIFView(getApplicationContext());
//        view.addView(gifView);

        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
//                    fragmentManager.beginTransaction().add(R.id.mainMenuFrameLayout,fragment,FRAGMENT_TAG).commit();
                    Snackbar.make(v,
                            "Ya, hold me tight",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if (vibrator.hasVibrator()){
                        long[] pattern = {0,5000};
                        vibrator.vibrate(pattern,0);
                    }
                } else if (event.getActionMasked() == MotionEvent.ACTION_UP){
//                    fragmentManager.beginTransaction().remove(fragment).commit();
//                    fragmentTransaction.commit();
                    long diff = event.getEventTime() - event.getDownTime();
                    if (vibrator.hasVibrator()){
                        vibrator.cancel();
                    }
                    if (diff > 3000){
                        getRandom(null);
                    } else {
                        Snackbar.make(v,
                                "Nooo, don't let go",
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
                //Log.d("MainMenuActivity","hold");
                return false;
            }
        };
        shuffleButton.setOnTouchListener(listener);

//        ActionBar actionBar = getSupportActionBar();
        /* setting the color for the upArrow */
//        final Drawable settings = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_black_24dp);
//        actionBar.setHomeAsUpIndicator(upArrow);
//        Shaker.Callback cb = new Shaker.Callback() {
//            @Override
//            public void shakingStarted() {
//            }
//            @Override
//            public void shakingStopped() {
//                getRandom(null);
//                stopListening();
//            }
//            @Override
//            public void shakingNotEnough() {
//                notifyMoreShaking();
//            }
//        };
//        shaker = new Shaker(getApplicationContext(),1.5d, 200, 4, cb);
    }
    @Override
    protected void onPause(){
        super.onPause();
//        stopListening();
    }

    @Override
    protected void onResume(){
        super.onResume();
        shuffleButton.setText(getResources().getString(R.string.get_random));
//        startListening();
    }

//    public void startListening(){
//        shaker.startListening();
//    }
//
//    public void stopListening(){
//        shaker.stopListening();
//    }

//    public void notifyMoreShaking(){
//        shuffleButton.setText(getResources().getString(R.string.more_shaking));
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        db = new EntryDb(getApplicationContext());
//        db.dropTable(getApplicationContext());
        //Log.d("onCreate, Main activity","damn");
    }


    public void callEntryWriterActivity(View view){
        Intent intent = new Intent(this, EntryWriterActivity.class);
        startActivity(intent);
    }

    public void getRandom(View view){
//            shuffleButton.setText(db.getRandom());
        if (view == null){
            view = findViewById(R.id.shuffle_button);
        }
        if (db.hasAtLeast(3)) {
            Entry extra = db.getRandom();
            //if (extra != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(bundleKey, extra);
                Intent intent = new Intent(getApplicationContext(),
                        EntryReaderActivity.class);
                intent.putExtra(extraEntry, bundle);
                startActivity(intent);
            //}
        } else {
            Snackbar.make(view,
                    getResources().getString(R.string.more_entries),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void launchViewAll(View view){
        if (view == null){
            view = findViewById(R.id.view_all_button);
        }
        if (db.hasAtLeast(1)){
            Intent intent = new Intent(getApplicationContext(),
                ViewAllActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(view,
                    getResources().getString(R.string.no_entries),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_all:
                DialogFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getSupportFragmentManager(),"ConfirmDeleteFragment");
                return true;
            case R.id.feedback:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.feedback_email_addr)});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_title));
                i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.feedback_email_body));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainMenuActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.ratemyapp:
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        MainMenuActivity.db.deleteAll();
        Snackbar.make(shuffleButton,
                getString(R.string.deleted_all),
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Snackbar.make(shuffleButton,
                getString(R.string.deleted_nothing),
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public int setDeleteMessage() {
        return R.string.delete_all_message;
    }
}
