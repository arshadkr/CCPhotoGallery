package com.movefast.photogallery.repository

import com.movefast.photogallery.interfaces.IRefreshData
import com.movefast.photogallery.interfaces.IWebService
import com.movefast.photogallery.model.api.Image
import com.movefast.photogallery.repository.ApiUrls.GET_PHOTOS
import com.movefast.photogallery.viewmodels.GalleryViewModel
import com.movefast.utils.constants.Common
import org.json.JSONArray

/**
 * Singleton - API returns JSON and set/getter
 */
object ImageDataRepository : IWebService {

    private val _listData : ArrayList<GalleryViewModel> = ArrayList()
    private var _objRefreshData :IRefreshData?=null

    private fun setData(objRefreshData : IRefreshData)
    {
        this._objRefreshData = objRefreshData
        WebServiceCall(this).execute(GET_PHOTOS)
    }

    fun getData(objRefreshData : IRefreshData) {
        setData(objRefreshData)
    }

    override fun WserviceDataResult(data: String) {
        val arrJson = JSONArray(data)

        //Read JSON data
        var x=0
        while (x<arrJson.length())
        {
            val jsonObject = arrJson.getJSONObject(x)

            val id = jsonObject.getString(APIConstModel.ID)
            val desc = jsonObject.getString(APIConstModel.DESCRIPTION)
            val alt_desc = jsonObject.getString(APIConstModel.ALT_DESCRIPTION)
            val likesCount = jsonObject.getInt(APIConstModel.LIKES_COUNT).toString()

            //Img Urls
            val objImg_urls = jsonObject.getJSONObject(APIConstModel.URLS)
            val img_url = objImg_urls.getString(APIConstModel.REGULAR)

            //User Details
            val objUser = jsonObject.getJSONObject(APIConstModel.USER)
            val name = objUser.getString(APIConstModel.NAME)
            val bio = objUser.getString(APIConstModel.BIO)
            val profile_image = objUser.getJSONObject(APIConstModel.PROFILE_IMAGE).getString(APIConstModel.MEDIUM)
            val img_description = _description(desc,alt_desc)


            var objImage1 = Image(id,img_description,likesCount,img_url,name,profile_image,bio)
            var vmGallery :GalleryViewModel = GalleryViewModel(objImage1)
            _listData.add(vmGallery)

            x++
        }
        this._objRefreshData!!.RefreshData(_listData)

    }

// if image description isn't available then assign to alternate desc
    private fun _description(desc :String, alt_desc :String) :String
    {
        if(!desc.isNullOrBlank() && !desc.equals("null"))
            return desc
        else if(alt_desc.isNullOrBlank()|| alt_desc.equals("null"))
            return  Common.NO_DESC
        else
            return alt_desc

        return  desc
    }

}