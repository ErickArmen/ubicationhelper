package com.neoris.eeomartinez.ubicationhelper.features.map.domain.models

import java.text.SimpleDateFormat
import java.util.*

class RawRoute(private val initDate: Long, private val finalDate: Long, private val points: Map<String, Any>){

    companion object {
        const val dateformat = "mm:ss"
    }

    class Route(val time: String, val points: SortedMap<String, Any>)

    fun transformtoRoute(): Route{
        val diffDate = finalDate.minus(initDate)
        val sdf = SimpleDateFormat(dateformat, Locale.getDefault())
        return Route(sdf.format(diffDate), points.toSortedMap())
    }
}