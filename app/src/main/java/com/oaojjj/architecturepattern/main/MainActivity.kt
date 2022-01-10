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
import com.oaojjj.architecturepattern.todos.TodosFragment
import com.oaojjj.architecturepattern.todos.TodosPresenter
import com.oaojjj.architecturepattern.util.FragmentFactoryImpl
import com.oaojjj.architecturepattern.util.Injection

class MainActivity : AppCompatActivity(), MainContract.View {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override var mCurrentFragment: Fragment? = null

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
            when (mCurrentFragment) {
                is TodosFragment -> showAddEditTodoFragment()
                is AddEditTodoFragment -> {
                    presenter.addEditTodoPresenter.saveTodo()
                    showAddEditTodoFragment()
                }
            }
        }

        // hosting todosFragment on mainActivity
        showTodosFragment()
    }

    override fun getFragmentByName(name: String) =
        supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            name
        )

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
     * change bottom fab position(fab, bottomAbbBar)
     */
    override fun showBottomAnimation(ResId: Int, fabAlignmentMode: Int) {
        binding.babMain.fabAlignmentMode = fabAlignmentMode

        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabMain.setImageDrawable(
                ContextCompat.getDrawable(this@MainActivity, ResId)
            )
        }, 300)
    }

    /**
     * View
     * Fragment 이동
     */
    override fun showTodosFragment() {
        val todosFragment = getFragmentByName(TodosFragment::class.java.name) as TodosFragment

        presenter.setFragmentPresenter(
            TodosPresenter(
                Injection.provideTodoRepository(applicationContext),
                todosFragment
            )
        )

        if (!todosFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(
                R.id.fl_container_main,
                todosFragment,
                MainContract.View.TODOS_FRAGMENT_TAG
            ).commit()
        }

        mCurrentFragment = todosFragment
        showBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
    }

    override fun showAddEditTodoFragment() {
        val addEditTodoFragment =
            getFragmentByName(AddEditTodoFragment::class.java.name) as AddEditTodoFragment

        presenter.setFragmentPresenter(
            AddEditTodoPresenter(
                Injection.provideTodoRepository(applicationContext),
                addEditTodoFragment
            )
        )

        supportFragmentManager.beginTransaction()
            .addToBackStack(MainContract.View.ADD_EDIT_TODO_FRAGMENT_TAG)
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            ).replace(R.id.fl_container_main, addEditTodoFragment).commit()

        mCurrentFragment = addEditTodoFragment
        showBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
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
}