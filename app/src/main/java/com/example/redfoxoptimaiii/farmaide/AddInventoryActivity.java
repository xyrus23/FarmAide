package com.example.redfoxoptimaiii.farmaide;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class AddInventoryActivity extends AppCompatActivity {

    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final String[] items = new String[] {"Take Photo", "Choose Photo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),"ingredient1");
                    imageCaptureUri = Uri.fromFile(file);
                    try{
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageCaptureUri);
                        intent.putExtra("return data",true);

                        startActivityForResult(intent,PICK_FROM_CAMERA);
                    } catch (Exception e){ }
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Complete action using"),PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        mImageView = (ImageView) findViewById(R.id.imageView_pic);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Bitmap bitmap = null;
        String path = "";
        if (requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri);
            if (path == null) path = imageCaptureUri.getPath();
            if (path != null) bitmap = BitmapFactory.decodeFile(path);
        } else {
            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        mImageView.setImageBitmap(bitmap);
    }

    public String getRealPathFromURI(Uri contentURI){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI,proj,null,null,null);
        if(cursor==null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void addInventory(View view){
        String ingredient_name = ((EditText)findViewById(R.id.ingredient_name)).getText().toString();
        String dm = ((EditText)findViewById(R.id.editText_dm)).getText().toString();
        String cp = ((EditText)findViewById(R.id.editText_cp)).getText().toString();
        String me = ((EditText)findViewById(R.id.editText_me)).getText().toString();
        String ca = ((EditText)findViewById(R.id.editText_ca)).getText().toString();
        String p = ((EditText)findViewById(R.id.editText_p)).getText().toString();
        String tdn = ((EditText)findViewById(R.id.editText_tdn)).getText().toString();
        String supply = ((EditText)findViewById(R.id.editText_supp)).getText().toString();
        String price = ((EditText)findViewById(R.id.editText_price)).getText().toString();

        boolean check = true;
        if (ingredient_name.isEmpty()) check = false;
        if (dm.isEmpty()) check = false;
        if (cp.isEmpty()) check = false;
        if (me.isEmpty()) check = false;
        if (ca.isEmpty()) check = false;
        if (p.isEmpty()) check = false;
        if (tdn.isEmpty()) check = false;
        if (supply.isEmpty()) check = false;
        if (price.isEmpty()) check = false;
        if (imageCaptureUri.getPath().isEmpty())
            Toast.makeText(this, "Choose an image for the ingredient", Toast.LENGTH_SHORT).show();
        if (!check) Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();

    }
}