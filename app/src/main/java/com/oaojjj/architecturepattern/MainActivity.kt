package com.oaojjj.architecturepattern

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.frgment.ActiveListFragment
import com.oaojjj.architecturepattern.frgment.AddTodoFragment
import com.oaojjj.architecturepattern.listener.OnFinishedAddTodoListener

// 안드로이드에서 MVC 구조는 activity(or fragment)가 controller 와 view 의 역할을 수행한다.
// view는 xml_layout 자체이다.
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private var fabFlag = true

    // fragment instance
    private var activeListFragment = ActiveListFragment()
    private lateinit var addTodoFragment: AddTodoFragment

    // listener
    private lateinit var finishedAddTodoListener: OnFinishedAddTodoListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

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
        when (fabFlag) {
            true -> onAddTodo()
            false -> onFinishedAddTodo()
        }
    }

    private fun onAddTodo() {
        addTodoFragment = AddTodoFragment().apply { finishedAddTodoListener = this }
        supportFragmentManager.beginTransaction()
            .addToBackStack("addTodoFragment")
            .replace(R.id.fl_container_main, addTodoFragment)
            .commit()

        Log.d("main", "onAddTodo: ${supportFragmentManager.backStackEntryCount}")
        changeBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
    }

    private fun onFinishedAddTodo() {
        finishedAddTodoListener.onFinishedAddTodo()
        supportFragmentManager.popBackStack(
            "addTodoFragment",
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        Log.d("main", "finishedAddTodo: ${supportFragmentManager.backStackEntryCount}")
        changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
    }

    /**
     *  change bottom fab position(fab, bottomAbbBar)
     */
    private fun changeBottomAnimation(ResId: Int, fabAlignmentMode: Int) {
        fabFlag = !fabFlag
        Handler(Looper.getMainLooper()).apply {
            postDelayed({
                binding.fabMain.setImageDrawable(
                    ContextCompat.getDrawable(this@MainActivity, ResId)
                )
            }, 300)
        }

        binding.babMain.fabAlignmentMode = fabAlignmentMode
    }

    override fun onBackPressed() {
        if (!fabFlag) {
            Log.d("main", "onBackPressed: ${supportFragmentManager.backStackEntryCount}")
            changeBottomAnimation(R.drawable.add, BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
        }
        super.onBackPressed()
    }
}