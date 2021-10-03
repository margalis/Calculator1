package com.example.calculator1

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

const val WORKINGS_KEY = "workings_key"
const val RESULT_KEY ="result_key"
class MainActivity : AppCompatActivity() {
    lateinit var workings: TextView
    lateinit var result: TextView

    private var canAddOperation = false  //for operation adding validation in 'workings' env
    private var canAddDecimal = true// for '.' adding validation in 'workings' env

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootLayout = findViewById<LinearLayout>(R.id.root_layout)
        workings = findViewById(R.id.workings)
        result = findViewById(R.id.result)

        savedInstanceState?.let { data->
            workings.text= data.getString(WORKINGS_KEY)
            result.text = data.getString(RESULT_KEY)
        }

    }

    // lifecycle
    override fun onStart() {
        super.onStart()
        println("on Start")
    }

    override fun onResume() {
        super.onResume()
        println("on Resume")
    }

    override fun onPause() {
        super.onPause()
        println("on Pause")
    }

    override fun onStop() {
        super.onStop()
        println("on Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("on Destroy")
    }

    override fun onRestart() {
        super.onRestart()
        println("on Restart")
    }

   /* override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }*/

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(WORKINGS_KEY,workings.text?.toString())
        outState.putString(RESULT_KEY,result.text?.toString())
    }

    fun NumberAction(view: View) {
        if (view is Button) {

            if (view.text == ".") {

                if (canAddDecimal && workings.text[workings.text.lastIndex].isDigit()) {
                    workings.append(view.text)
                    canAddOperation = false
                }
                //canAddOperation = false
                canAddDecimal = false
            }
            else {
                workings.append(view.text)
               // canAddDecimal = true
                canAddOperation = true
            }
        }
    }

    fun OperationAction(view: View) {
        if (view is Button && canAddOperation) {
            workings.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun AllClearAction(view: View) {
        workings.text = ""
        result.text = ""

    }

    fun BackspaceAction(view: View) {
        val length = workings.length()
        if (length > 0) {
            workings.text = workings.text.subSequence(0, length - 1)
        }
    }

    fun EqualsAction(view: View) {
        if(workings.text.isEmpty()) {  }
        else if(workings.text[workings.text.lastIndex].isDigit())
             result.text = calculateResult()
       /* if(workings.text.contains(".*\\/0([^.]|$|\\.(0{2,0}.*|0{1,4}([^0-9]|$))).*"))
            result.setText("cant divide by zero!")*/
        else{

        }
    }

    private fun calculateResult(): String {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""   // ok

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""   //ok

        val result = addSubtractionCalculate(timesDivision)
        return result.toString()
    }

    private fun digitsOperators() : MutableList<Any>{ // workings-ic generate anel number-neri u
        // operator-neri list                        // ok
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workings.text){
            if(character.isDigit() || character=='.'){
                currentDigit += character
            }
            else{
                list.add(currentDigit.toFloat())
                currentDigit=""
                list.add(character)
            }
        }
        if (currentDigit!="")
            list.add(currentDigit.toFloat())

        return list
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>):MutableList<Any> {
        var list = passedList
        while('X' in list || '/' in list){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for(i in passedList.indices){
            if(passedList[i] is Char &&  i!=passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as  Float
                when(operator){
                    'X' ->{
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->{
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i> restartIndex){
                newList.add(passedList[i])
            }
        }
        return newList
    }

    private fun addSubtractionCalculate(passedList: MutableList<Any>): Float {
        val list : MutableList<Any> = passedList // just exp
        var result = passedList[0] as Float
        for(i in list.indices){ // 1 iter avel a
                if(list[i] is Char) {
                    val operator = list[i]
                    val nextDigit = list[i + 1] as Float
                    if (operator == '+') {
                        result += nextDigit
                    }
                    if (operator == '-') {
                        result -= nextDigit
                    }
                }
        }
        return result
    }
}