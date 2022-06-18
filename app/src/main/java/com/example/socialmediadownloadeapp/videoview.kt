package com.example.socialmediadownloadeapp

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.Uri.parse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_imageview.*
import kotlinx.android.synthetic.main.activity_videoview.*
import android.media.MediaPlayer
import android.os.Build
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import java.io.IOException
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import org.apache.commons.io.FileUtils
import java.io.File


class videoview : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoview)


        class MyMediaController : MediaController {
            constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
            constructor(context: Context?, useFastForward: Boolean) : super(
                context,
                useFastForward
            )

            constructor(context: Context?) : super(context) {}

            override fun show(timeout: Int) {
                super.show(0)
            }

            override fun hide() {
                super.show(0)
            }

        }

        val uri: String = intent.getStringExtra("uri").toString()
        val path : String = intent.getStringExtra("path").toString()
        val videoView = findViewById<VideoView>(R.id.statusvideo)
        videoView.setVideoURI(parse(uri))
        val rl = findViewById<RelativeLayout>(R.id.rl1)


        var mediaController1 = MyMediaController(rl.context)
        mediaController1.setAnchorView(videoView)
        videoView.requestFocus()
        videoView.start()
        videoView.setMediaController(mediaController1)
        mediaController1.visibility = View.VISIBLE

        videoback.setOnClickListener {
            super.onBackPressed()
        }


        videoshare.setOnClickListener {

            val share = Intent(Intent.ACTION_SEND)
            share.type = "video/mp4"
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
            startActivity(Intent.createChooser(share, "Share Image"))

        }

        videodownload.setOnClickListener {

            if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                val inputstring = contentResolver.openInputStream(parse(uri))
                val filename = "${System.currentTimeMillis()}.mp4"
                try {
                    val values = ContentValues()
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                    values.put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS + "/StatusSaverVideos/"
                    )
                    val uri = contentResolver.insert(
                        MediaStore.Files.getContentUri("external"), values
                    )
                    val outputstream = uri?.let {
                        contentResolver.openOutputStream(it)!!
                    }

                    if (inputstring != null) {
                        outputstream?.write(inputstring.readBytes())

                    }
                    outputstream?.close()
                    Toast.makeText(applicationContext, "Video Saved", Toast.LENGTH_SHORT).show()

                } catch (e: IOException) {
                    Toast.makeText(applicationContext, "Download failed", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(ContentValues.TAG, "Permission is granted1")
                true
            } else {
                Log.v(ContentValues.TAG, "Permission is revoked1")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    3
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(ContentValues.TAG, "Permission is granted1")
            true
        }
    }

    fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(ContentValues.TAG, "Permission is granted2")
                true
            } else {
                Log.v(ContentValues.TAG, "Permission is revoked2")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(ContentValues.TAG, "Permission is granted2")
            true
        }
    }
}