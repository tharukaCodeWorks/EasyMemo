package com.tharuka.easymemo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddMemo extends AppCompatActivity {

    private ImageButton back_to_home;
    private TextView save;
    private CustomEditText editText;
    private SQLiteHelper helper_db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        back_to_home = findViewById(R.id.back_to_home);
        save = findViewById(R.id.save_note);
        editText = findViewById(R.id.memo_text);
        helper_db = new SQLiteHelper(this);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note = String.valueOf(editText.getText()).trim();

                if(!note.equals(""))
                {


                    long id = helper_db.insertNote(note);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",id);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                    //Toast.makeText(AddMemo.this, "New note inserted successfully!",Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}
