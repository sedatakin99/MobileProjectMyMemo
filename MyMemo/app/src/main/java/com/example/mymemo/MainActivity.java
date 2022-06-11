package com.example.mymemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText title, description, location;
    TextView moodText;
    ImageView image;
    Button saveButton, viewButton, galleryButton, locButton;
    SQLiteAdapter sqLiteAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri imageUri;
    FusedLocationProviderClient fusedLocationProviderClient;
    Spinner spinnerEmoji;


    List<Integer> listEmoji = Arrays.asList(0x2764, 0x1F601, 0x1F603, 0x1F606, 0x1F609, 0x1F60A, 0x1F60B,
            0x1F60D, 0x1F616, 0x1F618, 0x1F621, 0x1F624, 0x1F628, 0x1F62D, 0x1F631, 0x1F64C);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.default_memory);

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

        title = findViewById(R.id.titleMain);
        description = findViewById(R.id.descriptionMain);
        location = findViewById(R.id.locationMain);

        saveButton = findViewById(R.id.buttonSave);
        viewButton = findViewById(R.id.buttonView);
        galleryButton = findViewById(R.id.buttonGetImage);
        locButton = findViewById(R.id.buttonGetLoc);
        image = findViewById(R.id.imageViewMain);

        moodText = findViewById(R.id.moodText);
        spinnerEmoji = findViewById(R.id.spinnerEmoji);
        ArrayAdapter<String> emojiAdapter = new ArrayAdapter<String>(this, R.layout.emoji_spinner, getEmojiList(listEmoji));
        spinnerEmoji.setAdapter(emojiAdapter);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        sqLiteAdapter = new SQLiteAdapter(this);


        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    showLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Memory.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleTXT = title.getText().toString();
                String descTXT = description.getText().toString();
                String locTXT = location.getText().toString();
                String uriTXT = imageUri.toString();
                String emojiTXT = spinnerEmoji.getSelectedItem().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(" d MMM yyyy HH:mm:ss ");
                String timeTXT = format.format(calendar.getTime());

                Boolean checkSaveData = sqLiteAdapter.insertMemoryData(titleTXT, descTXT, locTXT, uriTXT, emojiTXT, timeTXT);
                if (checkSaveData == true) {
                    Toast.makeText(MainActivity.this, "New Memory Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Couldn't Save Memory", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(MainActivity.this, Memory.class));
            }
        });
    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location1 = task.getResult();
                if (location1 != null){
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressesList = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                        location.setText(addressesList.get(0).getAddressLine(0));
                    }catch (IOException e){

                    }
                }
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
}