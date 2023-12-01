package com.example.newfirebasedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Note> notes = new ArrayList<>();
    private Note currentNote;
    private Button add, save, logout;
    private EditText title, content;
    private ListView listItem;
    private ViewFlipper flipper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.addBtn);
        save = findViewById(R.id.saveBtn);
        logout = findViewById(R.id.logout);
        title = findViewById(R.id.titleTxt);
        content = findViewById(R.id.contentTxt);
        listItem = findViewById(R.id.noteList);
        flipper = findViewById(R.id.flipper);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user.getUid());
        add.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentNote = new Note(String.valueOf(notes.size()+1), new Date(), "New note",null,null);
                myRef.child(currentNote.getId()).setValue(currentNote);
                title.setText(currentNote.getTitle());
                content.setText(currentNote.getContent());
                add.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                flipper.showNext();
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setTitle(title.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setContent(content.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(currentNote.getId()).setValue(currentNote);
                save.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                flipper.showPrevious();
                currentNote = new Note();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteAdapter adt = (NoteAdapter) parent.getAdapter();
                currentNote = adt.getItem(position);

                myRef.child(currentNote.getId()).setValue(currentNote);
                title.setText(currentNote.getTitle());
                content.setText(currentNote.getContent());
                add.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                flipper.showNext();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notes.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note note = ds.getValue(Note.class);
                    notes.add(note);
                }

                NoteAdapter adapter = new NoteAdapter(getApplicationContext(), notes);
                listItem.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}