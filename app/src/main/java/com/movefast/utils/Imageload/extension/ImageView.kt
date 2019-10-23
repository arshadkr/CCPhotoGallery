package com.movefast.utils.Imageload.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.movefast.utils.Imageload.ImageLoad

var ImageView.source: Any
    get() = ""
    set(value) {
        if (value is String) {
            ImageLoad.with(this.context).source(value).loadImage(this)
        }
        else if (value is Int)
            ImageLoad.with(this.context).source(value).loadImage(this)
    }

var ImageView.placeholder: Drawable?
    get() = null
    set(source) {
        this.setImageDrawable(source)
    }