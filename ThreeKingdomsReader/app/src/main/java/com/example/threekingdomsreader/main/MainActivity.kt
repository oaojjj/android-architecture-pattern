package com.example.threekingdomsreader.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.source.local.GeneralDatabase
import com.example.threekingdomsreader.generals.GeneralsFragment
import com.example.threekingdomsreader.generals.GeneralsPresenter
import com.example.threekingdomsreader.util.FragmentFactoryImpl
import com.example.threekingdomsreader.util.Injection
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), MainContract.View {
    override var isActive: Boolean = false
        get() = isDestroyed

    override var presenter: MainContract.Presenter = MainPresenter(view = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        // set fragmentFactory
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl(presenter)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar)).run {
            title = "삼국지 무장 목록"
        }

        // create the presenter, fragment
        presenter.setFragmentPresenter(
            GeneralsPresenter(
                Injection.provideTodoRepository(applicationContext),
                getFragmentByName(
                    GeneralsFragment::javaClass.name,
                    R.id.fl_main_container
                ) as GeneralsFragment
            )
        )

    }

    private fun getFragmentByName(name: String, @IdRes frameId: Int) =
        with(supportFragmentManager) {
            fragmentFactory.instantiate(classLoader, name).also {
                this.beginTransaction().replace(frameId, it).commit()
            }
        }
}
