package com.oaojjj.architecturepattern.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoFragment
import com.oaojjj.architecturepattern.main.MainContract
import com.oaojjj.architecturepattern.main.MainPresenter
import com.oaojjj.architecturepattern.todos.TodosFragment

class FragmentFactoryImpl(presenter: MainContract.Presenter) : FragmentFactory() {
    private val mainPresenter = presenter as MainPresenter
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            TodosFragment::class.java.name -> TodosFragment(mainPresenter)
            AddEditTodoFragment::class.java.name -> AddEditTodoFragment(mainPresenter)
            else -> super.instantiate(classLoader, className)
        }
    }
}