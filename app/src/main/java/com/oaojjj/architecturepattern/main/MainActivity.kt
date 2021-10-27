package com.oaojjj.architecturepattern.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.addtodo.AddTodoFragment
import com.oaojjj.architecturepattern.todos.TodosFragment
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener

class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    // fragment
    private lateinit var todosFragment: TodosFragment
    private lateinit var addTodoFragment: AddTodoFragment

    override lateinit var presenter: MainContract.Presenter

    override var isChangeFragment: Boolean = false

    // listener
    private lateinit var finishedAddTodoListener: OnFinishedAddTodoListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up the toolbar
        setSupportActionBar(binding.toolbarMain)

        // create the presenter
        presenter = MainPresenter(this)

        // set floating button listener
        binding.fabMain.setOnClickListener(this)

        // create fragment
        addTodoFragment = AddTodoFragment().apply { finishedAddTodoListener = this }
        todosFragment = TodosFragment()

        // hosting TodosFragment on MainActivity
        showTodosFragment()
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
     * fab 클릭, 옵션 메뉴 클릭..
     */

    // fab 클릭 이벤트
    override fun onClick(view: View) {
        when (isChangeFragment) {
            false -> {
                showAddTodoFragment()
                showBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
            }
            true -> {
                setExpandedAppBarLayout(true)
                onFinishedAddTodo()
                showBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
            }
        }
    }

//    // option menu 클릭 이벤트
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                true
//            }
//            R.id.save_todo -> {
//                onFinishedAddTodo()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun onFinishedAddTodo() {
        finishedAddTodoListener.onFinishedAddTodo() // 데이터 추가 발생 -> Controller: Model 변경
        supportFragmentManager.popBackStack(
            "addTodoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        showBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
    }

    override fun onBackPressed() {
        if (isChangeFragment)
            showBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
        super.onBackPressed()
    }

    /**
     * View - Fragment 표시
     */
    override fun showTodosFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fl_container_main, todosFragment)
            .commit()
    }

    override fun showAddTodoFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("addTodoFragment")
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            ).replace(R.id.fl_container_main, addTodoFragment).commit()
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