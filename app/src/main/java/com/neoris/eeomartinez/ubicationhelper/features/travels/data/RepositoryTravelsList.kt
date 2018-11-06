package com.neoris.eeomartinez.ubicationhelper.features.travels.data

import com.google.firebase.firestore.FirebaseFirestore
import com.neoris.eeomartinez.ubicationhelper.core.types.Repository
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryTravelsList @Inject constructor(): Repository<Observable<List<String>>, Nothing> {

    companion object {
        const val driverId = "driver_1"
    }

    override fun getData(param: String): Observable<List<String>> {

        val travels = mutableListOf<String>()
        return Observable.create<List<String>> { emitter ->

            FirebaseFirestore.getInstance()
                    .collection(driverId)
                    .orderBy("id")
                    .startAfter(param)
                    .limit(10)
                    .get()
                    .addOnSuccessListener { task ->
                        task.documents.forEach {
                            travels.add(it.id)
                        }
                        emitter.onNext(travels)
                    }
                    .addOnFailureListener {
                        val cause = it.cause
                        if (cause != null) {
                            emitter.onError(cause)
                        }
                    }
        }
    }
}