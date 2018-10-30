package com.neoris.eeomartinez.ubicationhelper.features.map.data

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.neoris.eeomartinez.ubicationhelper.core.extensions.animate
import com.neoris.eeomartinez.ubicationhelper.core.types.Repository
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.Location
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryCloudLocations @Inject constructor(): Repository<Observable<DataSnapshot>, Nothing> {

    private val database = FirebaseDatabase.getInstance().reference

    override fun getData(param: String): Observable<DataSnapshot> =

            Observable.create {emitter ->
                database.addChildEventListener(object: ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                    override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                        emitter.onNext(snapshot)
                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {}
                    override fun onChildRemoved(p0: DataSnapshot) {}
                })
            }
}