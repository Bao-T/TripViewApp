package com.example.android.TripView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.android.camera2basic.R;


import org.w3c.dom.Text;

import java.util.Calendar;



/**
 * Created by Bao on 3/4/2018.
 */

public class AddTripActivity extends AppCompatActivity {
    private TextView startDate;
    private TextView endDate;
    private TextView title;
    private TextView description;
    private Button save;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private Calendar sDate = Calendar.getInstance();
    private Calendar eDate = Calendar.getInstance();
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
                startDate.setText(month + "/" + dayOfMonth + "/" + year);
                sDate.set(year, month, dayOfMonth);
            }
        };
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate.setText(month + "/" + dayOfMonth + "/" + year);
                eDate.set(year, month, dayOfMonth);
            }
        };

        title = (TextView) findViewById(R.id.TripTitle);
        description = (TextView) findViewById(R.id.description);

        save = (Button) findViewById(R.id.saveButton);



    }

}
