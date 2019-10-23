package com.movefast.utils.Imageload.async

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import com.movefast.utils.Imageload.cache.ImageCache
import com.movefast.utils.Imageload.manager.ImageFetcher
import java.net.MalformedURLException
import java.net.URL

/**
 * Bitmap Async Task to show in imageview.
 */
class LoadBitmapFromURLTask (private val imageView: ImageView, private val mCache: ImageCache?, private val mCacheImage: Boolean, private val mWidth: Int, private val mHeight: Int, private val strUrl: String) :
    AsyncTask<Void,Void, Bitmap>(){
    override fun doInBackground(vararg p0: Void?): Bitmap {
            var url: URL? = null
            try {
                url = URL(strUrl)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            var bitmap :Bitmap?=null
            if (url != null) {
                bitmap = ImageFetcher.decodeSampledBitmapFromUrl(url, mWidth, mHeight)
                return bitmap
            }
            else
                return bitmap!!

    }

    override fun onPostExecute(bitmap: Bitmap?) {
        super.onPostExecute(bitmap)
        if (bitmap==null)
            return
        else
        {
            if (mCacheImage) {
                mCache?.put(strUrl, bitmap)
            }
            imageView.setImageBitmap(bitmap)
        }
    }

}