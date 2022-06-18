package com.example.socialmediadownloadeapp

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_imageview.*
import org.apache.commons.io.FileUtils
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths


class imageview : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview)


        val uri : String = intent.getStringExtra("uri").toString()
        val path : String = intent.getStringExtra("path").toString()

        var image1 = findViewById<ImageView>(R.id.statusimage)
        Glide.with(this).load(Uri.parse(uri)).into(image1)


        imageback.setOnClickListener {
            super.onBackPressed()
        }

            imageshare.setOnClickListener {

                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/jpeg"
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
                startActivity(Intent.createChooser(share, "Share Image"))

            }

            imagedownload.setOnClickListener {

                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(uri))

                    val fileName = "${System.currentTimeMillis()}.jpg"
                    var fos: OutputStream? = null

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentResolver.also { resolver ->
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                                put(
                                    MediaStore.MediaColumns.RELATIVE_PATH,
                                    Environment.DIRECTORY_PICTURES + "/StatusSaverImages/"
                                )
                            }

                            val imageUri = resolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues
                            )
                            fos = imageUri?.let { resolver.openOutputStream((it)) }

                        }
                    } else {
                        val imagedir =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        val image = File(imagedir, fileName)
                        fos = FileOutputStream(image)
                    }

                    fos?.use {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

                        Toast.makeText(applicationContext, "Image Saved", Toast.LENGTH_LONG).show()
                    }

                }
            }
    }



    fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted1")
                true
            } else {
                Log.v(TAG, "Permission is revoked1")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    3
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1")
            true
        }
    }

    fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted2")
                true
            } else {
                Log.v(TAG, "Permission is revoked2")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2")
            true
        }
    }


}