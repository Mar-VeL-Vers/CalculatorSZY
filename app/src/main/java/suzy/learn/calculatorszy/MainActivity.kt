package suzy.learn.calculatorszy


import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.lang.Math.cbrt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

@SuppressLint("SetTextI18n")
@Suppress("SameParameterValue")

class MainActivity : AppCompatActivity() {
    private var tvResult: TextView? = null
    private var menuHistory: MenuItem? = null
    private var canAddPoint = false
    private var canAddDigit = true
    private var contor = 0
    private var canAddMinusSign = true
    private var canAddPlusSign = false
    private var canAddMultiplySign = false
    private var canAddDivisionSign = false
    private var canAddPi = true
    private var canAddEuler = true
    private var alertDialog: AlertDialog? = null
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val btnTrigonometric: Button = findViewById(R.id.btnTrigonometric)
        btnTrigonometric.setOnClickListener {
            val intent = Intent(this, TrigonometricActivity::class.java)
            startActivity(intent)
            finish()
        }
        tvResult = findViewById(R.id.tvResult)
        tvResult?.movementMethod = ScrollingMovementMethod()
        menuHistory = findViewById(R.id.History)
        createDialog()
        
    }
    
    private fun createDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
       
        alertDialogBuilder.setTitle("Delete content???")
        alertDialogBuilder.setMessage("Info cannot be recovered!")
        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            this@MainActivity.deleteFromFile()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        alertDialog = alertDialogBuilder.create()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
    
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    
        R.id.History-> {
            showPopUpHistory()
            true
        }
        R.id.CloseApp1->{
            finish()
            true
        }
    
        else -> {
      
            super.onOptionsItemSelected(item)
        }
    }
    
    @SuppressLint("InflateParams")
    private fun showPopUpHistory() {
        val view = layoutInflater.inflate(R.layout.history_popup, null)
        val window = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window.showAtLocation(view, Gravity.CENTER, 0, 0)
        window.contentView = view
        
        val textHistory = view.findViewById<TextView>(R.id.textHistory)
        textHistory.text = readTextFile("mytextfile.txt")
        textHistory.movementMethod = ScrollingMovementMethod()
        
        val closeHistory = view.findViewById<Button>(R.id.closeText)
        closeHistory.setOnClickListener {
            window.dismiss()
        }
        window.showAsDropDown(closeHistory)
        
        val deleteHistory = view.findViewById<Button>(R.id.deleteHistory)
        
        deleteHistory.setOnClickListener {
            
            alertDialog?.show()
      
    
        }
        
    }
    
    fun canAdDiggitforNeg(view: View) {
        val string = tvResult?.text.toString()
        if (string == "NaN" || string == "Infinity" || string == "null" || string == "error" || string == "-Infinity") {
            tvResult?.text = ""
        }
        if (view is Button && canAddDigit) {
            changeColor(view, "#CDDC39")
            tvResult?.append(view.text)
            canAddPlusSign = true
            canAddMinusSign = true
            canAddMultiplySign = true
            canAddDivisionSign = true
            canAddPoint = true
            canAddPi = false
            canAddEuler = false
        }
    }
    
    private fun changeColor(view: View, colorString: String) {
        if (view is Button) {
           view.setTextColor(Color.parseColor(colorString))
            Timer().schedule(50) {
                view.setTextColor(Color.parseColor("#CDDC39"))
            }
            Timer().schedule(2500) {
                view.setTextColor(Color.parseColor("#FFFFFFFF"))
            }
        }
    }
    
    fun addMinusSign(view: View) {
   
        val string = tvResult?.text.toString()
        if (string.isEmpty()) {
            canAddMinusSign = true
        }
        if (string == "NaN" || string == "Infinity" || string == "null" || string == "error" || string == "-Infinity") {
            tvResult?.text = ""
            canAddMinusSign = true
        }
        if (view is Button && canAddMinusSign) {
            changeColor(view, "#CDDC39")
            tvResult?.append(view.text)
            canAddDigit = true
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = false
            canAddPoint = false
            canAddPi = true
            canAddEuler = true
            contor = 0
        }
    }
    
    fun addPlusSign(view: View) {
        val string = tvResult?.text.toString()
        if (string == "NaN" || string == "Infinity" || string == "null" || string == "error" || string == "-Infinity") {
            tvResult?.text = ""
            canAddPlusSign = false
        }
        if (view is Button && canAddPlusSign && checkIfLastIsDigit(tvResult?.text.toString())) {
            changeColor(view, "#CDDC39")
            tvResult?.append(view.text)
            canAddDigit = true
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = true
            canAddPoint = false
            contor = 0
            canAddPi = true
            canAddEuler = true
        }
    }
    
    fun addMultiplySign(view: View) {
        val string = tvResult?.text.toString()
        if (string == "NaN" || string == "Infinity" || string == "null" || string == "error" || string == "-Infinity") {
            tvResult?.text = ""
            canAddMultiplySign = false
        }
        if (view is Button && canAddMultiplySign) {
            changeColor(view, "#CDDC39")
            tvResult?.append(view.text)
            canAddDigit = true
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = true
            canAddPoint = false
            contor = 0
            canAddPi = true
            canAddEuler = true
            
        }
    }
    
    fun addDivisionSign(view: View) {
        val string = tvResult?.text.toString()
        if (string == "NaN" || string == "Infinity" || string == "null" || string == "error" || string == "-Infinity") {
            tvResult?.text = ""
            canAddDivisionSign = false
        }
        if (view is Button && canAddDivisionSign) {
            changeColor(view, "#CDDC39")
            tvResult?.append(view.text)
            canAddDigit = true
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = true
            canAddPoint = false
            contor = 0
            canAddPi = true
            canAddEuler = true
        }
    }
    
    fun addPoint(view: View) {
        if (view is Button && canAddPoint && contor == 0) {
            changeColor(view, "#CDDC39")
            tvResult?.append(view.text)
            canAddMinusSign = false
            canAddDivisionSign = false
            canAddMultiplySign = false
            canAddPlusSign = false
            canAddPoint = false
            contor += 1
            canAddPi = false
            canAddEuler = false
        }
    }
    
    fun onClear(view: View) {
        if (view is Button) {
            changeColor(view, "#CDDC39")
            tvResult?.text = ""
            canAddPoint = false
            canAddDigit = true
            contor = 0
            canAddMinusSign = true
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            
        }
    }
    
    fun xPatrat(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val length = tvResult?.length()
            if (length != null && length > 0) {
                val ecuatieInCifre = calculateResults()
                val maybeDouble = ecuatieInCifre.toDoubleOrNull()
                if (maybeDouble == null) {
                    tvResult?.text = "error"
                } else {
                    appendCharToFile("\n____________________\n" + "(" + tvResult?.text.toString() + ")\u00B2")
                    maybeDouble.let {
                        val resultat = maybeDouble.pow(2)
                        tvResult?.text = resultat.toString()
                        appendCharToFile("\n=$maybeDouble\u00B2\n=$resultat")
                    }
                }
            } else {
                tvResult?.text = "error"
                appendCharToFile("\n=error")
            }
        }
        
    }
    
    fun doiLaX(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val length = tvResult?.length()
            if (length != null && length > 0) {
                val maybeDouble = calculateResults().toDoubleOrNull()
                if (maybeDouble == null) {
                    tvResult?.text = "error"
                } else {
                    appendCharToFile("\n____________________\n2^(" + tvResult?.text.toString() + ")")
                    maybeDouble.let {
                        val exponent = 2.0
                        val resultat = exponent.pow(maybeDouble)
                        tvResult?.text = resultat.toString()
                        appendCharToFile("\n=2^$maybeDouble" + "\n= " + tvResult?.text.toString())
                    }
                }
            } else
                tvResult?.text = "error"
        }
    }
    
    private fun checkifValidNumber(string: String): Boolean {
        return string.toDoubleOrNull() != null
    }
    
    fun zeceLaX(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val length = tvResult?.length()
            if (length != null && length > 0) {
                val maybeDouble = calculateResults().toDoubleOrNull()
                if (maybeDouble == null) {
                    tvResult?.text = "error"
                } else {
                    appendCharToFile("\n____________________\n10^(" + tvResult?.text.toString() + ")")
                    maybeDouble.let {
                        val exponent = 10.0
                        val resultat = exponent.pow(maybeDouble)
                        tvResult?.text = resultat.toString()
                        appendCharToFile("\n=10^$maybeDouble" + "\n= " + tvResult?.text.toString())
                    }
                }
            } else
                tvResult?.text = "error"
        }
    }
    
    fun nRootX(view: View) {
        changeColor(view, "#CDDC39")
        val length = tvResult?.length()
        if (length != null && length > 0) {
            when {
                checkIfNotValid()                -> {
                    tvResult?.text = ""
                }
                tvResult?.text.toString() == "-" -> {
                    tvResult?.text = "error"
                }
                else                             -> tvResult?.append(" nth root ")
            }
        } else
            tvResult?.text = ""
    }
    
    fun xpowY(view: View) {
        changeColor(view, "#CDDC39")
        val length = tvResult?.length()
        if (checkIfNotValid()||tvResult?.text.toString()==Math.PI.toString()||tvResult?.text.toString()==Math.E.toString()) {
            tvResult?.text = ""
        } else if (tvResult?.text.toString() == "-") {
            tvResult?.text = "error"
        } else if (length != null && length > 0 && !tvResult?.text.toString().contains("^") && checkIfLastIsDigit(tvResult?.text.toString())) {
            tvResult?.append("^")
        }
    }
    
    private fun checkIfRoot(): Boolean {
        val stringToCheck = tvResult?.text.toString()
        return stringToCheck.contains(" nth root ")
    }
    
    private fun checkIfNotValid(): Boolean {
        val stringToCheck = tvResult?.text.toString()
        return stringToCheck.isEmpty() || stringToCheck.contains("null") || stringToCheck.contains("error") || stringToCheck.contains("NaN") || stringToCheck.contains(" nth root ") || stringToCheck.contains("Infinity") || stringToCheck.contains("-Infinity") || stringToCheck.contains("^") || stringToCheck.contains("log base")
    }
    
    fun unupeX(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result == null) {
                tvResult?.text = "error"
            } else {
                appendCharToFile("\n____________________\n1/(" + tvResult?.text.toString() + ")")
                val resultToScreen = 1 / result
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n=" + tvResult?.text.toString())
            }
        }
    }
    
    fun calculateAbsolute(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result == null) {
                tvResult?.text = "error"
            } else {
                appendCharToFile("\n____________________\n|" + tvResult?.text.toString() + "|")
                appendCharToFile("\n=|$result|")
                val resultToScreen = abs(result)
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n=$resultToScreen")
            }
        }
    }
    
    fun backSpaceAction(view: View) {
        if (view is Button){
            val length = tvResult?.length()
            if (length != null) {
                if (length > 0)
                    if (checkIfLastIsDigit(tvResult?.text.toString())) {
                        tvResult?.text = tvResult?.text?.subSequence(0, length - 1)
                    }
            }
        }
       
    }
    
    private fun checkIfLastIsDigit(stringToCheck: String): Boolean {
        val lastChar = stringToCheck.last()
        return lastChar.isDigit()
    }
    
    fun calculateSquareRoot(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result == null) {
                tvResult?.text = "error"
            } else {
                appendCharToFile("\n____________________\n" + "\u221A " + tvResult?.text.toString())
                val resultToScreen = sqrt(result)
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n=" + tvResult?.text.toString())
            }
        }
    }
    
    fun calculateCubeRoot(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result == null) {
                tvResult?.text = "error"
            } else {
                appendCharToFile("\n____________________\n" + "\u221B" + tvResult?.text.toString())
                val resultToScreen = cbrt(result)
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n=" + tvResult?.text.toString())
            }
        }
    }
    
    fun calculatePercent(view: View) {
        changeColor(view, "#CDDC39")
        if (checkIfNotValid()||!checkIfLastIsDigit(tvResult?.text.toString())) {
            tvResult?.text = ""
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result == null) {
                tvResult?.text = "error"
            } else {
                appendCharToFile("\n____________________\n(" + tvResult?.text.toString() + ")%")
                val resultToScreen = result / 100
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n=$result%")
                appendCharToFile("\n=$resultToScreen%")
                canAddEuler = false
                canAddPi = false
                canAddPlusSign = true
                canAddMultiplySign = true
                canAddDivisionSign = true
                canAddMinusSign=true
            }
        }
    }
    
    fun addEuler(view: View) {
        changeColor(view, "#CDDC39")
        val eulerNumber = Math.E
        if (tvResult?.text?.isEmpty() == true) {
            tvResult?.append(eulerNumber.toString())
            canAddEuler = false
        }
        if (canAddEuler) {
            tvResult?.append(eulerNumber.toString())
        }
        canAddPlusSign = true
        canAddMultiplySign = true
        canAddDivisionSign = true
        canAddPi = false
        canAddEuler = false
        
        
    }
    
    fun eulerLaX(view: View) {
        changeColor(view, "#CDDC39")
        val eulerNumber = Math.E
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            val length = tvResult?.length()
            if (length != null && length > 0) {
                val maybeDouble = calculateResults().toDoubleOrNull()
                if (maybeDouble==null){
                    tvResult?.text = "error"
                }else {
                    appendCharToFile("\n____________________\n" + eulerNumber.toString()+"^("+tvResult?.text.toString()+")" )
                    maybeDouble.let {
                        val resultat = eulerNumber.pow(maybeDouble)
                        tvResult?.text = resultat.toString()
                        appendCharToFile("\n=$eulerNumber^$maybeDouble\n=$resultat")
                    }
                }
            } else
                tvResult?.text = "error"
        }
    }
    
    fun addPiNumber(view: View) {
        changeColor(view, "#CDDC39")
        val piNumber = Math.PI
        if (tvResult?.text?.isEmpty() == true) {
            tvResult?.append(piNumber.toString())
            canAddPi = false
        }
        if (canAddPi) {
            tvResult?.append(piNumber.toString())
            
        }
        canAddPlusSign = true
        canAddMultiplySign = true
        canAddDivisionSign = true
        canAddPi = false
        canAddEuler = false
        canAddPoint = false
    }
    
    fun calculateFactorial(view: View) {
        changeColor(view, "#CDDC39")
        val stringPi = Math.PI.toString()
        val stringEuler = Math.E.toString()
        val string = tvResult?.text.toString()
        
        var factorial: BigInteger = BigInteger.ONE
        val bigDecFact: BigDecimal = factorial.toBigDecimal(0, MathContext(26, RoundingMode.HALF_EVEN))
        
        if (checkIfNotValid()) {
            tvResult?.text = ""
        } else {
            when {
                tvResult?.text.toString() == stringPi -> {
                    tvResult?.text = "7.18808272898"
                    appendCharToFile("\n____________________\n" + stringPi + " !"+"\n="+tvResult?.text.toString())
                }
                tvResult?.text.toString() == stringEuler -> {
                    tvResult?.text = "4.26082047636"
                    appendCharToFile("\n____________________\n" + stringEuler + " !"+"\n="+tvResult?.text.toString())
                }
                tvResult?.text.toString() == "-" -> {
                    tvResult?.text = "error"
                }
                else -> {
                    val result = calculateResultsForGivenString(string)
                    val doubleResult = result.toDoubleOrNull()
                    val intResult = doubleResult?.toInt()
                    if (doubleResult == null) {
                        tvResult?.text = ""
                    }
                    if (doubleResult != null) {
                        if (doubleResult < 0.0) {
                            tvResult?.text = "error"
                        } else {
                            for (i in 1..intResult!!) {
                                factorial = factorial.multiply(i.toBigInteger())
                            }
                            if (intResult < 30) {
                                appendCharToFile("\n____________________\n(" +tvResult?.text.toString()+")!")
                                tvResult?.text = factorial.toString()
                                appendCharToFile("\n=" + doubleResult + " !"+"\n="+tvResult?.text.toString())
                            } else {
                                appendCharToFile("\n____________________\n(" +tvResult?.text.toString()+")!")
                                tvResult?.text = bigDecFact.toString()
                                appendCharToFile("\n____________________\n" + doubleResult + " !"+"\n="+tvResult?.text.toString())
                                
                            }
                            
                        }
                    }
                    canAddPi = false
                    canAddEuler = false
                }
            }
        }
    }
    
    fun equalsAction(view: View) {
        changeColor(view, "#CDDC39")
        if (tvResult?.text.toString().isNotEmpty()) {
            if (checkIfLastIsDigit(tvResult?.text.toString())) {
                if (checkIfRoot() || checkIfPow() || checkifLog() || checkIfStrange()) {
                    if (checkIfStrange()) {
                        tvResult?.text = ""
                    }
                    if (checkIfRoot()) {
                        appendCharToFile("\n____________________\n" + tvResult?.text.toString() + "\n=\n")
                        val stringToShow = calculateNrootX(tvResult?.text.toString()).toString()
                        tvResult?.text = stringToShow
                        appendCharToFile("\n=" + tvResult?.text + "\n")
                    }
                    if (checkIfPow()) {
                        val stringToShow = calculateXpowY(tvResult?.text.toString()).toString()
                        tvResult?.text = stringToShow
                        appendCharToFile("\n= " + tvResult?.text.toString())
                    }
                    if (checkifLog()) {
                        tvResult?.text = calculateLogXbaseY(tvResult?.text.toString())
                    }
                    
                } else {
                    if (checkifValidNumber(tvResult?.text.toString())) {
                        tvResult?.text=""
                    } else {
                        appendCharToFile("\n____________________\n" + tvResult?.text.toString())
                        tvResult?.text = calculateResults()
                        appendCharToFile("\n=" + tvResult?.text)
                    }
                }
            }
        } else {
            tvResult?.text = ""
        }
    }
    
    private fun checkIfStrange(): Boolean {
        val stringToCheck = tvResult?.text.toString()
        return stringToCheck.contains("null") || stringToCheck.contains("error") || stringToCheck.contains("NaN") || stringToCheck.contains("Infinity") || stringToCheck.contains("-Infinity") || stringToCheck == "nth root" || stringToCheck == "log base"
    }
    
    private fun checkIfPow(): Boolean {
        val stringToCheck = tvResult?.text.toString()
        return stringToCheck.contains("^")
    }
    
    private fun checkifLog(): Boolean {
        val stringToCheck = tvResult?.text.toString()
        return stringToCheck.contains(" log base ")
    }
    
    private fun calculateNrootX(string: String): Double? {
        
        val delim = " nth root "
        val list = string.split(delim)
        val stringN = list[0]
        val resultN = calculateResultsForGivenString(stringN).toDoubleOrNull()
        val stringX = list[1]
        val resultX = calculateResultsForGivenString(stringX).toDoubleOrNull()
        val n = 1 / resultN!!
        val nRootX = resultX?.pow(n)
        appendCharToFile("$resultN \u221A$resultX")
        return nRootX
    }
    
    private fun calculateLogXbaseY(string: String): String {
        val delim = " log base "
        var xBaseY = 0.0
        val list = string.split(delim)
        val stringN = list[0]
        val resultN = calculateResultsForGivenString(stringN).toDoubleOrNull()
        val stringX = list[1]
        val resultX = calculateResultsForGivenString(stringX).toDoubleOrNull()
        if (resultN == null || resultX == null) {
            tvResult?.text = "error"
        } else {
            appendCharToFile("\n____________________\nlog($stringX)base ($stringN)")
            appendCharToFile("\n=log($resultX)base ($resultN)")
              xBaseY = resultX.let {
                kotlin.math.log(it, resultN)
            }
            appendCharToFile("\n= $xBaseY")
        }
    
        return xBaseY.toString()
    }
    
    private fun calculateXpowY(string: String): Double {
        val delim = "^"
        val list = string.split(delim)
        val stringX = list[0]
        val resultX = calculateResultsForGivenString(stringX).toDouble()
        val stringY = list[1]
        val resultY = calculateResultsForGivenString(stringY).toDouble()
        appendCharToFile("\n____________________\n($stringX) ^ ($stringY)")
        appendCharToFile("\n=($resultX) ^ ($resultY)")
        return resultY.let { resultX.pow(it) }
        
    }
    
    fun naturalLogE(view: View) {
        changeColor(view, "#CDDC39")
        if (tvResult?.text.toString()=="-"){
            tvResult?.text = "error"
        }
        if (checkIfNotValid()) {
            tvResult?.text = ""
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = false
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result==null){
                tvResult?.text=""
            }else {
                appendCharToFile("\n____________________\nlogₑ(" +tvResult?.text.toString()+")")
                appendCharToFile("\n=logₑ$result")
                val resultToScreen = kotlin.math.ln(result)
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n="+tvResult?.text.toString())
                canAddEuler = false
                canAddPi = false
                canAddPoint = false
            }
        }
        
    }
    
    fun dualLog2(view: View) {
        changeColor(view, "#CDDC39")
        if (tvResult?.text.toString()=="-"){
            tvResult?.text = "error"
        }
        if (checkIfNotValid()) {
            tvResult?.text = ""
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = false
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result==null){
                tvResult?.text=""
            }else {
                appendCharToFile("\n____________________\nlog₂(" +tvResult?.text.toString()+")")
                appendCharToFile("\n=log₂$result")
                val resultToScreen = kotlin.math.log2(result)
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n="+tvResult?.text.toString())
                canAddEuler = false
                canAddPi = false
                canAddPoint = false
            }
        }
    }
    
    fun zecimalLog10(view: View) {
        changeColor(view, "#CDDC39")
        if (tvResult?.text.toString()=="-"){
            tvResult?.text = "error"
        }
        if (checkIfNotValid()) {
            tvResult?.text = ""
            canAddPlusSign = false
            canAddMultiplySign = false
            canAddDivisionSign = false
            canAddMinusSign = false
        } else {
            val result = calculateResults().toDoubleOrNull()
            if (result==null){
                tvResult?.text=""
            }else {
                appendCharToFile("\n____________________\nlog₁₀(" +tvResult?.text.toString()+")")
                appendCharToFile("\n=log₁₀$result")
                val resultToScreen = log10(result)
                tvResult?.text = resultToScreen.toString()
                appendCharToFile("\n="+tvResult?.text.toString())
                canAddEuler = false
                canAddPi = false
                canAddPoint = false
            }
        }
    }
    
    fun logXbaseY(view: View) {
        changeColor(view, "#CDDC39")
        val length = tvResult?.length()
        if (checkIfNotValid()) {
            tvResult?.text = ""
        }
        if (tvResult?.text.toString()=="-"){
            tvResult?.text = "error"
        }
        if (length != null && length > 0) {
            tvResult?.append(" log base ")
        } else
            tvResult?.text = ""
    }
    
    private fun returnProcessedStringAsList(stringToProcess: String): MutableList<Char> {
        val listFromScreen = mutableListOf<Char>()
        stringToProcess.forEach { character ->
            listFromScreen.add(character)
        }
        if (!listFromScreen.isNullOrEmpty()) {
            for (i in 0 until listFromScreen.size) {
                
                if (listFromScreen.last() == '+' || listFromScreen.last() == '/' || listFromScreen.last() == '*' || listFromScreen.last() == '-') {
                    listFromScreen.removeLast()
                }
            }
        }
        return listFromScreen
    }
    
    @Suppress("UNCHECKED_CAST")
    private fun calculateResults(): String {
        val listToUse = transformInNegatives(transformInDouble(getTextFromScreen()))
        if (listToUse.isEmpty()) return ""
        val timesAndDivision: MutableList<Any> = timesAndDivCalc(listToUse) as MutableList<Any>
        if (timesAndDivision.isEmpty()) return ""
        return addSubtractCalculator(timesAndDivision).toString()
    }
    @Suppress("UNCHECKED_CAST")
    private fun calculateResultsForGivenString(stringToUse: String): String {
        val listToUse = transformInNegatives(transformInDouble(returnProcessedStringAsList(stringToUse)))
        if (listToUse.isEmpty()) return ""
        val timesAndDivision: MutableList<Any> = timesAndDivCalc(listToUse) as MutableList<Any>
        if (timesAndDivision.isEmpty()) return ""
        return addSubtractCalculator(timesAndDivision).toString()
    }
    
    private fun addSubtractCalculator(passedList: MutableList<Any>): Double {
        
        var result = 0.00000
        
        if (!passedList.isNullOrEmpty()) {
            result = passedList[0] as Double
            for (i in passedList.indices) {
                if (passedList.getOrNull(0) != null) {
                    if (passedList[i] is Char && i != passedList.lastIndex) {
                        val operator = passedList[i]
                        val nextDigit = passedList[i + 1] as Double
                        if (operator == '+')
                            result += nextDigit
                        if (operator == '-')
                            result -= nextDigit
                    }
                }
            }
        }
        return result
    }
    
    private fun timesAndDivCalc(passedList: MutableList<Any>): Any {
        var list = passedList
        while (list.contains('*') || list.contains('/')) {
            list = calculateMultiplyAndDivision(list)
        }
        return list
    }
    
    private fun appendCharToFile(charToAppend: String) {
        try {
            val fileOutputStream: FileOutputStream = openFileOutput("mytextfile.txt", Context.MODE_APPEND)
            val outputWriter = OutputStreamWriter(fileOutputStream)
            outputWriter.append(charToAppend)
            outputWriter.close()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
    }
    
    private fun deleteFromFile() {
        try {
            val fileOutputStream: FileOutputStream = openFileOutput("mytextfile.txt", Context.MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileOutputStream)
            
            Toast.makeText(baseContext, "history cleared", Toast.LENGTH_SHORT).show()
            outputWriter.write("")
            outputWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
    }
    
    private fun readTextFile(fileName: String): String {
        var string = ""
        val fileInputStream: FileInputStream? = openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String?
        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            stringBuilder.append(text + "\n")
            string = stringBuilder.toString()
        }
        return string
    }
    
    private fun getTextFromScreen(): MutableList<Char> {
        val listFromScreen = mutableListOf<Char>()
        tvResult?.text?.forEach { character ->
            listFromScreen.add(character)
        }
        if (!listFromScreen.isNullOrEmpty()) {
            for (i in 0 until listFromScreen.size) {
                
                if (listFromScreen.last() == '+' || listFromScreen.last() == '/' || listFromScreen.last() == '*' || listFromScreen.last() == '-') {
                    listFromScreen.removeLast()
                }
            }
        }
        
        return listFromScreen
    }
    
    private fun transformInDoubleWithE(passedList: MutableList<Char>): MutableList<Any> {
        var stringToConvert = ""
        passedList.forEach { character ->
            stringToConvert += character
        }
        
        var digitDouble = ""
        var newString = ""
        var newStringLast = ""
        val stringSize = stringToConvert.length
        val listWithDoubles = mutableListOf<Any>()
        stringToConvert.forEachIndexed { _, character ->
            if (character == 'E') {
                newString = stringToConvert.substring(0, stringToConvert.indexOf('E') + 2)
                newStringLast = stringToConvert.substring(stringToConvert.indexOf('E') + 2, stringSize)
            }
        }
  
        val listaDeProcesatCuE = newString.toMutableList()
        if (listaDeProcesatCuE[0] == '-' && listaDeProcesatCuE[1] == '-') {
            listWithDoubles.add(listaDeProcesatCuE[0])
            listaDeProcesatCuE.removeFirst()
            newString = ""
            listaDeProcesatCuE.forEach { character ->
                newString += character
            }
            
        } else {
            newString = ""
            listaDeProcesatCuE.forEach { character ->
                newString += character
            }
        }
        
        val listaDeProcesat = newStringLast.toMutableList()
   
        for (i in 0 until listaDeProcesat.size) {
            if (listaDeProcesat[0].isDigit()) {
                newString += listaDeProcesat[0].toString()
                listaDeProcesat.removeAt(0)
            }
        }
        
        listWithDoubles.add(newString.toDouble())
        
        listaDeProcesat.forEach { character ->
            if (character.isDigit() || character == '.' || character == 'E') {
                digitDouble += character
            } else {
                digitDouble.toDoubleOrNull()?.let { listWithDoubles.add(it) }
                digitDouble = ""
                listWithDoubles.add(character)
            }
        }
        if (digitDouble != "") {
            listWithDoubles.add(digitDouble.toDouble())
        }

        return listWithDoubles
        
    }
    
    private fun transformInDouble(passedList: MutableList<Char>): MutableList<Any> {
        var stringForE = ""
        passedList.forEach { character ->
            stringForE += character
        }
        var listWithDoubles = mutableListOf<Any>()
        var digitDouble = ""
        if (stringForE.contains("E-")) {
            listWithDoubles = transformInDoubleWithE(passedList)
            
        } else {
            passedList.forEach { character ->
                if (character.isDigit() || character == '.' || character == 'E') {
                    digitDouble += character
                  
                } else {
                    digitDouble.toDoubleOrNull()?.let { listWithDoubles.add(it) }
                    digitDouble = ""
                    listWithDoubles.add(character)
                }
            }
            if (digitDouble != "") {
                listWithDoubles.add(digitDouble.toDouble())
            }
           
            
        }
        return listWithDoubles
    }
    
    private fun transformInNegatives(passedList: MutableList<Any>): MutableList<Any> {
        val negativeListFinal = mutableListOf<Any>()
        var negativeNumber: String
        var character: Char
        if (!passedList.isNullOrEmpty()) {
            
            if (passedList.size > 1 && passedList[0] == '-') {
                negativeNumber = passedList[0].toString() + passedList[1].toString()
                negativeListFinal.add(negativeNumber.toDouble())
                passedList.subList(0, 2).clear()
            }
        }
        for (i in 0 until passedList.size) {
            
            if (passedList.isNullOrEmpty()) {
                return negativeListFinal
            }
            if (passedList[0] is Char && passedList[1] == '-' && passedList[2] !is Char) {
                character = passedList[0] as Char
                negativeNumber = passedList[1].toString() + passedList[2].toString()
                negativeListFinal.add(character)
                negativeListFinal.add(negativeNumber.toDouble())
                passedList.subList(0, 3).clear()
            } else {
                negativeListFinal.add(passedList[0])
                passedList.subList(0, 1).clear()
            }
            
        }
        return negativeListFinal
    }
    
    private fun calculateMultiplyAndDivision(passedList: MutableList<Any>): MutableList<Any> {
        val listWithMultyply = mutableListOf<Any>()
        var restartIndex = passedList.size
        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Double
                val nextDigit = passedList[i + 1] as Double
                when (operator) {
                    '*' -> {
                        listWithMultyply.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        listWithMultyply.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        listWithMultyply.add(prevDigit)
                        listWithMultyply.add(operator)
                    }
                }
            }
            if (i > restartIndex)
                listWithMultyply.add(passedList[i])
        }
        return listWithMultyply
    }
    
}



