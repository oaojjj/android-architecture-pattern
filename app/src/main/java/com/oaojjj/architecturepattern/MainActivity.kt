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
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var appBarLayout: AppBarLayout

    private lateinit var binding: ActivityMainBinding

    private var menuVisible: Boolean = false

    // addFragment -> False
    // activeListFragment -> true
    private var changeViewFlag = true

    // fragment instance
    private var activeListFragment = ActiveListFragment()
    private lateinit var addTodoFragment: AddTodoFragment

    // listener
    private lateinit var finishedAddTodoListener: OnFinishedAddTodoListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        bottomAppBar = binding.babMain
        appBarLayout = binding.appBarLayout
        showOptionMenu(false)

        // Controller: Model 세팅(초기화)
        Thread { TodoModel.instantiate(applicationContext) }.start()

        // set listener
        binding.fabMain.setOnClickListener(this)

        // host Activity on Fragment
        createActiveListFragment()
    }

    /**
     * View
     * ActiveListFragment 생성
     */
    private fun createActiveListFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fl_container_main, activeListFragment)
            .commit()
    }


    /**
     * View
     * AddTodoFragment 생성
     */
    private fun createAddTodoFragment() {
        setExpandedAppBarLayout(true)

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

    /**
     * View
     * change bottom fab position(fab, bottomAbbBar)
     */
    private fun changeBottomAnimation(ResId: Int, fabAlignmentMode: Int) {
        showOptionMenu(changeViewFlag)
        changeViewFlag = !changeViewFlag
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
     * View
     * 옵션 메뉴 조작
     * menuVisible:
     * true -> show optionMenu
     * false -> hide optionMenu
     */
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

    private fun showOptionMenu(isShow: Boolean) {
        invalidateOptionsMenu()
        menuVisible = isShow
    }

    /**
     * View
     * 상단 액션바, 하단 바텀바 조작
     */
    fun showBottomAppBar(isShow: Boolean) {
        when (isShow) {
            true -> bottomAppBar.behavior.slideUp(bottomAppBar)
            false -> bottomAppBar.behavior.slideDown(bottomAppBar)
        }
    }

    private fun setExpandedAppBarLayout(b: Boolean) {
        appBarLayout.setExpanded(b)
    }


    /**
     * Controller
     * fab 클릭, 옵션 메뉴 클릭..
     */

    // fab 클릭 이벤트
    override fun onClick(view: View) {
        Log.d("MainActivity", "onClick: ${supportFragmentManager.backStackEntryCount}")
        when (changeViewFlag) {
            true -> {
                createAddTodoFragment()
                changeBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
            }
            false -> {
                onFinishedAddTodo()
                changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
            }
        }
    }

    // option menu 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.save_todo -> {
                onFinishedAddTodo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onFinishedAddTodo() {
        finishedAddTodoListener.onFinishedAddTodo() // 데이터 추가 발생 -> Controller: Model 변경
        supportFragmentManager.popBackStack(
            "addTodoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
    }


    override fun onBackPressed() {
        if (!changeViewFlag)
            changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
        super.onBackPressed()
    }
}