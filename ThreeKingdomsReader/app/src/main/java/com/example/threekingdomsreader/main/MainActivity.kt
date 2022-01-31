package com.example.threekingdomsreader.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.general.GeneralFragment
import com.example.threekingdomsreader.general.GeneralPresenter
import com.example.threekingdomsreader.generals.GeneralsFragment
import com.example.threekingdomsreader.generals.GeneralsPresenter
import com.example.threekingdomsreader.util.FragmentFactoryImpl
import com.example.threekingdomsreader.util.Injection
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), MainContract.View {

    override var isActive: Boolean = false
        get() = isDestroyed

    override var presenter: MainContract.Presenter = MainPresenter(view = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mainLife", "onCreate: ")
        // set fragmentFactory
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl(presenter)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar)).run {
            title = "삼국지 무장 목록"
        }

        // create the fragment
        val generalsFragment = supportFragmentManager.findFragmentById(R.id.fl_main_container)
                as GeneralsFragment? ?: addFragmentToActivity(
            GeneralsFragment::class.java.name, R.id.fl_main_container
        ) as GeneralsFragment

        // create the presenter
        val generalsPresenter = GeneralsPresenter(
            Injection.provideTodoRepository(applicationContext),
            generalsFragment
        )

        presenter.setFragmentPresenter(generalsPresenter)
    }

    override fun showGeneral(general: General?) {
        val fragment =supportFragmentManager.findFragmentById(R.id.fl_main_container)
        // create the fragment
        val generalFragment = /*supportFragmentManager.findFragmentById(R.id.fl_main_container)
                as GeneralFragment? ?: */replaceFragmentInActivity(
            GeneralFragment::class.java.name, R.id.fl_main_container
        ) as GeneralFragment

        // create the presenter
        val generalPresenter = GeneralPresenter(
            general?.id,
            Injection.provideTodoRepository(applicationContext),
            generalFragment
        )

        presenter.setFragmentPresenter(generalPresenter)
    }

    override fun fabHide() {
        findViewById<FloatingActionButton>(R.id.fab_add_general).hide()
    }

    override fun fabShow() {
        findViewById<FloatingActionButton>(R.id.fab_add_general).show()
    }

    private fun replaceFragmentInActivity(name: String, @IdRes frameId: Int): Fragment =
        with(supportFragmentManager) {
            fragmentFactory.instantiate(classLoader, name).also {
                beginTransaction().replace(frameId, it).addToBackStack(null).commit()
            }
        }

    private fun addFragmentToActivity(name: String, @IdRes frameId: Int): Fragment =
        with(supportFragmentManager) {
            fragmentFactory.instantiate(classLoader, name).also {
                beginTransaction().add(frameId, it).commit()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mainLife", "onDestroy: ")
    }

    companion object {
        const val GENERAL_BUNDLE_KEY = "GENERAL_BUNDLE_KEY"
    }
}
