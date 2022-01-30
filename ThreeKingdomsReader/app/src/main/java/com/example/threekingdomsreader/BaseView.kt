package com.example.threekingdomsreader

interface BaseView<T> {

    var isActive: Boolean

    var presenter: T

}
