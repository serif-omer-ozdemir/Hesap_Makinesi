package com.info.hesapmakinesi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.info.hesapmakinesi.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tasarim: ActivityMainBinding
    private lateinit var expression: Expression

    var lastNumeric: Boolean = false
    var stateError: Boolean = false
    var lastDot: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)
    }

    fun onAllclearClick(view: View) { // view tasarima erişmemıı saglar
        tasarim.dataTv.text = ""
        tasarim.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        tasarim.resultTv.visibility = View.GONE
    }


    fun onEqualClick(view: View) {
        onEqual()
        tasarim.dataTv.text = tasarim.resultTv.text.toString().drop(1)
    }


    fun onDigitClick(view: View) {

        if (stateError) {
            tasarim.dataTv.text = (view as Button).text
            stateError = false

        } else {
            tasarim.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }


    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric) {
            tasarim.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }


    fun onBackClick(view: View) {

        tasarim.dataTv.text = tasarim.dataTv.text.toString().drop(1)
        try {
            val lastCharter = tasarim.dataTv.text.toString().last()

            if (lastCharter.isDigit()) {
                onEqual()
            }

        } catch (e: Exception) {
            tasarim.resultTv.text = ""
            tasarim.resultTv.visibility = View.GONE
            Log.e("Last Char Error", e.toString())
        }

    }


    fun onClearClick(view: View) {

        tasarim.dataTv.text = ""
        lastNumeric = false

    }

    fun onEqual() {

        if (lastNumeric && !stateError) {
            val txt = tasarim.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                tasarim.resultTv.visibility = View.VISIBLE
                tasarim.resultTv.text = "=" + result.toString()

            } catch (e: ArithmeticException) {
                Log.e("Su hata goruldu:", e.toString())
                tasarim.resultTv.text = "HATA"
                stateError = true
                lastNumeric = false

            }
        }
    }


}