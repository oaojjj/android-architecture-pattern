package com.example.threekingdomsreader.general

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.threekingdomsreader.R

class GeneralFragment : Fragment(), GeneralContract.View {
    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

    override var presenter: GeneralContract.Presenter
        get() = TODO("Not yet implemented")
        set(value) {}

}