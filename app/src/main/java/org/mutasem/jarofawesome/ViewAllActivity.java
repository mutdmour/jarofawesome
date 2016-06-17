package org.mutasem.jarofawesome;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewAllActivity extends AppCompatActivity {

    protected SimpleCursorAdapter adapter;

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

        final ListView listView = (ListView) findViewById(R.id.listview);

        String[] fromColumns = {
                EntryDbContract.Entry._ID,
                EntryDbContract.Entry.COLUMN_NAME_ENTRY_TITLE,
                EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT,
        };

        int[] toViews = {
                0,
                R.id.title_listView,
                R.id.content_listView,
        };

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
        listView.setOnItemClickListener(listener);
        Log.d("ViewAllActivity","listening");
    }
}
