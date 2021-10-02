package com.example.calculator1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var workings: TextView
    lateinit var result: TextView

    private var canAddOperation = false  //for operation adding validation in 'workings' env
    private var canAddDecimal = true// for '.' adding validation in 'workings' env

    fun dotCount() : Int=workings.text.count { it=='.' }

    fun operatorCount() : Int= workings.text.count{ it == '/' } +workings.text.count{ it == 'X' }+
            workings.text.count{ it == '+' } +workings.text.count{ it == '-' }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootLayout = findViewById<LinearLayout>(R.id.root_layout)
        workings = findViewById(R.id.workings)
        result = findViewById(R.id.result)

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

    //
    fun NumberAction(view: View) {
        if (view is Button) {
               //avelacnel, vor  menak mi hat dot lini numberneri mej !
                   //tarberak , check if number of  operations is less than number of dots BY 1
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

    fun AllClearAction(view: View) {  // ok
        //  workings.text=""
        workings.text = ""
        result.text = ""

    }

    fun BackspaceAction(view: View) {   // ok
        val length = workings.length()
        if (length > 0) {
            workings.text = workings.text.subSequence(0, length - 1)
        }
    }

    fun EqualsAction(view: View) { //ok
        if(workings.text[workings.text.lastIndex].isDigit())
             result.text = calculateResult()
       /* if(workings.text.contains(".*\\/0([^.]|$|\\.(0{2,0}.*|0{1,4}([^0-9]|$))).*"))
            result.setText("cant divide by zero!")*/
        else{
            //vor . kam operator lini verjum , bani tegh chdni
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
        val new_list = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char &&  i!=passedList.lastIndex && i <restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as  Float
                when(operator){
                    'X' ->{
                        new_list.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->{
                        new_list.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else ->
                    {
                        new_list.add(prevDigit)
                        new_list.add(operator)
                    }
                }
            }
            if(i> restartIndex){
                new_list.add(passedList[i])
            }
        }
        return new_list
    }

    private fun addSubtractionCalculate(passedList: MutableList<Any>): Float {
        val list : MutableList<Any> = passedList
        var result = passedList[0] as Float
        for(i in list.indices){
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