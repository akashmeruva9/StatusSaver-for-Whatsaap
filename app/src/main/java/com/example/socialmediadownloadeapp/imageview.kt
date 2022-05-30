package com.example.socialmediadownloadeapp

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_imageview.*
import java.io.*



class imageview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview)


        var uri : String = intent.getStringExtra("uri").toString()
        var file : String = intent.getStringExtra("name").toString()
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
                                Environment.DIRECTORY_PICTURES
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