package com.neoris.e_eomartinez.ubicationhelper

class DummyData {

    fun zones(): ArrayList<Models.Zone> {
            val zoneModels = ArrayList<Models.Zone>()
            var points: ArrayList<Models.Point> = ArrayList()
            points.add(Models.Point(25.677739526560693, -100.41719924658538))
            points.add(Models.Point(25.677739526560693, -100.41548497974873))
            points.add(Models.Point(25.67669614251968, -100.41554197669029))
            points.add(Models.Point(25.67669614251968, -100.41734911501408))
            points.add(Models.Point(25.677739526560693, -100.41719924658538))
            var z1 = Models.Zone(0, "Zona neoris", "Descripcion neoris",
                    null, "#636161D9", points)
            points = ArrayList()
            points.add(Models.Point(25.677462742110194, -100.41810616850853))
            points.add(Models.Point(25.677379041882304, -100.41745640337467))
            points.add(Models.Point(25.676528740928294, -100.41752044111489))
            points.add(Models.Point(25.67629032923068, -100.41801329702137))
            points.add(Models.Point(25.677005260724773, -100.41804917156696))
            points.add(Models.Point(25.677462742110194, -100.41810616850853))
            val z2 = Models.Zone(0,"Zona Papers","Descripcion papers",
                    null, "#4286f4D9",points)


            points = ArrayList()
            points.add(Models.Point(25.67817766657093, -100.41632048785686))
            points.add(Models.Point(25.676000548084065, -100.41634932160378))
            points.add(Models.Point(25.6760712559575, -100.41527073830366))
            points.add(Models.Point(25.676973230918673, -100.41430648416281))
            points.add(Models.Point(25.678261366237894, -100.41472792625426))
            points.add(Models.Point(25.67817766657093, -100.41632048785686))
            val z3 = Models.Zone(0, "General electrics",
                    "Descripcion GE",null, "#ff0037D9", points)
            zoneModels.add(z1)
            zoneModels.add(z2)
            zoneModels.add(z3)
            return zoneModels
        }
}
