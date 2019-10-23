package com.movefast.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movefast.photogallery.databinding.ListphotoItemBinding
import com.movefast.photogallery.viewmodels.GalleryViewModel
import com.movefast.utils.ImageFilter

/**
 * Recycler Adapter assigned based on MVVM pattern.
 */
class GalleryImageAdapter(private val arrayList:ArrayList<GalleryViewModel>) : RecyclerView.Adapter<GalleryImageAdapter.GalleryImageView>(),
    Filterable {

    private var _imageFilter : ImageFilter?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listphotoItemBinding:ListphotoItemBinding = DataBindingUtil.inflate(layoutInflater, com.movefast.photogallery.R.layout.listphoto_item,parent,false)
        return GalleryImageView(listphotoItemBinding)

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: GalleryImageView, position: Int) {
        val vmGallery = arrayList[position]
        holder.bind(vmGallery)
    }

    // search image
    override fun getFilter(): Filter {

        if (_imageFilter == null) {
            _imageFilter = ImageFilter(arrayList)
        }

        return _imageFilter!!
    }

    // view Holder for adapter
    class GalleryImageView(val listphotoItemBinding : ListphotoItemBinding) : RecyclerView.ViewHolder(listphotoItemBinding.root)
    {
        fun bind(vmGallery : GalleryViewModel)
        {
            this.listphotoItemBinding.imagemodel = vmGallery
            listphotoItemBinding.executePendingBindings()
        }
    }
}
