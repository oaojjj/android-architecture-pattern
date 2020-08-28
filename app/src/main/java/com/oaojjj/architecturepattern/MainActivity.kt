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
    private lateinit var data: MutableList<Todo>
    private lateinit var mAdapter: ArrayAdapter<Todo>
    private lateinit var newTodo: Todo
    private var mPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view
        setContentView(R.layout.activity_main)

        // model
        todoModel = TodoModel(this).db().todoDao()

        // controller
        btn_add.setOnClickListener(this)
        lv_list.onItemLongClickListener = this

        // init data, adapter
        data = todoModel.getAll()
        mAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, data)
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

    // Todo데이터 추가 사용자 이벤트 발생
    override fun onClick(v: View?) {
        newTodo = Todo(et_content.text.toString())

        // model에 data를 추가를 요청하고 ui를 다시 갱신
        todoModel.insert(newTodo)
        data.add(newTodo)
        mAdapter.notifyDataSetChanged()

        et_content.setText("")
    }

    // view
    // 리스트 아이템을 길게 클릭할 시 팝업메뉴 생성
    override fun onItemLongClick(
        adapterView: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val popupMenu = PopupMenu(this, view)

        menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.show()
        mPosition = position
        return true
    }

    // controller
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        var id = item!!.itemId
        return when (id) {
            R.id.btn_update -> {
                createAlertDialog()
                true
            }
            R.id.btn_remove -> {
                todoModel.delete(todoModel.getTodo(data[mPosition].content))
                data.removeAt(mPosition)
                mAdapter.notifyDataSetChanged()
                true
            }
            else -> false
        }
    }

    private fun createAlertDialog() {
        // view
        // 수정버튼 클릭 시 다이얼로그 생성
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("수정")
        alertDialog.setMessage("수정할 내용 입력")

        val et = EditText(this)
        alertDialog.setView(et)

        // controller
        alertDialog.setPositiveButton(
            "확인"
        ) { _, _ ->
            newTodo = Todo(et.text.toString()).apply {
                id = todoModel.getTodo(data[mPosition].content).id
            }
            todoModel.update(newTodo)
            data[mPosition] = newTodo
            mAdapter.notifyDataSetChanged()
        }
        alertDialog.show()
    }


}