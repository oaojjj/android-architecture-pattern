package com.oaojjj.architecturepattern.frgment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.databinding.DialogUpdateTodoBinding
import com.oaojjj.architecturepattern.listener.OnUpdateTodoListener
import com.oaojjj.architecturepattern.model.TodoModel

class UpdateTodoDialog(var listener: OnUpdateTodoListener, var pos: Int) :
    DialogFragment(),
    View.OnClickListener {

    private lateinit var binding: DialogUpdateTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogUpdateTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etUpdateTodo.setText(TodoModel.getData(pos).content)
        binding.etUpdateTodo.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        binding.btUpdate.setOnClickListener(this)
        binding.btUpdateCancel.setOnClickListener(this)
    }

    // Controller: 사용자가 업데이트 요청 -> Model에 업데이트 요청
    override fun onClick(view: View?) {
        if (view?.id == R.id.bt_update) {
            Thread {
                TodoModel.updateTodo(pos, binding.etUpdateTodo.text.toString())
                requireActivity().runOnUiThread { listener.onUpdateFinished() }
            }.start()
        }
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dismiss()
    }
}