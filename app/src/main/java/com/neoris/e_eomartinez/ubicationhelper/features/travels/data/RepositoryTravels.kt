package com.neoris.e_eomartinez.ubicationhelper.features.travels.data

import com.google.firebase.firestore.FirebaseFirestore
import com.neoris.e_eomartinez.ubicationhelper.core.types.Repository
import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.models.Kardex
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryTravels @Inject constructor(): Repository<String, Kardex> {

    override fun getData(param: String): Observable<String> =

        Observable.create<String> { emitter ->

            FirebaseFirestore.getInstance()
                    .collection("driver_1")
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

    override fun setData(param: Kardex): Completable = Completable.complete()

}