package com.oaojjj.architecturepattern

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.frgment.ActiveListFragment
import com.oaojjj.architecturepattern.frgment.AddTodoFragment
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener
import com.oaojjj.architecturepattern.model.TodoModel

// 안드로이드에서 MVC 구조는 activity(or fragment)가 controller 와 view 의 역할을 수행한다.
// view는 xml_layout 자체이다.
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var menuVisible: Boolean = false
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var appBarLayout: AppBarLayout

    private lateinit var binding: ActivityMainBinding
    private var changeFlag = true

    // fragment instance
    private var activeListFragment = ActiveListFragment()
    private lateinit var addTodoFragment: AddTodoFragment

    // listener
    private lateinit var finishedAddTodoListener: OnFinishedAddTodoListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // setting appbar
        setSupportActionBar(binding.toolbar)
        bottomAppBar = binding.babMain
        appBarLayout = binding.appBarLayout

        // model
        Thread { TodoModel.setContext(applicationContext) }.start()

        // view, controller
        setContentView(binding.root)

        // set listener
        binding.fabMain.setOnClickListener(this)

        // fragment
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container_main, activeListFragment)
            .commit()
    }

    override fun onClick(view: View) {
        Log.d("MainActivity", "onClick: ${supportFragmentManager.backStackEntryCount}")
        when (changeFlag) {
            true -> {
                onAddTodo()
                changeBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
            }
            false -> {
                onFinishedAddTodo()
                changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
            }
        }
    }

    private fun onAddTodo() {
        addTodoFragment = AddTodoFragment().apply { finishedAddTodoListener = this }
        supportFragmentManager.beginTransaction()
            .addToBackStack("addTodoFragment")
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            ).replace(R.id.fl_container_main, addTodoFragment).commit()

        Log.d("main", "onAddTodo: ${supportFragmentManager.backStackEntryCount}")
    }

    private fun onFinishedAddTodo() {
        finishedAddTodoListener.onFinishedAddTodo()

        supportFragmentManager.popBackStack(
            "addTodoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    /**
     *  change bottom fab position(fab, bottomAbbBar)
     */
    private fun changeBottomAnimation(ResId: Int, fabAlignmentMode: Int) {
        changeFlag = !changeFlag
        binding.babMain.fabAlignmentMode = fabAlignmentMode
        Handler(Looper.getMainLooper()).apply {
            postDelayed({
                binding.fabMain.setImageDrawable(
                    ContextCompat.getDrawable(this@MainActivity, ResId)
                )
            }, 300)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_todo, menu)
        val menuItem = menu?.findItem(R.id.save_todo)

        return if (menuVisible) {
            menuItem?.isVisible = true
            true
        } else {
            menuItem?.isVisible = false
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.save_todo -> {
                onFinishedAddTodo()
                changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // fragment에서 뒤로가기 눌렀을 때 호출된다.
    // 현재는 AddTodoFragment 한개에서만 호출되서 따로 인터페이스 구현은 안해도 될듯?
    override fun onBackPressed() {
        if (!changeFlag) {
            changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
        }
        super.onBackPressed()
    }

    /**
     * control appbar
     * 상단 액션바, 하단 바텀바 - view 조작
     */
    fun showBottomAppBar() {
        bottomAppBar.behavior.slideUp(bottomAppBar)
    }

    fun hideBottomAppBar() {
        bottomAppBar.behavior.slideDown(bottomAppBar)
    }

    fun expendedAppBarLayout() {
        appBarLayout.setExpanded(true)
    }

    fun collapsedAppBarLayout() {
        appBarLayout.setExpanded(false)
    }

    fun showOptionMenu(isShow: Boolean) {
        invalidateOptionsMenu()
        menuVisible = isShow
    }
}