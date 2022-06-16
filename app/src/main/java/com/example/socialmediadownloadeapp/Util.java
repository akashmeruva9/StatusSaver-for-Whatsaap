package com.example.socialmediadownloadeapp;

import android.os.Environment;

import java.io.File;

public class Util {

    public static File RootDirectory = new File(
            Environment.getExternalStorageDirectory() + "/Download/StatusSaver");

    public static  void createFileFolder()
    {
        if(!RootDirectory.exists())
            RootDirectory.mkdirs();
    }
}
