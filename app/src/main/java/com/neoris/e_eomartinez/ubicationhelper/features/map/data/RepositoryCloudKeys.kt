package com.neoris.e_eomartinez.ubicationhelper.features.map.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.neoris.e_eomartinez.ubicationhelper.core.types.Repository
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryCloudKeys @Inject constructor(): Repository<Observable<String>, Nothing> {

    private val database = FirebaseDatabase.getInstance().reference

    override fun getData(param: String): Observable<String> =

            Observable.create { emitter ->

                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        Log.i("TESTING datasnap", p0.toString())
                        p0.children.forEach {
                            Log.i("TESTING key", it.key!!)
                            emitter.onNext(it.key!!)
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        emitter.onError(p0.toException().cause!!)
                    }
                })
            }
}