package com.example.android.TripView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;

public class ImageManager {
    // SDCard Path
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath();
    private ArrayList<String> imageList = new ArrayList<String>();
    private String jpgPattern = ".jpg";
    private String pngPattern = ".JPG";

    // Constructor
    public ImageManager() {

    }

    public ArrayList<String> getPlayList() {
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addImageToList(file);
                    }
                }
            }
        }
        // return songs list array
        return imageList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addImageToList(file);
                    }

                }
            }
        }
    }

    private void addImageToList(File image) {

//adding JPEG and PNG format Images
        if (image.getName().endsWith(jpgPattern) || image.getName().endsWith(pngPattern)) {
            imageList.add(image.getPath());
        }
    }
}
