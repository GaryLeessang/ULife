package com.example.joe.cityumobile.View.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.DataModel.BmobModel.RegisterRequest;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Dialog.SubmitFailedDialog;
import com.example.joe.cityumobile.View.Dialog.SubmitSuccessDialog;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends BaseActivity {

    Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    private Switch announceSwitch;
    private TextView announcement;
    private ImageView idCardPhoto;
    private ImageButton uploadPhoto;
    private Button registerBtn;
    private ProgressBar progressBar;
    private TextView name;
    private TextView email;

    private Uri cardUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setImmersiveStatusBar();

        findAndBindView();
    }

    private void findAndBindView(){
        announceSwitch = findViewById(R.id.announceSwitch);
        announcement = findViewById(R.id.registerDescrip);
        idCardPhoto = findViewById(R.id.idCardPhoto);
        uploadPhoto = findViewById(R.id.uploadButton);
        registerBtn = findViewById(R.id.submitRegister);
        progressBar = findViewById(R.id.progressBar);
        name = findViewById(R.id.nameInput);
        email = findViewById(R.id.emailInput);

        announceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    announcement.setVisibility(View.VISIBLE);
                }else{
                    announcement.setVisibility(View.GONE);
                }
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setState("unread");
                registerRequest.setStudentName("Kami");
                registerRequest.setStudentID("55469991");
                registerRequest.setEmail("55469991@my.cityu.edu.hk");

                if (cardUri != null && validInfo()){
                    Log.e("My","not Null uri");

                    String path = getImagePath(cardUri,null);
                    final BmobFile img = new BmobFile(new File(path));
                    registerRequest.setCardPhoto(img);
                    Utils.note("Submiting...");
                    img.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            Log.e("My","Executed");
                            if (e == null){
                                registerRequest.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException ex) {
                                        if (ex == null){
                                            SubmitSuccessDialog dialog = new SubmitSuccessDialog.Builder(v.getContext()).create();
                                            dialog.show();
                                            //Utils.note("Success");
                                        }else{
                                            //Utils.note("Failed");
                                            SubmitFailedDialog dialog = new SubmitFailedDialog.Builder(v.getContext()).create();
                                            dialog.show();
                                        }
                                    }
                                });
                            }else{
                                //Utils.note("Failed");
                                SubmitFailedDialog dialog = new SubmitFailedDialog.Builder(v.getContext()).create();
                                dialog.show();
                            }
                        }
                    });

                }else {
                    Log.e("My","Null uri");
                    SubmitFailedDialog dialog = new SubmitFailedDialog.Builder(v.getContext()).create();
                    dialog.show();
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean validInfo(){
        if (name.getText().toString().isEmpty()){
            return false;
        }
        if (!email.getText().toString().endsWith("cityu.edu.hk")){
            return false;
        }
        return true;
    }

    private void selectImage(){

        final CharSequence[] items={"Select From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add a picture");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Select From Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                idCardPhoto.setImageBitmap(bmp);
                cardUri = data.getData();

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                cardUri = selectedImageUri;
                idCardPhoto.setImageURI(selectedImageUri);
            }

        }
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 设置监听器
     */
    @Override
    public void setListener() {

    }
}
