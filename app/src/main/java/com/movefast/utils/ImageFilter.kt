package com.movefast.utils

import android.widget.Filter
import com.movefast.photogallery.model.api.Image
import com.movefast.photogallery.viewmodels.GalleryViewModel


class ImageFilter(val imageList :ArrayList<GalleryViewModel>) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val filterResults = Filter.FilterResults()
        if (constraint != null && constraint.length > 0) {
            val tempList = ArrayList<Image>()

            // search content in image list
            for (image in imageList) {
                if (image._image!!.id.toLowerCase().contains(constraint.toString().toLowerCase())) {
                    tempList.add(image._image!!)
                }
            }

            filterResults.count = tempList.size
            filterResults.values = tempList
        } else {
            filterResults.count = imageList.size
            filterResults.values = imageList
        }

        return filterResults
    }

    override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
        TODO("not implemented")
    }
}