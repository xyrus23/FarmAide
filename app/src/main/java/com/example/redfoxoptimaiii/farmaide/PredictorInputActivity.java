package com.example.redfoxoptimaiii.farmaide;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class PredictorInputActivity extends AppCompatActivity {

    private double[][] dataX;
    private double[][] dataY;
    private double predictedOutput, r_squared, adj_r_squared;
    private int cow_id;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictor_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cow_id = Integer.parseInt(getIntent().getStringExtra("cow_id"));
        int sizeX = getIntent().getIntExtra("sizeX", 0);
        int cols = getIntent().getDoubleArrayExtra("dataX0").length;
        dataX = new double[sizeX][cols];
        for (int i=0;i<sizeX;i+=1)
            for (int j = 0; j < cols; j += 1)
                dataX[i][j] = getIntent().getDoubleArrayExtra("dataX" + i)[j];
        MLR mlr = new MLR(dataX);
        dataY = new double[dataX.length][1];
        for (int i=0;i<dataX.length;i+=1){
            dataY[i][0] = dataX[i][0];
            dataX[i][0] = 1;
        }
        adj_r_squared = mlr.getAdj_r_sqrd()*100;
        String value = String.format("%.2f", adj_r_squared);
        ((TextView)findViewById(R.id.adj_r_squared)).setText("Adequacy Fit: "+value+"%");
        if (adj_r_squared > 50) ((TextView)findViewById(R.id.adj_r_squared)).setTextColor(getResources().getColor(R.color.colorPrimary));
        else ((TextView)findViewById(R.id.adj_r_squared)).setTextColor(Color.RED);
    }

    public void predict(View view){
        final String[] input = new String[dataX[0].length-1];
        input[0] = ((EditText)findViewById(R.id.ld)).getText().toString();
        input[1] = ((EditText)findViewById(R.id.fy)).getText().toString();
        input[2] = ((EditText)findViewById(R.id.py)).getText().toString();
        input[3] = ((EditText)findViewById(R.id.tsy)).getText().toString();

        double[][] in = new double[1][dataX[0].length];
        if (input[0].isEmpty() || input[1].isEmpty() || input[2].isEmpty() || input[3].isEmpty())
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        else {
            for (int i=0;i<in[0].length;i+=1) {
                if (i==0) in[0][i] = 1;
                else in[0][i] = Double.parseDouble(input[i-1]);
            }

            MLR mlr = new MLR(dataX, dataY, in);
            predictedOutput = mlr.getPredictedOutput();
            r_squared = mlr.getR_squared()*100;
            adj_r_squared = mlr.getAdj_r_sqrd()*100;

            String pvalue = String.format("%.2f", predictedOutput);

            //SHOW OUTPUT
            final Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            LayoutInflater inflater =  (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View snackView = inflater.inflate(R.layout.snackbar_prediction, null);
            ((TextView)snackView.findViewById(R.id.predicted_output)).setText(pvalue+"kg");
            snackView.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(PredictorInputActivity.this);
                        db = FarmAideDBHelper.getReadableDatabase();
                        Date date = new Date();
                        String time_stamp = Admin.months[date.getMonth()] + "-" + date.getDate() + "-" + (date.getYear()+1900);
                        ((FarmAideDatabaseHelper)FarmAideDBHelper).insertMilkYield(db,cow_id,Admin.farm_id,predictedOutput,Integer.parseInt(input[0]),Double.parseDouble(input[1]),Double.parseDouble(input[2]),Double.parseDouble(input[3]),time_stamp);
                        Toast.makeText(PredictorInputActivity.this, "Successfully saved milk yield", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PredictorInputActivity.this, Admin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("username", Admin.username);
                        intent.putExtra("farm_id", Admin.farm_id);
                        intent.putExtra("position", 3);
                        startActivity(intent);
                        finish();
                        db.close();
                    } catch (SQLiteException e){}
                }
            });
//            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    snackbar.dismiss();
//                }
//            });
            layout.addView(snackView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            snackbar.show();
        }
    }
}
