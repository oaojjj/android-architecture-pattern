package com.example.threekingdomsreader.general

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.databinding.FragmentGeneralBinding
import com.example.threekingdomsreader.main.MainActivity
import com.example.threekingdomsreader.main.MainPresenter
import com.example.threekingdomsreader.util.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GeneralFragment(private val mainPresenter: MainPresenter) : Fragment(), GeneralContract.View {
    private var _binding: FragmentGeneralBinding? = null

    private val binding get() = _binding!!

    override lateinit var presenter: GeneralContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    // 상태가 true면 변경 불가능, false면 변경 가능
    var lock: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.container.setOnScrollChangeListener { _, _, scrollY, _, oldY ->
            if (lock) mainPresenter.scrollControl(scrollY, oldY)
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fab_add_general)
            .setOnClickListener {
                val general = newGeneral()
                presenter.saveGeneral(general)
            }

        binding.imageGeneral.setOnClickListener {
            showImageSaveDialog()
        }
    }

    private fun newGeneral(): General =
        with(binding) {
            val date = dateGeneral.text.toString().trim().split('~')
            if (date.size < 2) return General(id = null)

            General(
                name = nameGeneral.text.toString(),
                sex = sexGeneral.text.toString(),
                belong = belongGeneral.text.toString(),
                position = positionGeneral.text.toString(),
                birth = date[0],
                death = date[1],
                description = descriptionGeneral.text.toString(),
                id = null
            )
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

        presenter.checkViewState(menu.findItem(R.id.lock_general), lock)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lock_general -> requireActivity().invalidateOptionsMenu()
            R.id.delete_general -> presenter.deleteGeneral()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setGeneral(general: General) {
        Log.d("generalFragment", "setGeneral: $general")
        with(binding) {
            showGeneralImage(general.image)
            nameGeneral.setText(general.name)
            sexGeneral.setText(general.sex)
            belongGeneral.setText(general.belong)
            positionGeneral.setText(general.position)
            dateGeneral.setText((general.birth + "~" + general.death))
            descriptionGeneral.setText(general.description)

            Utils.getColorAccordingBelong(requireContext(), general.belong).let {
                container.setBackgroundColor(it)
            }
        }
    }

    override fun showEmptyGeneralError() {
        Toast.makeText(context, getString(R.string.empty_general_message), Toast.LENGTH_SHORT)
            .show()
    }

    override fun enableEditView(item: MenuItem) {
        item.setIcon(R.drawable.ic_unlock)
        binding.frameView.children.forEach { it.isEnabled = true }

        requireActivity().findViewById<FloatingActionButton>(R.id.fab_add_general)
            .setImageResource(R.drawable.ic_edit)

        mainPresenter.view.fabShow()
        lock = true
    }

    override fun unableEditView(item: MenuItem) {
        item.setIcon(R.drawable.ic_lock)
        binding.frameView.children.forEach { it.isEnabled = false }
        mainPresenter.view.fabHide()
        lock = false
    }

    override fun showImageSaveDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setView(R.layout.dialog_pick_image)
            setPositiveButton("저장") { dialog, _ ->
                val url =
                    (dialog as AlertDialog).findViewById<EditText>(R.id.et_image_url).text.toString()
                presenter.cachedGeneralImageUrl(url)
            }
            setNegativeButton("취소", null)
        }.create().show()
    }

    override fun showGeneralImage(url: String) {
        Glide.with(this@GeneralFragment).load(url)
            .placeholder(R.drawable.ic_error)
            .error(R.drawable.ic_error)
            .into(binding.imageGeneral)
    }

    override fun showGenerals() {
        parentFragmentManager.popBackStack()
    }

    override fun showEmptyGeneral() {
        binding.frameView.children.forEach {
            if (it is TextView) it.setTextColor(Color.BLACK)
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fab_add_general)
            .setImageResource(R.drawable.ic_edit)

        lock = false
        requireActivity().invalidateOptionsMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        requireActivity().findViewById<FloatingActionButton>(R.id.fab_add_general)
            .setImageResource(R.drawable.ic_add)

        mainPresenter.view.fabShow()
    }
}