package com.example.threekingdomsreader.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.threekingdomsreader.general.GeneralFragment
import com.example.threekingdomsreader.generals.GeneralsFragment
import com.example.threekingdomsreader.main.MainContract
import com.example.threekingdomsreader.main.MainPresenter

class FragmentFactoryImpl(presenter: MainContract.Presenter) : FragmentFactory() {
    private val mainPresenter = presenter as MainPresenter
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            GeneralsFragment::javaClass.name -> GeneralsFragment()
            GeneralFragment::javaClass.name -> GeneralFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}