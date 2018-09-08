package com.tharuka.easymemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar app_bar;
    private NotesAdapter mAdapter;
    private FloatingActionButton fab_btn;
    private List<Note_Pojo> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SQLiteHelper db;
    TextView noNotesView;
    RelativeLayout main_view;
    //private EditText searchme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_bar = findViewById(R.id.app_bar);
        fab_btn = findViewById(R.id.fab_btn);
        setSupportActionBar(app_bar);


        //searchme = findViewById(R.id.search_me);
        recyclerView = findViewById(R.id.recycler_view);
        db = new SQLiteHelper(this);
        notesList.addAll(db.getAllNotes());
        noNotesView = findViewById(R.id.empty_notes_view);
        main_view = findViewById(R.id.main_view);


        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddMemo.class);
                startActivityForResult(i,7);
            }
        });

        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        toggleEmptyNotes();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                update(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    update(position);
                    //showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();

        /*searchme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Toast.makeText(MainActivity.this, searchme.getText(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, AddMemo.class);
                startActivity(i);

                }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });*/
    }

    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
            main_view.setVisibility(View.VISIBLE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            main_view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 7){
            if(resultCode==RESULT_OK)
            {
                assert data != null;
                Note_Pojo n = db.getNote(data.getLongExtra("result",0));

                if (n != null) {
                    // adding new note to array list at 0 position
                    notesList.add(0, n);

                    // refreshing the list
                    mAdapter.notifyDataSetChanged();

                    toggleEmptyNotes();
                }
            }



        }

        if(requestCode == 2){
            if(resultCode==RESULT_OK)
            {
                assert data != null;
                String post_data = data.getStringExtra("post");
                int id = data.getIntExtra("id",0);
                Note_Pojo n = notesList.get(id);
                n.setNote(post_data);

                mAdapter.notifyItemChanged(id);

                toggleEmptyNotes();

            }



        }
    }
    private void update(int position)
    {
        Intent i = new Intent(MainActivity.this, UpdateMemo.class);
        i.putExtra("data", notesList.get(position));
        i.putExtra("postion", position);
        startActivityForResult(i,2);
    }
}
