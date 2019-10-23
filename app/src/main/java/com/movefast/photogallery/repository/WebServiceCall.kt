package com.movefast.photogallery.repository

import android.content.Context
import android.os.AsyncTask
import com.movefast.photogallery.interfaces.IWebService
import org.json.JSONArray
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

/**
 * Retrive data from API.
 */
class WebServiceCall(val objWebService :IWebService) :AsyncTask<String,String,String>() {


    override fun doInBackground(vararg url: String?): String {

        var data :String
        var connection :HttpsURLConnection?=null

        try {
            connection = URL(url[0]).openConnection() as HttpsURLConnection
            connection.connect()
            data = connection.inputStream.use { it.reader().use { reader -> reader.readText() }
            }

        }
        finally {
            connection!!.disconnect()
        }
        return  data
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        objWebService.WserviceDataResult(result!!)

    }
}

