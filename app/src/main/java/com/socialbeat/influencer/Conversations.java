package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import in.nashapp.androidsummernote.Summernote;

public class Conversations extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Summernote summernote;
    Button showtext,show_file;
    TextView contentstatus,contentshared,view_conversation;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Conversations");
        contentstatus = findViewById(R.id.contentstatus);
        contentshared = findViewById(R.id.contentshared);
        view_conversation = findViewById(R.id.view_conversation);
        show_file = findViewById(R.id.show_file);
        summernote = findViewById(R.id.summernote);
        showtext = findViewById(R.id.show_button);
        ivImage = findViewById(R.id.ivImage);

        summernote.setRequestCodeforFilepicker(5);

        showtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String html_data = summernote.getText();
                Toast.makeText(getApplicationContext(),html_data, Toast.LENGTH_SHORT).show();
                System.out.println(html_data);
            }
        });

        view_conversation.setPaintFlags(view_conversation.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        view_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String html_data = summernote.getText();
                Toast.makeText(getApplicationContext(),"Chat Converstation is under Contruction", Toast.LENGTH_SHORT).show();
                System.out.println(html_data);
            }
        });

        show_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Add File Clicked", Toast.LENGTH_SHORT).show();
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Conversations.this);
        builder.setTitle("ADD IMAGE");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Conversations.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

            private void galleryIntent()
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
            }

            private void cameraIntent()
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }


        });
        
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
    }
}