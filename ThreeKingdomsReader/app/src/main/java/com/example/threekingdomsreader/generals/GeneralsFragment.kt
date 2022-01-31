package com.example.threekingdomsreader.generals

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.databinding.FragmentGeneralsBinding
import com.example.threekingdomsreader.main.MainActivity
import com.example.threekingdomsreader.main.MainPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GeneralsFragment(private val mainPresenter: MainPresenter) : Fragment(),
    GeneralsContract.View {
    private var _binding: FragmentGeneralsBinding? = null

    private val binding get() = _binding!!

    override var isActive: Boolean = false
        get() = isAdded

    override lateinit var presenter: GeneralsContract.Presenter

    private var itemListener: GeneralItemListener = object : GeneralItemListener {
        override fun onGeneralClick(clickGeneral: General) {
            showGeneral(clickGeneral)
        }

    }

    private var listAdapter: GeneralsAdapter = GeneralsAdapter(ArrayList(0), itemListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("fragLife", "onCreateView: ")
        _binding = FragmentGeneralsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("fragLife", "onViewCreated: ")
        with(binding.rvGenerals) {
            adapter = listAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            itemAnimator = null
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    mainPresenter.scrollControl(dy, 0)
                }
            })
        }


        // set up progress indicator
        binding.srlGenerals.setOnRefreshListener {
            presenter.loadGenerals(false)
        }

        requireActivity()
            .findViewById<FloatingActionButton>(R.id.fab_add_general)
            .setOnClickListener {
                listAdapter.generals = mutableListOf()
                showNoGenerals()
            }
    }

    override fun onResume() {
        super.onResume()
        Log.d("fragLife", "onResume: ")
        presenter.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("fragLife", "onDestroyView: ")
        _binding = null
    }

    override fun setLoadingIndicator(active: Boolean) {
        with(binding.srlGenerals) {
            post { isRefreshing = active }
        }
    }

    override fun showGenerals(generals: List<General>) {
        listAdapter.generals = generals
        binding.rvGenerals.visibility = View.VISIBLE
        binding.noGenerals.visibility = View.GONE
    }

    override fun showGeneral(general: General) {
        mainPresenter.addEditDetailGeneral(general)
    }

    override fun showNoGenerals() {
        binding.rvGenerals.visibility = View.GONE
        binding.noGenerals.visibility = View.VISIBLE
    }


    override fun showLoadingGeneralsError() {
        showMessage(getString(R.string.loading_generals_error))
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun showAllGeneralsFilterText() {
        (requireActivity() as MainActivity).title = resources.getString(R.string.text_all)
    }

    override fun showMarkGeneralsFilterText() {
        (requireActivity() as MainActivity).title = resources.getString(R.string.text_mark)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("fragLife", "onAttach: $isActive")
    }

    override fun onStart() {
        super.onStart()
        Log.d("fragLife", "onStart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("fragLife", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("fragLife", "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("fragLife", "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("fragLife", "onDetach: ")
    }

}