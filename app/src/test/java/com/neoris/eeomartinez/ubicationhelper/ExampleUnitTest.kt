package com.neoris.eeomartinez.ubicationhelper

import io.reactivex.Observable
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {

    @Test
    fun testObservableMap() {
        val observable: Observable<String> = Observable.create {
            it.onNext("1")
        }
        observable.subscribe {
            System.out.println(it)
        }
    }
}
