package com.example.mymemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Memory extends AppCompatActivity implements RecyclerViewInterface, PasswordDialog.PasswordDialogListener, VerificationDialog.PasswordVerification {
    RecyclerView recyclerView;
    ArrayList<String> title, desc, location, uri, emoji, time;
    SQLiteAdapter sqLiteAdapter;
    ViewAdapter viewAdapter;
    Button newMemoryButton, generalPasswordButton;
    TextView passwordView;
    String passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        sqLiteAdapter = new SQLiteAdapter(this);
        title = new ArrayList<>();
        desc = new ArrayList<>();
        location = new ArrayList<>();
        uri = new ArrayList<>();
        emoji = new ArrayList<>();
        time = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        viewAdapter = new ViewAdapter(this, title, desc, location, uri, emoji, this);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();

        newMemoryButton = findViewById(R.id.addNewMemory);
        generalPasswordButton = findViewById(R.id.addGeneralPassword);
        passwordView = findViewById(R.id.passwordVerification);



        newMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Memory.this, MainActivity.class));
            }
        });

        generalPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getSupportFragmentManager(), "Password Dialog");
    }



    private void displayData() {
        Cursor cursor = sqLiteAdapter.getData();
        if (cursor.getCount() == 0){
            Toast.makeText(Memory.this, "No record of memories", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            while (cursor.moveToNext()){
                title.add(cursor.getString(0));
                desc.add(cursor.getString(1));
                location.add(cursor.getString(2));
                uri.add(cursor.getString(3));
                emoji.add(cursor.getString(4));
                time.add(cursor.getString(5));
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Memory.this, ViewDetails.class);

        intent.putExtra("Title", title.get(position));
        intent.putExtra("Description", desc.get(position));
        intent.putExtra("Location", location.get(position));
        intent.putExtra("Uri", uri.get(position));
        intent.putExtra("Emoji", emoji.get(position));
        intent.putExtra("Time", time.get(position));

        startActivity(intent);


    }

    @Override
    public void applyText(String password) {
        Toast.makeText(Memory.this, "Password added/updated", Toast.LENGTH_SHORT).show();
        sqLiteAdapter.insertGeneralPassword(password);
    }

    @Override
    public void sendInput(String input) {
        passwordInput = input;
    }
}