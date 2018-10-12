package com.neoris.e_eomartinez.ubicationhelper.features.map.domain.models

import com.neoris.e_eomartinez.ubicationhelper.core.types.Model

data class Location(val lat: Double = 0.0, val lon: Double = 0.0, val bearing: Float = 0f): Model
