package com.oaojjj.architecturepattern

import androidx.appcompat.app.ActionBar

interface BaseView<T> {
    var presenter: T

    fun setToolbar(
        supportActionBar: ActionBar?,
        title: String,
        isDisplayHomeAsUpEnabled: Boolean
    ) {
        with(supportActionBar) {
            this?.title = title
            this?.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled)
        }
    }
}