package com.oaojjj.architecturepattern.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoFragment
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoPresenter
import com.oaojjj.architecturepattern.main.MainContract.View.Companion.ADD_EDIT_TODO_FRAGMENT_TAG
import com.oaojjj.architecturepattern.main.MainContract.View.Companion.TODOS_FRAGMENT_TAG
import com.oaojjj.architecturepattern.todos.TodosFragment
import com.oaojjj.architecturepattern.todos.TodosPresenter
import com.oaojjj.architecturepattern.util.FragmentFactoryImpl
import com.oaojjj.architecturepattern.util.Injection


class MainActivity : AppCompatActivity(), MainContract.View {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override var presenter: MainContract.Presenter = MainPresenter(view = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        // instantiate fragmentFactory
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl(presenter)

        Log.d("lifecycle_MainActivity", "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // set up the toolbar
        setSupportActionBar(binding.toolbarMain)

        // set floating button listener
        binding.fabMain.setOnClickListener {
            when (getCurrentFragment()) {
                is TodosFragment -> navigateAddEditFragment()
                is AddEditTodoFragment -> {
                    presenter.addEditTodoPresenter.saveTodo()
                    navigateTodosFragment()
                }
            }
        }

        // hosting todosFragment on mainActivity
        navigateTodosFragment()
    }

    override fun getFragmentByName(name: String) =
        supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            name
        )

    override fun getCurrentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.fl_container_main)

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("lifecycle_MainActivity", "onSaveInstanceState: ")
        super.onSaveInstanceState(outState)
    }

    override fun onRestart() {
        Log.d("lifecycle_MainActivity", "onRestart: ")
        super.onRestart()
    }

    override fun onStart() {
        Log.d("lifecycle_MainActivity", "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d("lifecycle_MainActivity", "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.d("lifecycle_MainActivity", "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.d("lifecycle_MainActivity", "onStop: ")
        super.onStop()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("lifecycle_MainActivity", "onRestoreInstanceState: ")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        Log.d("lifecycle_MainActivity", "onDestroy: ")
        super.onDestroy()
    }

    /**
     * View
     * Show&Navigate Fragment
     */
    override fun navigateTodosFragment() {
        instantiateTodosFragment()
        showTodosFragment()
    }

    override fun navigateAddEditFragment() {
        instantiateAddEditFragment()
        showAddEditTodoFragment()
    }

    override fun instantiateTodosFragment() {
        presenter.setFragmentPresenter(
            TodosPresenter(
                Injection.provideTodoRepository(applicationContext),
                getFragmentByName(TodosFragment::class.java.name) as TodosFragment
            )
        )
    }

    override fun instantiateAddEditFragment() {
        presenter.setFragmentPresenter(
            AddEditTodoPresenter(
                Injection.provideTodoRepository(applicationContext),
                getFragmentByName(AddEditTodoFragment::class.java.name) as AddEditTodoFragment
            )
        )
    }

    override fun showTodosFragment() {
        val todosFragment = presenter.todosPresenter.getView()

        if (!todosFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(
                R.id.fl_container_main,
                todosFragment,
                TODOS_FRAGMENT_TAG
            ).commit()
        }
        changeFabIconToPlus()
    }

    override fun showAddEditTodoFragment() {
        val addEditTodoFragment = presenter.addEditTodoPresenter.getView()

        supportFragmentManager.beginTransaction()
            .addToBackStack(ADD_EDIT_TODO_FRAGMENT_TAG)
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            ).replace(R.id.fl_container_main, addEditTodoFragment).commit()
        changeFabIconToCheck()
    }

    /**
     * View
     * 상단 액션바, 하단 바텀바 조작
     */
    override fun showBottomAppbar(isShow: Boolean) {
        val bottomAppbar = binding.babMain
        when (isShow) {
            true -> bottomAppbar.behavior.slideUp(bottomAppbar)
            false -> bottomAppbar.behavior.slideDown(bottomAppbar)
        }
    }

    override fun setExpandedAppBarLayout(isExpended: Boolean) {
        binding.appBarLayout.setExpanded(isExpended)
    }

    override fun changeFabIconToPlus() {
        showBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)

    }

    override fun changeFabIconToCheck() {
        showBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
    }

    override fun showBottomAnimation(ResId: Int, fabAlignmentMode: Int) {
        binding.babMain.fabAlignmentMode = fabAlignmentMode

        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabMain.setImageDrawable(
                ContextCompat.getDrawable(this@MainActivity, ResId)
            )
        }, 300)
    }
}