package com.neoris.e_eomartinez.ubicationhelper

import com.google.android.gms.maps.model.Polygon


class MapController {
    private var mZIndex: Int = 0
    private lateinit var mLstZoneModels: ArrayList<Models.Zone>
    private lateinit var mCurrentZoneSelected: Models.Zone
    private lateinit var mControllerCallback: MapControllerCallback

    constructor(controllerCallback: MapControllerCallback) {
        this.mControllerCallback = controllerCallback
    }

    fun getZones() {
        this.mLstZoneModels = DummyData().zones()
        this.mZIndex =  mLstZoneModels.size
        this.mControllerCallback!!.onGetZones(mLstZoneModels)
    }

    fun upZIndex(): Number {
        this.mZIndex +=1
        return this.mZIndex
    }

    fun updateIndex(polygon: Polygon) {
        polygon.setZIndex(upZIndex().toFloat())
        for (model in this.mLstZoneModels) {
            if (model.getPolygon(null)?.id.equals(polygon.getId(), true)) {
                this.mCurrentZoneSelected = model
                this.mControllerCallback.onCurrentZoneSelected(this.mCurrentZoneSelected)
                break
            }
        }
    }

    interface MapControllerCallback {
        fun onGetZones(lstZoneModels: ArrayList<Models.Zone>)
        fun onCurrentZoneSelected(zoneModel: Models.Zone)
    }
}