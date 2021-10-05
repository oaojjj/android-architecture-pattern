package com.oaojjj.architecturepattern

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.oaojjj.architecturepattern.model.Todo
import com.oaojjj.architecturepattern.model.TodoDao
import com.oaojjj.architecturepattern.model.TodoModel
import kotlinx.android.synthetic.main.activity_main.*

// 안드로이드에서 MVC 구조는 activity가 controller와 view의 역할을 수행한다.
// view는 xml_layout 자체이다.
class MainActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemLongClickListener, PopupMenu.OnMenuItemClickListener {
    private lateinit var todoModel: TodoDao
    private lateinit var dataList: MutableList<Todo>
    private lateinit var mAdapter: ArrayAdapter<Todo>
    private lateinit var newTodo: Todo
    private var mPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view, controller
        setContentView(R.layout.activity_main)
        btn_add.setOnClickListener(this)
        lv_list.onItemLongClickListener = this

        // model
        todoModel = TodoModel(this).db().todoDao()

        // init data, adapter
        dataList = todoModel.getAll()
        mAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, dataList)
        lv_list.adapter = mAdapter

        // to use the menu
        registerForContextMenu(lv_list)
    }

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
        newTodo = Todo(et_content.text.toString()) // 새로운 데이터

        // model에 data를 추가를 요청하고 ui를 다시 갱신
        // 데이터 변경
        todoModel.insert(newTodo)
        dataList.add(newTodo)
        mAdapter.notifyDataSetChanged()

        et_content.setText("")
    }

    // view, controller -> show popup menu
    override fun onItemLongClick(
        adapterView: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val popupMenu =
            PopupMenu(this, view).apply { setOnMenuItemClickListener(this@MainActivity) }
        menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
        popupMenu.show()

        mPosition = position
        return true
    }

    // controller
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_update -> {
                showAlertDialog()
                true
            }
            R.id.btn_remove -> {
                // 데이터 변경
                todoModel.delete(todoModel.getTodo(dataList[mPosition].content))
                dataList.removeAt(mPosition)
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
            newTodo = Todo(et.text.toString()).apply {
                id = todoModel.getTodo(dataList[mPosition].content).id
            }

            // 데이터 변경
            todoModel.update(newTodo)
            dataList[mPosition] = newTodo
            mAdapter.notifyDataSetChanged()
        }
        alertDialog.show()
    }


}