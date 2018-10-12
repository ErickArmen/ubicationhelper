package com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.models

import com.neoris.e_eomartinez.ubicationhelper.core.types.Model
import java.util.Date

data class Event (val id: String = "",  val name: String = "", val time: Date = Date()): Model
