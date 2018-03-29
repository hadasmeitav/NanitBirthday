package com.nanit.birthday;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DatePickerListener {

    private TextInputEditText nameView;
    private TextView dateView;
    private Button showBirthday;
    private Button addPhoto;
    private ImageView photo;

    private Preferences preferences;

    static final int REQUEST_IMAGE_CAPTURE = 101;
    static final int REQUEST_SELECT_FILE = 102;
    static final int REQUEST_CAMERA_READ_STORAGE_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = new Preferences(getApplicationContext());

        initViews();
        initListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        preferences.setValue(Preferences.Key.Name, nameView.getText().toString());
        preferences.setValue(Preferences.Key.Date, dateView.getText().toString());
    }

    @Override
    public void onDateSelected(String selectedDate) {
        dateView.setText(selectedDate);
        enableShowBirthdayButton();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_READ_STORAGE_PERMISSION && grantResults.length == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            selectPhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                photo.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_SELECT_FILE) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        photo.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initViews() {
        nameView = findViewById(R.id.name);
        dateView = findViewById(R.id.date);
        showBirthday = findViewById(R.id.show_birthday);
        addPhoto = findViewById(R.id.add_photo);
        photo = findViewById(R.id.photo);

        nameView.setText(preferences.getValue(Preferences.Key.Name));
        dateView.setText(preferences.getValue(Preferences.Key.Date));

        enableShowBirthdayButton();
    }

    private void initListeners() {
        findViewById(R.id.set_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dFragment = new DatePickerFragment();
                dFragment.setDatePickerListener(MainActivity.this);
                dFragment.show(getSupportFragmentManager(), getString(R.string.date_picker_tag));
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CAMERA_READ_STORAGE_PERMISSION);
                } else {
                    selectPhoto();
                }
            }
        });

        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableShowBirthdayButton();
            }
        });
    }

    private void enableShowBirthdayButton() {
        if (showBirthday.getVisibility() == View.GONE &&
                !TextUtils.isEmpty(dateView.getText()) && !TextUtils.isEmpty(nameView.getText())) {
            showBirthday.setVisibility(View.VISIBLE);
        } else if (showBirthday.getVisibility() == View.VISIBLE &&
                TextUtils.isEmpty(nameView.getText())) {
            showBirthday.setVisibility(View.GONE);
        }
    }

    private void selectPhoto() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_library)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else if (items[item].equals(getString(R.string.choose_from_library))) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), REQUEST_SELECT_FILE);
                }
            }
        });
        builder.show();
    }
}
