package com.movefast.photogallery.interfaces

import com.movefast.photogallery.viewmodels.GalleryViewModel

interface IRefreshData {
    fun RefreshData(newDataViewModel : ArrayList<GalleryViewModel>)
}