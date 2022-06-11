package com.example.mymemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ViewDetails extends AppCompatActivity implements PasswordDialog.PasswordDialogListener {
    EditText title, description, location;
    ImageView image;
    Uri imageUri;
    Button updateButton, viewButton, deleteButton, updateImageButton, updateTimeButton, passwordButton;
    TextView emoji, time;
    Spinner spinnerEmoji;
    ActivityResultLauncher<Intent> activityResultLauncher;
    List<Integer> listEmoji = Arrays.asList(0x2764, 0x1F601, 0x1F603, 0x1F606, 0x1F609, 0x1F60A, 0x1F60B,
            0x1F60D, 0x1F616, 0x1F618, 0x1F621, 0x1F624, 0x1F628, 0x1F62D, 0x1F631, 0x1F64C);
    SQLiteAdapter sqLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        String title1 = getIntent().getStringExtra("Title");
        String desc1 = getIntent().getStringExtra("Description");
        String loc1 = getIntent().getStringExtra("Location");
        String uri1 = getIntent().getStringExtra("Uri");
        String emoji1 = getIntent().getStringExtra("Emoji");
        String time1 = getIntent().getStringExtra("Time");


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri = result.getData().getData();
                        image.setImageURI(imageUri);
                    }
                }
            }
        });

        title = findViewById(R.id.titleView);
        description = findViewById(R.id.descriptionView);
        location = findViewById(R.id.locationView);
        image = findViewById(R.id.imageView);
        emoji = findViewById(R.id.emojiView);
        time = findViewById(R.id.timeView);

        viewButton = findViewById(R.id.buttonView);
        updateButton = findViewById(R.id.buttonUpdate);
        deleteButton = findViewById(R.id.buttonDelete);
        updateImageButton = findViewById(R.id.buttonUpdateImage);
        updateTimeButton = findViewById(R.id.buttonUpdateTime);
        passwordButton = findViewById(R.id.buttonPassword);

        spinnerEmoji = findViewById(R.id.spinnerEmojiView);
        ArrayAdapter<String> emojiAdapter = new ArrayAdapter<String>(this, R.layout.emoji_spinner, getEmojiList(listEmoji));
        spinnerEmoji.setAdapter(emojiAdapter);

        title.setText(title1);
        description.setText(desc1);
        location.setText(loc1);
        image.setImageURI(Uri.parse(uri1));
        emoji.setText(emoji1);
        time.setText(time1);

        sqLiteAdapter = new SQLiteAdapter(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Delete");
        builder.setMessage("Do you really want to delete memory?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                sqLiteAdapter.deleteMemoryData(title.getText().toString());
                startActivity(new Intent(ViewDetails.this, Memory.class));
                Toast.makeText(ViewDetails.this, "Memory Deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();


        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewDetails.this, Memory.class));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleTXT = title.getText().toString();
                String descTXT = description.getText().toString();
                String locTXT = location.getText().toString();
                String uriTXT = uri1;
                String emojiTXT = spinnerEmoji.getSelectedItem().toString();
                if (imageUri != null){
                    uriTXT = imageUri.toString();
                }

                sqLiteAdapter.updateMemoryData(title1, titleTXT, descTXT, locTXT, uriTXT, emojiTXT, time1);


                Toast.makeText(ViewDetails.this, "Memory Updated", Toast.LENGTH_SHORT).show();
            }
        });

        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogMemory();
            }
        });

        updateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleTXT = title1.toString();
                String descTXT = desc1.toString();
                String locTXT = loc1.toString();
                String uriTXT = uri1.toString();
                String emojiTXT = spinnerEmoji.getSelectedItem().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(" d MMM yyyy HH:mm:ss ");
                String timeTXT = format.format(calendar.getTime());

                sqLiteAdapter.updateMemoryData(title1, titleTXT, descTXT, locTXT, uriTXT, emojiTXT, timeTXT);
                time.setText(timeTXT);
                Toast.makeText(ViewDetails.this, "Time Updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }

    public List<String> getEmojiList(List<Integer> list){
        List<String> emojiList = new ArrayList<>();
        for (Integer element: list) {
            emojiList.add(getEmoji(element));
        }
        return emojiList;
    }

    @Override
    public void applyText(String password) {
        Toast.makeText(ViewDetails.this, "Password added/updated", Toast.LENGTH_SHORT).show();
        sqLiteAdapter.insertMemoryPassword(title.getText().toString(), password);
    }


    public void openDialogMemory() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getSupportFragmentManager(), "Password Dialog");
    }
}