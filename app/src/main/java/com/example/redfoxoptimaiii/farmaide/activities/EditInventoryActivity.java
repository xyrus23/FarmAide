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

public class EditInventoryActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    Button btn_upload;
    private int feed_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        EditText editText_feed_name = (EditText) findViewById(R.id.ingredient_name);
        EditText editText_dm = (EditText) findViewById(R.id.editText_dm);
        EditText editText_cp = (EditText) findViewById(R.id.editText_cp);
        EditText editText_me = (EditText) findViewById(R.id.editText_me);
        EditText editText_ca = (EditText) findViewById(R.id.editText_ca);
        EditText editText_p = (EditText) findViewById(R.id.editText_p);
        EditText editText_tdn = (EditText) findViewById(R.id.editText_tdn);
        EditText editText_supply = (EditText) findViewById(R.id.editText_supp);
        EditText editText_price = (EditText) findViewById(R.id.editText_price);
        mImageView = (ImageView) findViewById(R.id.imageView_pic);

        String feed_name = getIntent().getStringExtra("feed_name");
        try {
            byte[] pic;
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("FEED",
                    new String[]{"feed_id,feed_name,dry_matter,total_digestible_nutrient,feed_price,supply_amount,crude_protein," +
                            "met_energy,calcium,phosphorus,pic_ref"},
                    "farm_id=? AND feed_name=?",
                    new String[]{Integer.toString(Admin.farm_id), feed_name},
                    null, null, null);
            if (cursor.moveToFirst()) {
                feed_id = cursor.getInt(0);
                editText_feed_name.setText(cursor.getString(1));
                editText_dm.setText(cursor.getString(2));
                editText_cp.setText(cursor.getString(6));
                editText_me.setText(cursor.getString(7));
                editText_ca.setText(cursor.getString(8));
                editText_p.setText(cursor.getString(9));
                editText_tdn.setText(cursor.getString(3));
                editText_supply.setText(cursor.getString(5));
                editText_price.setText(cursor.getString(4));
                pic = cursor.getBlob(10);
                Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                mImageView.setImageBitmap(bitmap);
            }
        } catch (SQLiteException e) {
        }


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

    public void editInventory(View view) {
        TextInputLayout input_name = (TextInputLayout) findViewById(R.id.input_ingredient_name);
        String feed_type = ((Spinner) findViewById(R.id.spinner_type)).getSelectedItem().toString();
        String feed_name = ((EditText) findViewById(R.id.ingredient_name)).getText().toString();
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
        if (!check)
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        else {
            try {
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("FEED",
                        new String[]{"feed_id, feed_name"},
                        "farm_id=? AND feed_name=?",
                        new String[]{Integer.toString(Admin.farm_id), feed_name},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    if (cursor.getInt(0) != feed_id) {
                        input_name.setError("Feed name already exists");
                        check = false;
                    }
                }
                if (check) {
                    ((FarmAideDatabaseHelper) FarmAideDBHelper).updateFeed(db, feed_id, feed_type, feed_name, Double.parseDouble(dm),
                            Double.parseDouble(tdn), Double.parseDouble(price), Double.parseDouble(supply), Double.parseDouble(cp), Double.parseDouble(me),
                            Double.parseDouble(ca), Double.parseDouble(p), imageViewToByte(mImageView));
                    Toast.makeText(this, "Successfully edited ingredient", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AdminInventoryDetailActivity.class);
                    intent.putExtra("feed_name", feed_name);
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
