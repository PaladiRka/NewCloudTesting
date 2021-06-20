package com.mole.android.newcloudtesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity(), MainView {

    private val presenter: Presenter = Presenter()
    private lateinit var myTextView: MyTextView
    private lateinit var myStylePanel: MyStylePanel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myTextView = findViewById(R.id.textView)
        myTextView.text = resources.getString(R.string.main_text)

        myStylePanel = findViewById(R.id.stylePanel)

        myStylePanel.onStateChangeListener = OnStateChange { _, _ ->
            Log.i(TAG, "state: ${myStylePanel.states}")
            presenter.onStateChange()
        }

        presenter.attachView(this)
    }

    override fun setBold(isBold: Boolean) {
        myTextView.setBold(isBold)
    }

    override fun setItalic(isItalic: Boolean) {
        myTextView.setItalic(isItalic)
    }

    override fun setSize(size: Int){
        myTextView.setSize(size)
    }

    override fun getState(): MutableMap<Int, Int> {
        return myStylePanel.states
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
