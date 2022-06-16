package com.example.socialmediadownloadeapp

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_blank.*
import java.io.File

class BlankFragment : Fragment(R.layout.fragment_blank) {
    var uriList = java.util.ArrayList<statusdata>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.activity?.getSharedPreferences("app", AppCompatActivity.MODE_PRIVATE)
        val uri = preferences?.getString("uri", "no")


        if (uri != "no") {
            if (uri != null) {
                runRecyclerViewCode(uri)
            }
        } else {
            gettingAllData()
        }

    }

    private fun runRecyclerViewCode(uri: String) {

        val fileDoc = DocumentFile.fromTreeUri(requireContext(), Uri.parse(uri))

        for (file in fileDoc!!.listFiles()) {
            val item = statusdata(
                file.uri.toString() ,
                file.uri.path.toString()
            )
            uriList.add(item)
        }

        whatsapprecyclerview1.layoutManager = GridLayoutManager(context, 3)
        val adapter = statusadapter(this , uriList)
        whatsapprecyclerview1.adapter = adapter
    }


    private fun gettingAllData() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, 1234)
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == AppCompatActivity.RESULT_OK) {
            val treeUri = data!!.data
            this.activity?.contentResolver?.takePersistableUriPermission(
                treeUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val preferences = this.activity?.getSharedPreferences("app", AppCompatActivity.MODE_PRIVATE)
            val editor = preferences?.edit()
            editor?.putString("uri", treeUri.toString())
            editor?.apply()
            val fileDoc = DocumentFile.fromTreeUri(requireContext(), treeUri!!)
            for (file in fileDoc!!.listFiles()) {
                Log.d("ABHISHEKKKKK", "Files Name - " + file.name)
                Log.d("ABHISHEKKKKK", "Files URI - " + file.uri)
            }

            runRecyclerViewCode(treeUri.toString())
        }
    }

    fun openDirectory() {
        val path = Environment.getExternalStorageDirectory()
            .toString() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
        val file = File(path)
        var startDir: String? = null
        var secondDir: String
        val finalDirPath: String
        if (file.exists()) {
            startDir =
                "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses" // use %2F for "/"
        }
        val sm = this.activity?.getSystemService(AppCompatActivity.STORAGE_SERVICE) as StorageManager
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
            var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
            var scheme = uri.toString()
            Log.d("TAG", "INITIAL_URI scheme: $scheme")
            scheme = scheme.replace("/root/", "/document/")
            finalDirPath = "$scheme%3A$startDir"
            uri = Uri.parse(finalDirPath)
            intent.putExtra("android.provider.extra.INITIAL_URI", uri)
            Log.d("TAG", "uri: $uri")
            try {
                startActivityForResult(intent, 1234)
            } catch (ignored: ActivityNotFoundException) {
            }
        }
    }

    fun statusclicked(statusdata: statusdata)
    {

        if(statusdata.uri.endsWith(".mp4"))
        {
            var videointent = Intent(context, videoview::class.java)
            videointent.putExtra("uri", statusdata.uri)
            videointent.putExtra("path", statusdata.path)
            startActivity(videointent)

        }else{

            var imageintent = Intent(context, imageview::class.java)
            imageintent.putExtra("uri", statusdata.uri)
            imageintent.putExtra("path", statusdata.path)
            startActivity(imageintent)

        }

    }


}