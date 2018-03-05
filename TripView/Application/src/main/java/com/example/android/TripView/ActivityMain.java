package com.example.android.TripView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.camera2basic.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Vishal on 4/3/2017.
 */

public class ActivityMain extends AppCompatActivity {
    Button addTrip;
    private ListView tripList;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_selection);
        addTrip = findViewById(R.id.addTrip);
        addTrip.setText("Create a Trip");
        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, AddTripActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        tripList = (ListView) findViewById(R.id.list);
        stringArrayList = new ArrayList<String>();
        File dir = ActivityMain.this.getFilesDir();
        File[] subFiles = dir.listFiles();

        if (subFiles != null)
        {
            for (File file : subFiles)
            {
                if(file.getPath().endsWith(".txt"))
                    stringArrayList.add(file.getName());
            }
        }

        //for (int i = 0; i < 30; i++){
            //stringArrayList.add("Trip #" + i);
        //}
        stringArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, stringArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };
        tripList.setAdapter(stringArrayAdapter);
        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                String selectedFromList = (String)(tripList.getItemAtPosition(position));
                String[] filedata = readFromFile(ActivityMain.this, selectedFromList);
                Intent intent = new Intent(ActivityMain.this, TripView.class);
                intent.putExtra("tripName", selectedFromList);
                intent.putExtra("startDate", filedata[1]);
                intent.putExtra("endDate", filedata[2]);
                startActivity(intent);
            }
        });
    }

    public void test(View view)
    {
        Intent intent = new Intent(this, TripView.class);
        startActivity(intent);
    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: Checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ActivityMain.this);
        if (available == ConnectionResult.SUCCESS){
            //Everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google PLay Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ActivityMain.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private String[] readFromFile(Context context, String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        String[] separated = ret.split(",");
        return separated;
    }
}

