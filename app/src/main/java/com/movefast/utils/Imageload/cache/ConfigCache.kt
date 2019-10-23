package com.movefast.utils.Imageload.cache

/**
 * Cache should be less than 1(100%). Setting 25% -warning
 */

object ConfigCache {
    fun setMemCacheSizePercent(percent: Float): Float {
        if (percent > 1f) {
            return 25f
        }
        return percent
    }
}