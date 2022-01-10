package com.oaojjj.architecturepattern.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoFragment
import com.oaojjj.architecturepattern.todos.TodosFragment

class FragmentFactoryImpl : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            TodosFragment::class.java.name -> TodosFragment()
            AddEditTodoFragment::class.java.name -> AddEditTodoFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}