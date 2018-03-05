package com.example.android.TripView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.camera2basic.R;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;



/**
 * Created by Bao on 3/4/2018.
 */

public class AddTripActivity extends AppCompatActivity{
    private TextView startDate;
    private TextView endDate;
    private TextView title;
    private TextView description;
    private Button save;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private Calendar sDate = Calendar.getInstance();
    private Calendar eDate = Calendar.getInstance();

    private String blockCharacterSet = "~`!@#$%^&*()_+-={[}]|\';<,>.?\"/:";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trip_view);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = sDate.get(Calendar.YEAR);
                int month = sDate.get(Calendar.MONTH);
                int day = sDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddTripActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, startDateListener,year, month, day);
                dialog.getDatePicker().setMaxDate(eDate.getTimeInMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = eDate.get(Calendar.YEAR);
                int month = eDate.get(Calendar.MONTH);
                int day = eDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddTripActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, endDateListener,year, month, day);
                dialog.getDatePicker().setMinDate(sDate.getTimeInMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDate.setText((month+1) + "/" + dayOfMonth + "/" + year);
                sDate.set(year, month, dayOfMonth);
            }
        };
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate.setText((month+1) + "/" + dayOfMonth + "/" + year);
                eDate.set(year, month, dayOfMonth);
            }
        };

        title = (TextView) findViewById(R.id.TripTitle);
        title.setFilters(new InputFilter[]{filter});
        description = (TextView) findViewById(R.id.description);

        save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getApplicationContext().getFilesDir(),title.getText().toString()+ ".txt");
                if(file.exists()){
                    Toast.makeText(AddTripActivity.this, "This title already exists.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String data = title.getText().toString() + "/" + startDate.getText().toString() + "/" + endDate.getText().toString();
                    writeToFile(data, AddTripActivity.this, title.getText().toString());
                }
            }
        });



    }
    private void writeToFile(String data,Context context,String title) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput( title + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
