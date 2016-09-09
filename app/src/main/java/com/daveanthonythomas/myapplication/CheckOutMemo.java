package com.daveanthonythomas.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dave on 2016-09-08.
 */
public class CheckOutMemo extends AppCompatActivity {
    public static final int ADD_REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;
    public static final int REQUEST_IMAGE_CAPTURE = 1337;
    public String fileName;
    public Bitmap bitmap;
    private int position;
    EditText editableTitle;
    EditText editableContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        editableTitle = (EditText) findViewById(R.id.editHeader);
        editableContent = (EditText) findViewById(R.id.editBodyText);
        editableTitle.setText(intent.getStringExtra("header"));
        editableContent.setText(intent.getStringExtra("bodyText"));
        //checkIfUserChangedOrWroteAnyText();
        //Declaring keyword and default position.
        position = intent.getIntExtra("position", 0);
    }

    public void capturePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                loadImageFromFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void loadImageFromFile() throws IOException {

        ImageView view = (ImageView)this.findViewById(R.id.primeImage);
        view.setVisibility(View.VISIBLE);
        int targetW = view.getWidth();
        int targetH = view.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bitmap = BitmapFactory.decodeFile(fileName, bmOptions);
        view.setImageBitmap(bitmap);

    }

    public void onSaveClick(View view){
        String editableContentString = editableContent.getText().toString();
        String editableTitleString = editableTitle.getText().toString();
        if(TextUtils.isEmpty(editableContentString) && TextUtils.isEmpty(editableTitleString)) {
            finish();
            Toast.makeText(CheckOutMemo.this, "No content to save, note discarded", Toast.LENGTH_SHORT).show();
        }
        else {
            if ((TextUtils.isEmpty(editableTitleString))) {
                editableTitleString.equals(editableContentString);
                Intent intent = new Intent();
                //createImageFromBitmap();
                intent.putExtra("header", editableContent.getText().toString());
                intent.putExtra("position", position);
                intent.putExtra("photo", fileName);

                //Sending userInput back to MainActivity.
                setResult(AppCompatActivity.RESULT_OK, intent);
                finish();

            } else {
                Intent intent = new Intent();
                //createImageFromBitmap();
                intent.putExtra("header", editableTitle.getText().toString());
                intent.putExtra("bodyText", editableContent.getText().toString());
                intent.putExtra("photo", fileName);
                intent.putExtra("position", position);
                //Sending userInput back to MainActivity.
                setResult(AppCompatActivity.RESULT_OK, intent);
                finish();
            }
        }
    }

    public void cancelButtonClickedAfterEdit() {
        Button button = (Button) findViewById(R.id.bigCancelButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //openDialogFragment(v);
            }
        });
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String folder_main = "DNote";
        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + File.separator + folder_main;
        File storageDir = new File(path);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        fileName =  image.getAbsolutePath();
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{image.getPath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
//                        Log.i(TAG, "Scanned " + path);
                    }
                });
        return image;
    }


    @Override
    public void onBackPressed() {
        //openDialogFragment(null);
    }
    public void onCancelClick(View view){
        finish();

    }
}