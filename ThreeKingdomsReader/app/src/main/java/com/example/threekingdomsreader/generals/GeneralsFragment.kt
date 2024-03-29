package com.example.threekingdomsreader.generals

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.databinding.FragmentGeneralsBinding
import com.example.threekingdomsreader.main.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GeneralsFragment : Fragment(), GeneralsContract.View {
    private var _binding: FragmentGeneralsBinding? = null

    private val binding get() = _binding!!

    override var isActive: Boolean = false
        get() = isAdded

    override lateinit var presenter: GeneralsContract.Presenter

    private var itemListener: GeneralItemListener = object : GeneralItemListener {
        override fun onGeneralClick(clickGeneral: General) {
            TODO("Not yet implemented")
        }

    }

    private var listAdapter: GeneralsAdapter = GeneralsAdapter(ArrayList(0), itemListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvGenerals) {
            adapter = listAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            itemAnimator = null
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
        presenter.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setLoadingIndicator(active: Boolean) {
        // set the swipeRefreshLayout
        with(binding.srlGenerals) {
            post { isRefreshing = active }
        }


    }

    override fun showGenerals(generals: List<General>) {
        Log.d("GeneralsFragment", "showGenerals: $generals")
        listAdapter.generals = generals
        binding.rvGenerals.visibility = View.VISIBLE
        binding.noGenerals.visibility = View.GONE
    }


    override fun showGeneral(general: General) {

    }

    override fun showNoGenerals() {
        binding.rvGenerals.visibility = View.GONE
        binding.noGenerals.visibility = View.VISIBLE
    }


    override fun showLoadingGeneralsError() {
        TODO("Not yet implemented")
    }

    override fun showAllGeneralsFilterText() {
        (requireActivity() as MainActivity).title = resources.getString(R.string.text_all)
    }

    override fun showMarkGeneralsFilterText() {
        (requireActivity() as MainActivity).title = resources.getString(R.string.text_mark)
    }
}