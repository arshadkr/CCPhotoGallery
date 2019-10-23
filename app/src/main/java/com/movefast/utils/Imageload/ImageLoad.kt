package com.movefast.utils.Imageload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.AnyRes
import androidx.annotation.DrawableRes
import com.movefast.utils.Imageload.async.LoadBitmapFromURLTask
import com.movefast.utils.Imageload.cache.ImageCache
import com.movefast.utils.Imageload.manager.PlaceHolder
import java.lang.ref.WeakReference

class ImageLoad {

    private var mUrl: String? = null
    private var enableCache = true
    private var mRes: Int = 0
    private var mPlaceHolderBitmap: Bitmap? = null
    private var mWidth = DEFAULT_WIDTH
    private var mHeight = DEFAULT_HEIGHT
    private var cacheAllowed = 1f

    companion object {
        private var contextWeakReference: WeakReference<Context>? = null
        private lateinit var muralWeakReference: WeakReference<ImageLoad>
        var DEFAULT_WIDTH = 500
        var DEFAULT_HEIGHT = 500

        fun with(context: Context): ImageLoad {

            contextWeakReference = WeakReference(context)

            val mural = ImageLoad()
            muralWeakReference = WeakReference(mural)

            DEFAULT_HEIGHT = Util.getScreenHeight(getContext())!!
            DEFAULT_WIDTH = Util.getScreenWidth(getContext())!!

            return getImageLoad()
        }
        private fun getContext(): Context? {
            return contextWeakReference?.get()
        }

        private fun getImageLoad(): ImageLoad {
            return muralWeakReference.get()!!
        }
    }

    /**
     * Image source by url
     */
    fun source(url: String): ImageLoad {
        mUrl = url
        mRes = -100
        return getImageLoad()
    }

    /**
     * Image source by drawable resource
     */
    fun source(@DrawableRes res: Int): ImageLoad {
        mRes = res
        mUrl = null
        return getImageLoad()
    }

    /**
     * Resize image
     * @param width
     * @param height
     */
    fun resize(width: Int, height: Int): ImageLoad {
        if (width != 0)
            mWidth = width
        if (height != 0)
            mHeight = height
        return getImageLoad()
    }

    /**
     * Placeholder image
     * @param placeholder - Drawable
     */
    fun placeholder(@AnyRes placeholder: Int): ImageLoad {
        if (placeholder != PlaceHolder.placeHolder) {
            try {
                // Try for drawable resource
                mPlaceHolderBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext()?.resources, placeholder), 300, 300, true)
                PlaceHolder.placeHolder = placeholder
                PlaceHolder.placeHolderBitmap = mPlaceHolderBitmap
                PlaceHolder.placeHolderColor = -1
            } catch (ignored: Exception) {
                PlaceHolder.placeHolder = placeholder
                PlaceHolder.placeHolderBitmap = null
                PlaceHolder.placeHolderColor = placeholder
            }

        } else {
            mPlaceHolderBitmap = PlaceHolder.placeHolderBitmap
        }
        return getImageLoad()
    }

    /**
     * Enable cache and set cache size - 0 to 1f
     * @param percent
     */
    fun enableCache(percent: Float): ImageLoad {
        enableCache = true
        cacheAllowed = percent
        return getImageLoad()
    }

    /**
     * Disable cache
     */
    fun disableCache(): ImageLoad {
        enableCache = false
        cacheAllowed = 0f
        return getImageLoad()
    }

    /**
     * Load image to an ImageView
     * @param imageView
     */
    fun loadImage(imageView: ImageView) {
        var imageCache: ImageCache? = null
        var bitmap: Bitmap? = null
        if (enableCache) {
            /*Get imageCache instance*/
            imageCache = ImageCache[getContext(), cacheAllowed]
            var imageKey: String? = null
            if (mUrl != null) {
                imageKey = mUrl
            }
            if (mRes != -100) {
                imageKey = mRes.toString()
            }
            bitmap = imageCache.get(imageKey)
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            if (mUrl != null) { /*Load using url*/
                val task = LoadBitmapFromURLTask(imageView, imageCache, enableCache, mWidth, mHeight, mUrl!!)

                // Set Placeholder
                if (PlaceHolder.placeHolderBitmap != null) { // Placeholder is bitmap
                    imageView.setImageBitmap(PlaceHolder.placeHolderBitmap)
                } else if (PlaceHolder.placeHolderColor != -1) { // Placeholder is color
                    imageView.setImageResource(PlaceHolder.placeHolderColor)
                }

                task.execute()
            }
        }
    }
}