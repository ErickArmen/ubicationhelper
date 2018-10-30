package com.neoris.eeomartinez.ubicationhelper.features.map.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.neoris.eeomartinez.ubicationhelper.core.types.Repository
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.RawRoute
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryCloudRoutes @Inject constructor(): Repository<Observable<RawRoute>, Nothing> {

    companion object {
        const val routePath = "route"
        const val locationsPath = "locations"
    }

    private val firestore = FirebaseFirestore.getInstance()

    override fun getData(param: String): Observable<RawRoute> =

            Observable.create {emitter ->

                firestore.collection(param).get().addOnCompleteListener baseListener@ {

                    if (it.isSuccessful){
                        val size = it.result.size()
                         val document = it.result.documents[size-1] as QueryDocumentSnapshot
                        firestore.collection(param).document(document.id).collection(locationsPath)
                                .document(routePath).get().addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        val initDate = task.result.data?.keys?.min()?.toLong()
                                        val finalDate = task.result.data?.keys?.max()?.toLong()
                                        if (initDate == null || finalDate == null)
                                            return@addOnCompleteListener
                                        val map = task.result.data ?: return@addOnCompleteListener
                                        emitter.onNext(RawRoute(initDate, finalDate, map))
                                    }
                                }
                    }else{
                        emitter.onError(it.exception?.cause!!)
                    }
                }
            }
}