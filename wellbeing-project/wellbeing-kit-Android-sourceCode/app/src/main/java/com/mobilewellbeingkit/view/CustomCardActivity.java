package com.mobilewellbeingkit.view;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.model.Card;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CustomCardActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int CHOOSE_FROM_ALBUM = 2;

    private ImageView picture;
    private Card customCard;
    Uri imageUri_temp;
    private String mCurrentPhotoPath;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_card);

        Button takePhoto = findViewById(R.id.take_photo);
        Button selectFromAlbum = findViewById(R.id.select_from_album);
        Button createNewCard = findViewById(R.id.creatCustomCard);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); //  Needs a blank string or it will put name of the app up top

        picture = findViewById(R.id.card_image_select);

        //the listener of take photo button
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        /*
        * after choose the photo from the album, the custom activity would start up.
        * */
        selectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(CustomCardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CustomCardActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else{
                    openAlbum();
                }
            }
        });

        /*
         * after user enter the custom interface, the user must typed card name, select image and also can be favourite and duplicated.
         * after finished the custom card, the card activity would start up.
         */
        createNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameTemp = findViewById(R.id.card_name);
                String customCardName = nameTemp.getText().toString();
                //check the Name of the custom card is null or not
                if(customCardName.equals("")){
                    Toast.makeText(getApplicationContext(), "You must input a name for the card",
                            Toast.LENGTH_SHORT).show();

                    //check the photo has been selected or not
                } else if(mCurrentPhotoPath == null) {
                    Toast.makeText(getApplicationContext(), "You must select an image for the card",
                            Toast.LENGTH_SHORT).show();
                } else {
                    customCard = new Card(customCardName, null, 0, 0, mCurrentPhotoPath);

                    Intent returnIntent = new Intent(CustomCardActivity.this, CardsActivity.class);
                    returnIntent.putExtra("newCardName", customCard.getCardName());
                    returnIntent.putExtra("newCardImage", customCard.getCardImage());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    /**
    * Checking the photo if taken from the  camera or choose from album,
    * moreover checking the orientation rotation of the photo , and fixed the compatibility in different versions
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the user want to take photo or choose from album
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            if(imgFile.exists()) {
                displayImage(mCurrentPhotoPath);
            }

        } else if (requestCode == CHOOSE_FROM_ALBUM && resultCode == RESULT_OK) {
            // Determine the mobile phone system version number
            if(Build.VERSION.SDK_INT >= 19){
                //for upper than version 4.4
                handleImageOnKitKat(data);
                } else {
                //for lower than version 4.4
                handImageBeforeKitKat(data);
            }

        }
    }


    /**
     * write the directory, suffix, prefix for the photo, which has been taken by the user
     * @return File newly created image file
     **/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "MYWELLBEINGKIT_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Dispatches an intent to take a photo with device camera
     *
     **/
    private void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile;
            try {
                pictureFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.mobilewellbeingkit.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    /**
      * Launches an intent to browse gallery for an image
      */
    private void openAlbum(){
        Intent AlbumIntent = new Intent("android.intent.action.GET_CONTENT");
        AlbumIntent.setType("image/*");
        //open the album
        startActivityForResult(AlbumIntent,CHOOSE_FROM_ALBUM);
    }



    /**
     * Handle image selection on devices above api19
     * @param data Intent to launch
     */
    @TargetApi(19)
    private void  handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        imageUri_temp = uri;
        //if the Uri type is document, then operate through document ic
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);

            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//Parse the id to number format
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);

        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();//get the path of image
        }
        mCurrentPhotoPath = imagePath;
        displayImage(imagePath);
    }

    /**
     * Handle image picking on devices lower than api19
     * @param data Intent to launch
     */
    private void handImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        imageUri_temp = uri;
        String imagePath = getImagePath(uri,null);
        mCurrentPhotoPath = imagePath;
        displayImage(imagePath);
    }


    /**
     * Get the image path given a uri and name
     * @param uri the image uri
     * @param selection the image to be selected
     * @return String image path
     */
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //get the real path of the image through Uri and selection
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
      * Displays the image in custom card view
      *
      * @param imagePath
      *          the image path as a String
      */
    private void displayImage(String imagePath){
        if(imagePath != null){

            progressBar.setVisibility(View.VISIBLE);

            // Utilising Glide here to load a resized image more memory efficiently.
            Glide.with(this)
                    .load(imagePath)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(picture);
        }else{
            Toast.makeText(this,"Invalid image path",Toast.LENGTH_SHORT).show();
        }
    }
}









