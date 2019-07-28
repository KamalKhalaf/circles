package com.circles.circlesapp.helpers.kotlin

class MapZoomHelper {
    public fun calculateZoomLevel(screenWidth: Double): Float {
        val equatorLength = 40075004.0 // in meters
        val widthInPixels = screenWidth.toDouble()
        var metersPerPixel = equatorLength / 256
        var zoomLevel = 1
        while (metersPerPixel * widthInPixels > 8000) {
            metersPerPixel /= 2.0
            ++zoomLevel
        }
        return zoomLevel.toFloat()
    }

}