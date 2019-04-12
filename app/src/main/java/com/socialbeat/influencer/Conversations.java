package com.socialbeat.influencer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
    Button showtext,show_file,show_button;
    TextView contentstatus,contentshared,view_conversation,filename,campaignname;
    String userChoosenTask,success,message,cid,fcid,campid,fcampid,campname,fsocialmedia,feditor;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    long totalSize = 0;
    SharedPreferences.Editor editor;
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    private ProgressDialog pdialog;
    private String imagepath=null;
    private String filePath=null;

    public static final String KEY_APPNAME = "Influencer";
    public static final String KEY_CID = "cid";
    public static final String KEY_CAMPID = "campid";
    public static final String KEY_EDITOR = "editor";
    public static final String KEY_ATTACHMENT = "attachment";



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
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            campid = extras.getString("campid");
            campname = extras.getString("campname");
            Log.v("Conversation CCampid : ",campid);
            Log.v("Conversation CCampnme: ",campname);
        }else {
            SharedPreferences prefernce1 = getSharedPreferences("COMPLETE_CAMP", Context.MODE_PRIVATE);
            campid = prefernce1.getString("campid", "");
            campname = prefernce1.getString("campname", "");
            Log.v("campid Value : ",campid);
            Log.v("C value : ","Empty");
            Log.v("Conversation CCampid : ", campid);
            Log.v("Conversation CCampnme: ", campname);
        }

        contentstatus = findViewById(R.id.contentstatus);
       // contentshared = findViewById(R.id.contentshared);
        view_conversation = findViewById(R.id.view_conversation);
        campaignname = findViewById(R.id.campName);
        show_file = findViewById(R.id.show_file);
        summernote = findViewById(R.id.summernote);
        //showtext = findViewById(R.id.show_button);
        show_button = findViewById(R.id.show_button);
        ivImage = findViewById(R.id.ivImage);
        filename = findViewById(R.id.filename);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        summernote.setRequestCodeforFilepicker(5);

//        SharedPreferences prefernce = getSharedPreferences("COMPLETE_CAMP", Context.MODE_PRIVATE);
//        editor = prefernce.edit();
//        editor.clear();
//        editor.apply();

        SharedPreferences prefernce2 = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prefernce2.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);


        if(cid.length()!=0){
            if (isInternetPresent) {
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });
                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_LONG).show();
        }
        campaignname.setText(campname);

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
                showFileChooser();
            }
        });

        view_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences preferences1 = getSharedPreferences("CONVERSATION_CAMP_CONTENT", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor1 = preferences1.edit();
//                editor1.putString("cid",cid);
//                editor1.putString("campid",campid);
//                System.out.println("value of conversation campid : "+campid);
//                editor1.apply();

                Fragment fragment = new ViewConversationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("campid", campid);
                bundle.putString("campname", campname);
                Log.v("Con CCampid : ",campid);
                Log.v("Con CCampnme : ",campname);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.newframeid, fragment );
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {


                        fcid = cid;
                        fcampid = campid;
                        feditor = summernote.getText();

                        Log.v("con FCID",fcid);
                        Log.v("con FCAMPID",fcampid);
                        Log.v("con FEDITOR",feditor);

                        new UploadFileToServer().execute();


                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("SETTINGS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }

        });
    }

    //method to show file chooser
    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("application/pdf");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "image/*"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
//            imagepath = getPath(selectedImageUri);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
//            ivImage.setImageBitmap(bitmap);
            //filePath = selectedImageUri.toString();
            filePath =FilePath.getPath(this, selectedImageUri);
            filename.setText("Uploading file path:" + filePath);
        }
    }

//    private String getPath(Uri selectedImageUri) {
//    }

    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(Conversations.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String REGISTER_URL = "https://www.influencer.in/API/v6/api_v6.php/addNewConversationSample";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                if ((TextUtils.isEmpty(filePath))) {
                    Log.v("reachfile","Empty");
                }else{
                    File sourceFile = new File(filePath);
                    System.out.println("sourceFile of reach :" + sourceFile );
                    // Adding file data to http body
                    entity.addPart(KEY_ATTACHMENT, new FileBody(sourceFile));
                    // Extra parameters if you want to pass to server
                }

                entity.addPart(KEY_CID, new StringBody(cid));
                entity.addPart(KEY_CAMPID, new StringBody(fcampid));
                entity.addPart(KEY_EDITOR, new StringBody(feditor));
                


                //totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();

            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        public void onPostExecute(String success) {
            try {
                JSONObject json = new JSONObject(success);
                success = json.getString("success");
                message = json.getString("message");
                Log.v("success", success);
                Log.v("message", message);
                pdialog.dismiss();

                if (success == "true") {
//                    if (fileDesPath.isDirectory()) {
//                        String[] children = fileDesPath.list();
//                        for (int i = 0; i < children.length; i++) {
//                            new File(fileDesPath, children[i]).delete();
//                        }
//                        fileDesPath.delete();
//                    }
                    //Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Conversations.this, NewHomeActivity.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                    Toast.makeText(Conversations.this, message, Toast.LENGTH_LONG).show();
                } else if (success == "false") {
                    Toast.makeText(Conversations.this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}