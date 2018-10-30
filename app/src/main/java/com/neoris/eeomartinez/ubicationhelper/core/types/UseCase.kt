package com.neoris.eeomartinez.ubicationhelper.core.types

//Single responsability classes
interface UseCase<A, R> {

    fun run (param: A): R

    operator fun invoke(params: A, action: (R) -> Unit = {}) {
        action(run(params))
    }

    class None
}