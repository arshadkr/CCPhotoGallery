package com.movefast.photogallery.model.api

/**
 * Design Models.
 */
class Image(id : String,description :String,count_likes : String, img_url :String, name :String,profile_image :String,bio :String) {

    var id : String
    var count_likes : String
    var description :String
    var img_url :String
    var profile_image :String
    var name :String
    var bio :String

    init {
        this.id =  id
        this. count_likes = count_likes
        this.description = description
        this.img_url = img_url
        this.name = name
        this.profile_image = profile_image
        this.bio = bio
    }
}