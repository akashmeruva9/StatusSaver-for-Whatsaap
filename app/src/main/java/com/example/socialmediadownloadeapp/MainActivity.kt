package com.example.socialmediadownloadeapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_videoview.view.*


class MainActivity : AppCompatActivity()
{
   private lateinit var whatsappview : RecyclerView
   private lateinit var  statuslist : ArrayList<statusdata>
   private lateinit var  statusadapter : statusadapter

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       whatsappview = whatsapprecyclerview1
        statuslist = ArrayList()

        val permission = checkPermission()

        if(permission)
        {
            var shared = getSharedPreferences("DATA_PATH" , MODE_PRIVATE)
            var  uripath = shared.getString("PATH" , "")

            contentResolver.takePersistableUriPermission(Uri.parse(uripath),Intent.FLAG_GRANT_READ_URI_PERMISSION)

            if(uripath!=null)
            {
              contentResolver.takePersistableUriPermission(Uri.parse(uripath),Intent.FLAG_GRANT_READ_URI_PERMISSION)
                 val filedoc = DocumentFile.fromTreeUri(applicationContext , Uri.parse(uripath))

                for(file:DocumentFile in filedoc!!.listFiles())
                {
                    if(!file.name!!.endsWith(".nomedia")){
                        val status = statusdata(file.name!! , file.uri.toString())
                        statuslist.add(status)

                    }
                }

                setrecyclerview()
            }


        }else{
            askPermission()
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun askPermission()
    {
        val storagemanager = application.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent1 = storagemanager.primaryStorageVolume.createOpenDocumentTreeIntent()
        val targetdir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
        var uri = intent1.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI") as Uri
        var scheme = uri.toString()
        scheme = scheme.replace("/root/" , "/tree/")
        scheme += "%3A$targetdir"
        uri = Uri.parse(scheme)
        intent1.putExtra("android.provider.extra.INITIAL_URI" , uri)
        intent1.putExtra("android.content.extra.SHOW_ADVANCED" , true )
        startActivityForResult(intent1 , 1234)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK)
        {
            var treeuri = data?.data
            val sharedpref = getSharedPreferences("DATA_PATH" , MODE_PRIVATE)
            val myedit = sharedpref.edit()
            myedit.putString("PATH" , treeuri.toString())
            myedit.apply()

            if(treeuri!=null)
            {
                contentResolver.takePersistableUriPermission(treeuri,Intent.FLAG_GRANT_READ_URI_PERMISSION)

             val filedoc = DocumentFile.fromTreeUri(applicationContext , treeuri)

             for(file:DocumentFile in filedoc!!.listFiles())
             {
                 if(!file.name!!.endsWith(".nomedia")){
                     val status = statusdata(file.name!! , file.uri.toString())
                     statuslist.add(status)

                 }
             }

             setrecyclerview()
            }
        }
    }

private fun setrecyclerview() {

    statusadapter = statusadapter(this, statuslist)

    whatsappview.apply {
        setHasFixedSize(true)
        layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        adapter = statusadapter

    }
}


    private fun checkPermission():Boolean{

        val prefrence = getSharedPreferences("DATA_PATH" , MODE_PRIVATE)
        val uripath = prefrence.getString("PATH" , "")

        if(uripath!=null)
        {
            if(uripath.isEmpty())
                return false
        }

        return true
    }

  fun statusclicked(statusdata: statusdata)
  {

      if(statusdata.uri.endsWith(".mp4"))
      {
          var videointent = Intent(this, videoview::class.java)
          videointent.putExtra("uri", statusdata.uri)
          videointent.putExtra("name", statusdata.filename)
          startActivity(videointent)

      }else{

          var imageintent = Intent(this, imageview::class.java)
          imageintent.putExtra("uri", statusdata.uri)
          imageintent.putExtra("name", statusdata.filename)
          startActivity(imageintent)

      }

  }

}