package com.example.redfoxoptimaiii.farmaide;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddInventoryActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
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

        final String[] items = new String[]{"Take Photo", "Choose Photo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/", "temp.JPG");
                    imageCaptureUri = Uri.fromFile(file);
                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        intent.putExtra("return data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
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
        if (requestCode == PICK_FROM_FILE) {
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

    public String getRealPathFromURI(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void addInventory(View view) {
        TextInputLayout input_name = (TextInputLayout) findViewById(R.id.input_ingredient_name);
        String feed_type = ((Spinner) findViewById(R.id.spinner_type)).getSelectedItem().toString();
        String feed_name = ((EditText) findViewById(R.id.ingredient_name)).getText().toString().trim();
        String dm = ((EditText) findViewById(R.id.editText_dm)).getText().toString();
        String cp = ((EditText) findViewById(R.id.editText_cp)).getText().toString();
        String me = ((EditText) findViewById(R.id.editText_me)).getText().toString();
        String ca = ((EditText) findViewById(R.id.editText_ca)).getText().toString();
        String p = ((EditText) findViewById(R.id.editText_p)).getText().toString();
        String tdn = ((EditText) findViewById(R.id.editText_tdn)).getText().toString();
        String supply = ((EditText) findViewById(R.id.editText_supp)).getText().toString();
        String price = ((EditText) findViewById(R.id.editText_price)).getText().toString();
        input_name.setErrorEnabled(false);
        boolean check = true;
        if (feed_name.isEmpty()) check = false;
        if (dm.isEmpty()) check = false;
        if (cp.isEmpty()) check = false;
        if (me.isEmpty()) check = false;
        if (ca.isEmpty()) check = false;
        if (p.isEmpty()) check = false;
        if (tdn.isEmpty()) check = false;
        if (supply.isEmpty()) check = false;
        if (price.isEmpty()) check = false;
        if (imageCaptureUri == null || imageCaptureUri.getPath().isEmpty()) {
            Toast.makeText(this, "Choose an image for the ingredient", Toast.LENGTH_SHORT).show();
            check = false;
        }
        if (!check)
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        else {
            try {
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getWritableDatabase();
                cursor = db.query("FEED",
                        new String[]{"feed_name"},
                        "farm_id=? AND feed_name=?",
                        new String[]{Integer.toString(Admin.farm_id), feed_name},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    input_name.setError("Feed name already exists");
                    check = false;
                }
                if (check) {
                    ((FarmAideDatabaseHelper) FarmAideDBHelper).insertFeed(db, Admin.farm_id, feed_type, feed_name, Double.parseDouble(dm),
                            Double.parseDouble(tdn), Double.parseDouble(price), Double.parseDouble(supply), Double.parseDouble(cp), Double.parseDouble(me),
                            Double.parseDouble(ca), Double.parseDouble(p), imageViewToByte(mImageView));
                    Toast.makeText(this, "Successfully added ingredient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Admin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("username", Admin.username);
                    intent.putExtra("farm_id", Admin.farm_id);
                    startActivity(intent);
                    finish();
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) {
            }
        }
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] buffer = out.toByteArray();
        return buffer;
    }
}