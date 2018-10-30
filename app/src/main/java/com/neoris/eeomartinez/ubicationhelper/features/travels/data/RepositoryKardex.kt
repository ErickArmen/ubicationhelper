package com.neoris.eeomartinez.ubicationhelper.features.travels.data

import com.google.firebase.firestore.FirebaseFirestore
import com.neoris.eeomartinez.ubicationhelper.core.types.Repository
import com.neoris.eeomartinez.ubicationhelper.features.travels.domain.models.Kardex
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RepositoryKardex @Inject constructor(): Repository<Observable<Kardex>, Nothing> {

    companion object {
        const val driverId = "driver_1"
        const val eventsId = "events"
        const val fieldDate = "createdDate"
        const val format = "HH:mm"
        const val nameId = "name"
    }

    override fun getData(param: String): Observable<Kardex> =

            Observable.create { emitter ->
                FirebaseFirestore.getInstance()
                        .collection(driverId)
                        .document(param).collection(eventsId)
                        .orderBy(fieldDate)
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            querySnapshot?.documentChanges?.forEach { change ->
                                val kardexDate = change.document[fieldDate] as Date
                                val sdf = SimpleDateFormat(format, Locale.getDefault())
                                emitter.onNext(Kardex(change.document[nameId] as String, sdf.format(kardexDate)))
                            }

                            val cause = firebaseFirestoreException?.cause
                            if (cause != null) {
                                emitter.onError(cause)
                            }
                        }
            }
}