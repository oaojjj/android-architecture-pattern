package com.example.threekingdomsreader.main

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.general.GeneralFragment
import com.example.threekingdomsreader.general.GeneralPresenter
import com.example.threekingdomsreader.generals.GeneralsFragment
import com.example.threekingdomsreader.generals.GeneralsPresenter
import com.example.threekingdomsreader.util.FragmentFactoryImpl
import com.example.threekingdomsreader.util.Injection
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), MainContract.View {

    val GENERAL_BUNDLE_KEY = "GENERAL_BUNDLE_KEY"

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
            title = "삼국지"
        }

        // change only overflow menu icon color
        /*findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).overflowIcon?.setColorFilter(
            Color.WHITE,
            PorterDuff.Mode.SRC_ATOP
        )*/

        showGenerals()

        val generalId = savedInstanceState?.getLong(GENERAL_BUNDLE_KEY)
        if (generalId != null && generalId > 0)
            showGeneral(generalId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(GENERAL_BUNDLE_KEY, presenter.generalPresenter?.generalId ?: -1)
    }

    override fun showGenerals() {
        // create the fragment
        val fn = GeneralsFragment::class.java.name
        val generalsFragment =
            supportFragmentManager.findFragmentByTag(fn) as GeneralsFragment?
                ?: addFragmentToActivity(fn, R.id.fl_main_container) as GeneralsFragment

        // create the presenter
        val generalsPresenter = GeneralsPresenter(
            Injection.provideTodoRepository(applicationContext),
            generalsFragment
        )

        presenter.setFragmentPresenter(generalsPresenter)
    }

    override fun showGeneral(generalId: Long?) {
        val fn = GeneralFragment::class.java.name
        // create the fragment
        val generalFragment = supportFragmentManager.findFragmentByTag(fn) as GeneralFragment?
            ?: replaceFragmentInActivity(fn, R.id.fl_main_container) as GeneralFragment

        // create the presenter
        val generalPresenter = GeneralPresenter(
            generalId,
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
                beginTransaction().replace(frameId, it, name).addToBackStack(name).commit()
            }
        }

    private fun addFragmentToActivity(name: String, @IdRes frameId: Int): Fragment =
        with(supportFragmentManager) {
            fragmentFactory.instantiate(classLoader, name).also {
                beginTransaction().add(frameId, it, name).commit()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mainLife", "onDestroy: ")
    }
}
