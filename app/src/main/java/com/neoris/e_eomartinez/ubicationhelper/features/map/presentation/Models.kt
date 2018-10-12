package com.neoris.e_eomartinez.ubicationhelper.features.map.presentation

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

class Models {
    data class Point(var latitude: Double, var longitude: Double)
    data class Zone(var id: Number, var name: String, var description: String, var polygon: Polygon?,
                    var color: String, var points: ArrayList<Point>, var places: ArrayList<Place>){
        fun getPolygon(map: GoogleMap?): Polygon? {
            if (polygon == null) {
                var polygonOptions : PolygonOptions = PolygonOptions()
                polygonOptions.clickable(true);
                polygonOptions.fillColor(Color.parseColor(color));
                polygonOptions.strokeWidth(1f);
                for (p: Point in points) {
                    polygonOptions.add(LatLng(p.latitude, p.longitude))
                }
                polygon = map!!.addPolygon(polygonOptions);
            }
            return polygon;
        }
    }
    data class Place(var title: String, var latitude: Double, var longitude: Double,
                     var iconResource: Int )
}