package com.oaojjj.architecturepattern

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.oaojjj.architecturepattern.model.Todo
import com.oaojjj.architecturepattern.model.TodoModel
import kotlinx.android.synthetic.main.activity_main.*

// 안드로이드에서 MVC 구조는 activity가 controller와 view의 역할을 수행한다.
// view는 xml_layout 자체이다.
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var todoModel: TodoModel
    private lateinit var data: MutableList<Todo>
    private lateinit var mAdapter: ArrayAdapter<Todo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view
        setContentView(R.layout.activity_main)

        // model
        todoModel = TodoModel(this)

        // controller
        bt_add.setOnClickListener(this)

        // init data, adapter
        data = todoModel.db().todoDao().getAll()
        mAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, data)
        lv_list.adapter = mAdapter


    }

    // 사용자 이벤트 발생
    override fun onClick(v: View?) {
        val newTodo = Todo(et_content.text.toString())

        // model에 data를 추가를 요청하고 ui를 다시 갱신
        todoModel.db().todoDao().insert(newTodo)
        data.add(newTodo)
        mAdapter.notifyDataSetChanged()

        et_content.setText("")
    }
}