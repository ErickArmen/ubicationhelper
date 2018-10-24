package com.neoris.e_eomartinez.ubicationhelper.core.types


//Ver si puedo reusar algunas extensiones de cejas

//en el sender refactorizar el factory para ver que se le acomoda más a eso que estoy haciendo
//con el builder creo que no queda más que hacer uno para cada tipo de objeto, pero puedo hacer los builders
//con interfaces para que tengan diferentes propiedades extendibles como el ejemplo del sandwich y ensalada.
//Hacer extensión para no repetir el codigo de disposable --- probable si hacer lo de invoke para iniciar todas
//declarar las acciones que se harán cuando se inicie un stream de data


//For single responsability classes
interface UseCase<A, R> {

    fun run (param: A): R

    operator fun invoke(params: A, action: (R) -> Unit = {}) {
        action(run(params))
    }

    class None
}