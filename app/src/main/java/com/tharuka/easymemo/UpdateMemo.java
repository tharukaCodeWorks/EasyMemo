package com.tharuka.easymemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class UpdateMemo extends AppCompatActivity {

    private ImageButton back_to_home;
    private TextView update;
    private CustomEditText editText;
    private SQLiteHelper helper_db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_memo);

        back_to_home = findViewById(R.id.update_back_to_home);
        update = findViewById(R.id.update_note);
        editText = findViewById(R.id.update_memo_text);
        helper_db = new SQLiteHelper(this);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final Note_Pojo note_post = (Note_Pojo) intent.getSerializableExtra("data");
        final int position = intent.getIntExtra("position",0);

        editText.setText(note_post.getNote());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note = String.valueOf(editText.getText()).trim();

                if(!note.equals(""))
                {

                    Note_Pojo n = new Note_Pojo();
                    n.setId(note_post.getId());
                    n.setNote(note);
                    n.setTimestamp(note_post.getTimestamp());

                    helper_db.updateNote(n);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("post",note);
                    returnIntent.putExtra("id", position);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                    //Toast.makeText(AddMemo.this, "New note inserted successfully!",Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}
