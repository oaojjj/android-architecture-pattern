package com.oaojjj.architecturepattern.addedittodo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.FragmentAddEditTodoBinding
import com.oaojjj.architecturepattern.main.MainPresenter
import com.oaojjj.architecturepattern.util.Util

class AddEditTodoFragment(private val mainPresenter: MainPresenter) : Fragment(),
    AddEditTodoContract.View {
    private var _binding: FragmentAddEditTodoBinding? = null
    private val binding get() = _binding!!

    override lateinit var presenter: AddEditTodoContract.Presenter

    override fun onAttach(context: Context) {
        Log.d("lifecycle_AddEdit", "onAttach: ")
        setHasOptionsMenu(true)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle_AddEdit", "onCreate: ")
        presenter = mainPresenter.addEditTodoPresenter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("lifecycle_AddEdit", "onCreateView: ")
        _binding = FragmentAddEditTodoBinding.inflate(inflater, container, false)

        // set up the toolbar
        val supportActionbar = (requireActivity() as AppCompatActivity).supportActionBar
        setToolbar(supportActionbar, getString(R.string.todo_add), true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("lifecycle_AddEdit", "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        showInput()
    }

    override fun showInput() {
        Handler(Looper.getMainLooper()).postDelayed({
            Util.showInput(requireContext(), binding.etTodoContents)
        }, 300)
    }

    override fun onStart() {
        Log.d("lifecycle_AddEdit", "onStart: ")
        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("lifecycle_AddEdit", "onSaveInstanceState: ")
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        Log.d("lifecycle_AddEdit", "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.d("lifecycle_AddEdit", "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.d("lifecycle_AddEdit", "onStop: ")
        super.onStop()
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d("lifecycle_AddEdit", "onViewStateRestored: ")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        Log.d("lifecycle_AddEdit", "onDestroy: ")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("lifecycle_AddEdit", "onDetach: ")
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_todo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Log.d("TodosFragment", "onOptionsItemSelected: home")
            }
            R.id.save_todo -> {
                // TODO: 2021-10-27
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        Log.d("lifecycle_AddEdit", "onDestroyView: ")
        super.onDestroyView()
        Util.hideInput(requireActivity().currentFocus)
        _binding = null
    }

}