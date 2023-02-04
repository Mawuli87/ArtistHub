package com.messieyawo.artistshub.events

sealed class Event<T : Any> {
    class Success<T : Any>(val r: T, val msg: String = "") : Event<T>()
    class Error<T : Any>(val msg: String) : Event<T>()
    class Loading<T : Any>() : Event<T>()
    class Waiting<T : Any>() : Event<T>()
}
