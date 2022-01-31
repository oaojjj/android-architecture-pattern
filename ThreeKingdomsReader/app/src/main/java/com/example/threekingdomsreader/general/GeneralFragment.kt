package com.example.threekingdomsreader.general

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.databinding.FragmentGeneralBinding
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
        _binding = FragmentGeneralBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.container.setOnScrollChangeListener { _, _, scrollY, _, oldY ->
            Log.d("scroll", "onViewCreated: $scrollY")
            mainPresenter.scrollControl(scrollY, oldY)
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.start()
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
        _binding = null
    }

}