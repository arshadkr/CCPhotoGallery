package com.movefast.photogallery.viewmodels

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movefast.photogallery.R
import com.movefast.photogallery.interfaces.IRefreshData
import com.movefast.photogallery.model.api.Image
import com.movefast.photogallery.repository.ImageDataRepository
import com.movefast.utils.Imageload.ImageLoad

//** ViewModel holds logic

class GalleryViewModel : ViewModel, IRefreshData {

     private var _imageDataList: MutableLiveData<ArrayList<GalleryViewModel>> = MutableLiveData()
     var _image :Image?=null

     constructor() :super()

    constructor(image :Image) :super()
    {
        this._image = image
    }

    fun getImageData() : MutableLiveData<ArrayList<GalleryViewModel>>
    {
        return _imageDataList;
    }

     fun initialize()
    {
        if(_imageDataList==null || _imageDataList.value == null)
            PullGalleryData()
        else
            return
    }

     fun PullGalleryData()
     {
         ImageDataRepository.getData(this)
     }

    //** When data returned retrived from API Class:ImageDataRepository
     override fun RefreshData(newDataViewModel: ArrayList<GalleryViewModel>) {
         _imageDataList.postValue(newDataViewModel)
     }

    fun popupImage()
    {
        PullGalleryData()
    }
 }

//** Image binding_ called when layput uses "imageUrl"
object ImageBindingAdapter
{
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(iview :ImageView, url :String)
    {
        ImageLoad.with(iview.context)
            .placeholder(R.drawable.ic_launcher_foreground)
            .source(url)
            .loadImage(iview)
    }
}