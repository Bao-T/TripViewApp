package com.example.android.TripView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.camera2basic.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bao on 2/26/2018.
 */

public class TripView extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerView rvMain;
    String currentTrip;
    TextView currTrip;
    Button camButton;
    private ProgressDialog pd;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private ArrayList<placeObject> locations = new ArrayList<placeObject>();
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_view);
        //Create the Google Maps Fragment
        //getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rvMain = (RecyclerView) findViewById(R.id.rvMain);
        currTrip = (TextView) findViewById(R.id.currTrip);
        camButton = (Button) findViewById(R.id.camButton);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripView.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        currentTrip = getIntent().getStringExtra("tripName");
        currTrip.setText(currentTrip);
        GenerateImageTask ImageSetup = new GenerateImageTask();
        ImageSetup.execute();
        MyAdapter adapter = new MyAdapter(locations);



    }

    private class placeObject {
        public MarkerOptions markerOptions;
        public Bitmap bitmap;
        public String imagePath;
        public Marker marker;
        public Boolean hasGPS = false;

        public placeObject(MarkerOptions markerOptions, Bitmap bitmap, String imagePath) {
            this.markerOptions = markerOptions;
            this.bitmap = bitmap;
            this.imagePath = imagePath;
            if (this.markerOptions.getPosition().latitude != 0.0 && this.markerOptions.getPosition().longitude != 0.0)
                this.hasGPS = true;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {


        //ArrayList<placeObject> places;

        public MyAdapter(ArrayList<placeObject> places) {
            //this.places = places;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.logo.setImageBitmap(locations.get(position).bitmap);
            holder.logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(TripView.this, "This is: clicked", Toast.LENGTH_SHORT).show();
                    if (locations.get(position).hasGPS == true) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(position).markerOptions.getPosition(), (float) 20.0));
                        locations.get(position).marker.showInfoWindow();
                    } else
                        Toast.makeText(TripView.this, "No GPS data", Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return locations.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView logo;


        public MyViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.ivLogo);

        }
    }

    public void decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);


        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);
        String exif = "Exif: " + path;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            //exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            //exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            //exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            //exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            //exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            //exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            //exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            //exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            //exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            //exif += "\nGPS related:";
            //exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            //exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            //exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            //exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            //exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            //exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            //exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

            boolean valid = false;
            Float Longitude = (float) 0;
            Float Latitude = (float) 0;
            String attrLATITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String attrLATITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String attrLONGITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String attrLONGITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

            if ((attrLATITUDE != null) && (attrLATITUDE_REF != null) && (attrLONGITUDE != null) && (attrLONGITUDE_REF != null)) {
                valid = true;
                if (attrLATITUDE_REF.equals("N")) {
                    Latitude = convertToDegree(attrLATITUDE);
                } else {
                    Latitude = 0 - convertToDegree(attrLATITUDE);
                }

                if (attrLONGITUDE_REF.equals("E")) {
                    Longitude = convertToDegree(attrLONGITUDE);
                } else {
                    Longitude = 0 - convertToDegree(attrLONGITUDE);
                }
            }
            //locations.add(new MarkerOptions().position(new LatLng(Latitude,Longitude)).title("Place "));
            //Log.d("exif", Float.toString(Latitude) + Float.toString(Longitude));
            locations.add(new placeObject(
                    new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(path),
                    bm, path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return bm;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    private Float convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        //LatLng sydney = new LatLng(-33.852, 151.211);
        //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap = googleMap;
        for (placeObject placeObject : locations) {
            if (placeObject.markerOptions.getPosition().longitude != 0 && placeObject.markerOptions.getPosition().latitude != 0)
                placeObject.marker = googleMap.addMarker(placeObject.markerOptions);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0).markerOptions.getPosition()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        mLocationPermissionsGranted = false;
                        return;
                    }
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();
                }
            }
        }
    }

    private class GenerateImageTask extends AsyncTask<Long, String, Long>{
        @Override
        protected Long doInBackground(Long... arg0){
            ImageManager im = new ImageManager();
            ArrayList<String> images = im.getPlayList();
            int count = 0;
            for (String image : images) {
                if (count <= 100)
                    decodeSampledBitmapFromUri(image, 50, 50);
                count++;
            }
            MyAdapter adapter = new MyAdapter(locations);
            rvMain.setLayoutManager(new GridLayoutManager(TripView.this, 4));
            rvMain.setAdapter(adapter);
            return null;
        }
        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
        //Your main background view code ends up falling here
            pd.dismiss();
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//Your optional view and where it starts
            pd = ProgressDialog.show(TripView.this, "Loading", "Please wait...");
        }
    }
}

