package com.oaojjj.architecturepattern

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.frgment.ActiveListFragment
import com.oaojjj.architecturepattern.listener.OnFabClickListener

// 안드로이드에서 MVC 구조는 activity(or fragment)가 controller와 view의 역할을 수행한다.
// view는 xml_layout 자체이다.
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    // fragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private var activeListFragment = ActiveListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // view, controller
        setContentView(binding.root)

        // set listener
        binding.fabMain.setOnClickListener(this)


        // fragment setting
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.fl_container_main, activeListFragment).commit()

    }

    override fun onClick(view: View) {
        activeListFragment.onAddTodo("test")
    }
}