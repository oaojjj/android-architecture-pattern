package com.oaojjj.architecturepattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oaojjj.architecturepattern.databinding.ActivityAddTodoBinding
import com.oaojjj.architecturepattern.model.TodoModel

class AddTodoActivity : AppCompatActivity() {
    private lateinit var activityAddTodoBinding: ActivityAddTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddTodoBinding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(activityAddTodoBinding.root)

        activityAddTodoBinding.addTodo.setOnClickListener {
            setResult(
                RESULT_OK,
                Intent().apply {
                    putExtra(
                        "todo",
                        activityAddTodoBinding.etContents.text.toString()
                    )
                })
            finish()
        }

    }
}