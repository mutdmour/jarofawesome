package org.mutasem.jarofawesome;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewAllActivity extends AppCompatActivity
        implements ConfirmDeleteFragment.ConfirmDeleteListener{

    protected SimpleCursorAdapter adapter;
    private ListView listView;

    private static String[] fromColumns = {
            EntryDbContract.Entry._ID,
            EntryDbContract.Entry.COLUMN_NAME_ENTRY_TITLE,
            EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT,
    };

    private static int[] toViews = {
            0,
            R.id.title_listView,
            R.id.content_listView,
    };

    protected AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView parent, View v, int position, long id){
            Intent intent = new Intent(getApplicationContext(), EntryReaderActivity.class);
            Log.d("ViewAllActivity", String.valueOf(adapter.getItemId(position)));
//            Entry entry = MainMenuActivity.db.getRow(adapter.getItemId(position));
            TextView contentTextView = (TextView) v.findViewById(R.id.content_listView);
            TextView titleTextView = (TextView) v.findViewById(R.id.title_listView);
            Entry entry = new Entry(
                    adapter.getItemId(position),
                    contentTextView.getText().toString(),
                    titleTextView.getText().toString());
            Bundle bundle = new Bundle();
            bundle.putParcelable(MainMenuActivity.bundleKey,entry);
            intent.putExtra(MainMenuActivity.extraEntry,bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        listView = (ListView) findViewById(R.id.listview);

        Cursor cursor = MainMenuActivity.db.getAll(fromColumns);
        adapter = new SimpleCursorAdapter(
                this, //context
                R.layout.template_list_view,
                cursor,
                fromColumns,
                toViews,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        listView.setAdapter(adapter);
//        registerForContextMenu(listView);
        listView.setOnItemClickListener(listener);
        Log.d("ViewAllActivity","listening");
    }

    @Override
    public void onResume(){
        super.onResume();
        Cursor cursor = MainMenuActivity.db.getAll(fromColumns);
        adapter = new SimpleCursorAdapter(
                this, //context
                R.layout.template_list_view,
                cursor,
                fromColumns,
                toViews,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        listView.setAdapter(adapter);
        Log.d("ViewAllActivity","resuming");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_all_activity, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                DialogFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getSupportFragmentManager(), "ConfirmDeleteFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        MainMenuActivity.db.deleteAll();
        Snackbar.make(listView,
                getString(R.string.deleted_all),
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Snackbar.make(listView,
                getString(R.string.deleted_nothing),
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public int setDeleteMessage() {
        return R.string.delete_all_message;
    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
//        ListView listView = (ListView) view;
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
//        menu.add(0, view.getId(), 0, "Delete");
//    }
}
