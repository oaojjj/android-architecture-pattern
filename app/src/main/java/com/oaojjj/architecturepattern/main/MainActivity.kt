package com.oaojjj.architecturepattern.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoFragment
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoPresenter
import com.oaojjj.architecturepattern.todos.TodosFragment
import com.oaojjj.architecturepattern.todos.TodosPresenter

class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    // fragment
    private lateinit var todosFragment: TodosFragment
    private lateinit var addEditTodoFragment: AddEditTodoFragment

    override lateinit var presenter: MainContract.Presenter

    /**
     * true -> todosFragment
     * false -> addEditTodoFragment
     */
    override var isChangeFragment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle_MainActivity", "onCreate: ")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up the toolbar
        setSupportActionBar(binding.toolbarMain)

        // create view(fragment)
        addEditTodoFragment =
            supportFragmentManager.findFragmentByTag(MainContract.View.ADD_EDIT_TODO_FRAGMENT_TAG)
                    as AddEditTodoFragment? ?: AddEditTodoFragment()

        todosFragment =
            supportFragmentManager.findFragmentByTag(MainContract.View.TODOS_FRAGMENT_TAG)
                    as TodosFragment? ?: TodosFragment()


        // create the presenter
        presenter = MainPresenter(view = this).apply {
            setFragmentPresenter(
                TodosPresenter(view = todosFragment),
                AddEditTodoPresenter(view = addEditTodoFragment)
            )
        }

        // set floating button listener
        binding.fabMain.setOnClickListener(this)

        // hosting TodosFragment on MainActivity
        showTodosFragment()
    }

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
        Handler(Looper.getMainLooper()).apply {
            postDelayed({
                binding.fabMain.setImageDrawable(
                    ContextCompat.getDrawable(this@MainActivity, ResId)
                )
            }, 300)
        }
    }

    /**
     * View - Fragment 표시
     */
    override fun showTodosFragment() {
        if (!todosFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fl_container_main,
                    todosFragment,
                    MainContract.View.TODOS_FRAGMENT_TAG
                ).commit()
        }
    }

    override fun showAddEditTodoFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(MainContract.View.ADD_EDIT_TODO_FRAGMENT_TAG)
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            ).replace(R.id.fl_container_main, addEditTodoFragment).commit()
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

    /**
     * fab 클릭, 옵션 메뉴 클릭..
     * View 에서 사용자 이벤트 받아서 presenter에 전달
     * 근데 굳이 이럴 필요가 있을까? 그냥 바로 fragment 생성하고 애니메이션 바꾸면 안되는 것인가?
     * 어차피 둘다 view에 관련된 조작인데
     */
    override fun onClick(view: View) {
        when (isChangeFragment) {
            false -> presenter.addTodo()
            true -> {
                setExpandedAppBarLayout(true)
                showBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
            }
        }
    }
}