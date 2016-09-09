package com.daveanthonythomas.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener {

    public ImageView view;
    private String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_PERMISSION = 5;
    CustomAdapter customAdapter;
    ListView listView;
    Intent intent;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customAdapter = new CustomAdapter();
        listView = (ListView) findViewById(R.id.myListView);
        listView.setAdapter(customAdapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        //view = (ImageView) this.findViewById(R.id.imageIcon);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[0])
                != MockPackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[1])
                        != MockPackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    mPermission, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Memo memo = customAdapter.getItem(position);
        intent = new Intent(getApplicationContext(), CheckOutMemo.class);
        intent.putExtra("header", memo.header);
        intent.putExtra("bodyText", memo.bodyText);
        intent.putExtra("position", position);

        // launches edit request and saving existing item.
        startActivityForResult(intent, CheckOutMemo.EDIT_REQUEST_CODE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Confirm Delete");
        alertDialogBuilder.setMessage("Delete memo?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                customAdapter.delete(position);
                customAdapter.notifyDataSetChanged();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return true;
    }

    public void addNewNote(View view) {
        Intent intent = new Intent(getApplicationContext(), CheckOutMemo.class);
        //Adding new listItem to the ArrayList.
        startActivityForResult(intent, CheckOutMemo.ADD_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == CheckOutMemo.ADD_REQUEST_CODE) {
            String header = data.getStringExtra("header");
            String bodyText = data.getStringExtra("bodyText");
            File photo = new File(data.getStringExtra("photo"));
            ImageView view = new ImageView(this);

            if (photo.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
                view.setImageBitmap(myBitmap);
            }

            Memo memo = new Memo(header, bodyText, view);
            customAdapter.add(memo);
            customAdapter.notifyDataSetChanged();
        }

        if (requestCode == CheckOutMemo.EDIT_REQUEST_CODE) {

            int position = data.getIntExtra("position", 0);
            Memo memo = customAdapter.getItem(position);
            memo.header = data.getStringExtra("header");
            memo.bodyText = data.getStringExtra("bodyText");
            File photo = new File(data.getStringExtra("photo"));

            if (photo.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
                memo.imageView.setImageBitmap(myBitmap);
            }
            customAdapter.notifyDataSetChanged();
        }
    }
}
