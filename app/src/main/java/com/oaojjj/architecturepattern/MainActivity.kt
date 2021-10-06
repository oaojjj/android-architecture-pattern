package com.oaojjj.architecturepattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.oaojjj.architecturepattern.adapter.TodoAdapter
import com.oaojjj.architecturepattern.databinding.ActivityMainBinding
import com.oaojjj.architecturepattern.listener.OnTodoClickListener
import com.oaojjj.architecturepattern.model.TodoModel

// 안드로이드에서 MVC 구조는 activity가 controller와 view의 역할을 수행한다.
// view는 xml_layout 자체이다.
class MainActivity : AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener,
    OnTodoClickListener {
    private val TAG: String? = "test"
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var todoModel: TodoModel
    private lateinit var mAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        // view, controller
        setContentView(activityMainBinding.root)
        activityMainBinding.fabAdd.setOnClickListener(this)

        // model
        todoModel = TodoModel(this)

        // init data, adapter
        mAdapter = TodoAdapter(this, todoModel.getDataList())
            .apply { setOnTodoClickListener(this@MainActivity) }
        activityMainBinding.rvTodo.adapter = mAdapter
        activityMainBinding.rvTodo.layoutManager = LinearLayoutManager(this)

        // instead startActivityResult
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val contents = it.data?.getStringExtra("todo")
                    if (contents != null) {
                        // model에 data 추가를 요청하고 ui 갱신
                        todoModel.addTodo(contents)
                        mAdapter.notifyItemInserted(todoModel.size())
                    }
                }
            }

        // to use the menu
        registerForContextMenu(activityMainBinding.rvTodo)
    }

    // view
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.menu_main, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    // 데이터 추가 -> 사용자 이벤트 발생
    override fun onClick(v: View?) {
        val intent = Intent(this, AddTodoActivity::class.java)
        activityResultLauncher.launch(intent)
        overridePendingTransition(0, 0)
    }

    // controller
    // 데이터 수정, 삭제 -> 사용자 이벤트 발생
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_update -> {
                showAlertDialog()
                true
            }
            R.id.btn_remove -> {
                // 데이터 삭제
                todoModel.removeTodo()
                mAdapter.notifyDataSetChanged()
                true
            }
            else -> false
        }
    }

    // view, controller
    // 수정버튼 클릭 시 다이얼로그 생성
    private fun showAlertDialog() {
        // view
        val et = EditText(this)
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle("수정")
            setMessage("수정할 내용 입력")
            setView(et)
        }

        // controller
        alertDialog.setPositiveButton("확인") { _, _ ->
            // 데이터 수정
            todoModel.updateTodo(et.text.toString())
            mAdapter.notifyDataSetChanged()
        }.show()
    }

    override fun onTodoCheckClickListener(position: Int, checked: Boolean) {
        todoModel.updateChecked(position, checked)
    }

    override fun onTodoLongClickListener() {
        TODO("Not yet implemented")
    }

}