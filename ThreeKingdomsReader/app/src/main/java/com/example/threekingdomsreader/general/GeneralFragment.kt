package com.example.threekingdomsreader.general

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.forEach
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.databinding.FragmentGeneralBinding
import com.example.threekingdomsreader.main.MainActivity
import com.example.threekingdomsreader.main.MainPresenter
import com.example.threekingdomsreader.util.Utils

class GeneralFragment(private val mainPresenter: MainPresenter) : Fragment(), GeneralContract.View {
    private var _binding: FragmentGeneralBinding? = null

    private val binding get() = _binding!!

    override lateinit var presenter: GeneralContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("frag2Life", "onCreateView")
        _binding = FragmentGeneralBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("frag2Life", "onViewCreated")
        binding.container.setOnScrollChangeListener { _, _, scrollY, _, oldY ->
            Log.d("scroll", "onViewCreated: $scrollY")
            mainPresenter.scrollControl(scrollY, oldY)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("frag2Life", "onResume")

        // change toolbar title
        (requireActivity() as MainActivity).title = resources.getString(R.string.general_info)

        presenter.start()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_general, menu)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        when (item.itemId) {

        }
    }

    override fun setGeneral(general: General) {
        with(binding) {
            nameGeneral.text = general.name
            sexGeneral.text = general.sex
            belongGeneral.text = general.belong
            positionGeneral.text = general.position
            dateGeneral.text = (general.birth.toString() + "~" + general.death.toString())
            descriptionGeneral.text = general.description

            Utils.getColorAccordingBelong(requireContext(), general.belong).let {
                container.setBackgroundColor(it)
            }
        }
    }

    override fun showEmptyGeneralError() {
        Toast.makeText(context, "무장 정보가 없습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("frag2Life", "onDestroyView")
        _binding = null
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("frag2Life", "onAttach: $isActive")
    }

    override fun onStart() {
        super.onStart()
        Log.d("frag2Life", "onStart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("frag2Life", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("frag2Life", "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("frag2Life", "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("frag2Life", "onDetach: ")
    }


}