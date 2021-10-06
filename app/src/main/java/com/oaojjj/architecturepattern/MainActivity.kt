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
                    addTodo(contents)
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

    // 팝업 메뉴 생성
    private fun buildPopupMenu(view: View?) {
        val popupMenu =
            PopupMenu(this, view).apply { setOnMenuItemClickListener(this@MainActivity) }
        menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
        popupMenu.show()
    }

    // view, controller
    // 수정버튼 클릭 시 다이얼로그 생성
    private fun buildAlertDialog() {
        val et = EditText(this)
        AlertDialog.Builder(this).apply {
            setTitle("수정")
            setMessage("수정할 내용 입력")
            setView(et)
            setPositiveButton("확인") { _, _ -> updateTodo(et.text.toString()) }
        }.show()
    }

    // controller
    // 사용자 이벤트 발생
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_update -> {
                buildAlertDialog()
                true
            }
            R.id.btn_remove -> {
                removeTodo()
                true
            }
            else -> false
        }
    }

    override fun onClick(v: View?) {
        activityResultLauncher.launch(Intent(this, AddTodoActivity::class.java))
        overridePendingTransition(0, 0)
    }

    override fun onTodoLongClick(view: View?, position: Int) {
        Log.d("main_test", "onTodoLongClick: $position")
        todoModel.setPosition(position)
        buildPopupMenu(view)
    }

    override fun onTodoCheckClick(position: Int, checked: Boolean) {
        updateCheckedTodo(position, checked)
    }


    /**
     * 사용자 이벤트 발생 하고 호출되는 메소드(callback)
     * add, remove, update ...등
     * controller -> Model 에 데이터 추가 요청
     * Model 에서 데이터를 조작 후 View 는 UI만 갱신
     */

    // 데이터 추가
    private fun addTodo(contents: String?) {
        if (contents != null) {
            todoModel.addTodo(contents)
            mAdapter.notifyItemInserted(todoModel.size())
        }
    }

    // 데이터 수정
    private fun updateTodo(contents: String) {
        todoModel.updateTodo(contents)
        mAdapter.notifyItemChanged(todoModel.getPosition())
    }

    // 데이터 삭제
    private fun removeTodo() {
        todoModel.removeTodo()
        mAdapter.notifyItemRemoved(todoModel.getPosition())
        mAdapter.notifyItemRangeChanged(todoModel.getPosition(), todoModel.size())
    }

    // 데이터 수정(체크 유무)
    private fun updateCheckedTodo(position: Int, checked: Boolean) {
        todoModel.updateChecked(position, checked)
        mAdapter.notifyItemChanged(position)
    }

}