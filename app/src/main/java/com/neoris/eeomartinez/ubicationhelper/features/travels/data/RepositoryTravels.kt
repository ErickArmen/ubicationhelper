package com.neoris.eeomartinez.ubicationhelper.features.travels.data

import com.google.firebase.firestore.FirebaseFirestore
import com.neoris.eeomartinez.ubicationhelper.core.types.Repository
import io.reactivex.Observable
import javax.inject.Inject

@Deprecated("Replaced by Paging library (Datasource and PagedListAdapter")
class RepositoryTravels @Inject constructor(): Repository<Observable<String>, Nothing> {

    companion object {
        const val driverId = "driver_1"
    }

    override fun getData(param: String): Observable<String> =

        Observable.create<String> { emitter ->

            FirebaseFirestore.getInstance()
                    .collection(driverId)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        querySnapshot?.documentChanges?.forEach {
                            emitter.onNext(it.document.id)
                        }

                        val cause = firebaseFirestoreException?.cause
                        if (cause != null) {
                            emitter.onError(cause)
                        }
                    }
        }
}