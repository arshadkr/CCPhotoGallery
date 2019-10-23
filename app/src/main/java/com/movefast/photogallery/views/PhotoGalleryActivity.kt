package com.movefast.photogallery.views

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.movefast.photogallery.R
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.movefast.photogallery.viewmodels.GalleryViewModel
import com.movefast.utils.adapters.GalleryImageAdapter
import com.movefast.utils.constants.Common

/**
 * An activity that demonstrates photo gallery coding challenge - MoveFast .
 */

class PhotoGalleryActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

    private var _iRecyclerView : RecyclerView?=null
    private var _galleryItemRefresh : SwipeRefreshLayout?=null
    private lateinit var _vmGallery : GalleryViewModel
    private  var _adapter : GalleryImageAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photogallery)

        //** Set the binding MVVM pattern
        setupBindings(savedInstanceState)


        //** when data updated -UI notify
        _vmGallery.getImageData().observe(this , Observer { viewmodels->

            if(_galleryItemRefresh!!.isRefreshing)
                _galleryItemRefresh!!.isRefreshing = false

            initRecycler(viewmodels)
            _adapter!!.notifyDataSetChanged()

        })

        //** pull to refresh
        _galleryItemRefresh!!.setOnRefreshListener {
            _vmGallery.PullGalleryData()
        }
    }

    private fun initRecycler(listData:ArrayList<GalleryViewModel>)
    {
        _adapter = GalleryImageAdapter(listData)

        _setLayoutManagerRecycle()
        _iRecyclerView!!.setHasFixedSize(true)
        _iRecyclerView!!.adapter = _adapter

    }

    private fun _setLayoutManagerRecycle(value :Int = Common.LIST_DISPLAY)
    {
        val mLayoutManager :LinearLayoutManager

        if(Common.GRID_DISPLAY == value)
            mLayoutManager = GridLayoutManager(this, Common.GRID_COLUMN)
        else
            mLayoutManager =LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        _iRecyclerView!!.layoutManager = mLayoutManager
    }

    private fun setupBindings(savedInstanceState: Bundle?) {

        //View setup
        _iRecyclerView = findViewById(R.id.recycler_imgview) as RecyclerView
        _galleryItemRefresh = findViewById(R.id.galleryItemRefresh) as SwipeRefreshLayout

        //** Set the colors of the Pull To Refresh View
        _galleryItemRefresh!!.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        _galleryItemRefresh!!.setColorSchemeColors(Color.WHITE)

        //binding viewmodels
        _vmGallery = ViewModelProviders.of(this).get(GalleryViewModel :: class.java)
        _vmGallery.initialize()

        //initial progress until data -api
        _galleryItemRefresh!!.isRefreshing = true

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.action_search).actionView as SearchView).apply {

            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_gridview -> {
                _setLayoutManagerRecycle(Common.GRID_DISPLAY)
                true
            }
            R.id.action_listview ->{
                _setLayoutManagerRecycle(Common.LIST_DISPLAY)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        _adapter!!.getFilter().filter(newText);
        return true
    }
}
