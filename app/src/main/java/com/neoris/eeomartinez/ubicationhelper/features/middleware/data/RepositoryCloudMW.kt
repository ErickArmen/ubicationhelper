package com.neoris.eeomartinez.ubicationhelper.features.middleware.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.neoris.eeomartinez.ubicationhelper.core.types.Repository
import com.neoris.eeomartinez.ubicationhelper.features.middleware.domain.models.Event
import io.reactivex.Completable
import javax.inject.Inject

class RepositoryCloudMW @Inject constructor(): Repository<Nothing, Event> {

    private val firestore = FirebaseFirestore.getInstance()

    override fun setData(param: Event): Completable =

            Completable.create{ emitter ->
                firestore.collection(param.id).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val size = task.result.size()
                        val document = task.result.documents[size-1] as QueryDocumentSnapshot
                        val eventMap = HashMap<String, Any>()
                        eventMap["createdDate"] = param.time
                        eventMap["name"] = param.name
                        firestore.collection(param.id)
                                .document(document.id)
                                .collection("events").document()
                                .set(eventMap, SetOptions.merge())
                                .addOnCompleteListener{ emitter.onComplete() }
                                .addOnFailureListener{ emitter.onError(it) }
                    }
                }
            }
}