package com.neoris.e_eomartinez.ubicationhelper.features.travels.data

import com.google.firebase.firestore.FirebaseFirestore
import com.neoris.e_eomartinez.ubicationhelper.core.types.Repository
import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.models.Kardex
import io.reactivex.Completable
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RepositoryKardex @Inject constructor(): Repository<Kardex, Kardex> {


    override fun getData(param: String): Observable<Kardex> =

            Observable.create { emitter ->
                FirebaseFirestore.getInstance()
                        .collection("driver_1")
                        .document(param).collection("events")
                        .orderBy("createdDate")
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            querySnapshot?.documentChanges?.forEach { change ->
                                val kardexDate = change.document["createdDate"] as Date
                                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                                emitter.onNext(Kardex(change.document["name"] as String, sdf.format(kardexDate)))
                            }

                            val cause = firebaseFirestoreException?.cause
                            if (cause != null) {
                                emitter.onError(cause)
                            }
                        }
            }

    override fun setData(param: Kardex): Completable = Completable.complete()
}