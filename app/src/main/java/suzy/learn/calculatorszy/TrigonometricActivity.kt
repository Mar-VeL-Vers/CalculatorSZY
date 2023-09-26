package suzy.learn.calculatorszy

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
@SuppressLint("UseSwitchCompatOrMaterialCode")


class TrigonometricActivity : AppCompatActivity() {
    private var menuClose: MenuItem? = null
    private var tvResultInput: TextView? = null
    private var tvResultOutput: TextView? = null
    private var tvDegrees: TextView? = null
    private var tvRadians: TextView? = null
    private var switchButton: Switch? = null
    private var canAddPoint = false
    private var contor = 0
    private var canAddPi = true
    private var canAddDigit = true
    private var canAddMinus = true
    private var switchOnDegrees = true
    private var switchOnRadians = false
    private var stringToRetain = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trigonometric)

        tvResultInput = findViewById(R.id.tvResultInput)
        tvResultOutput = findViewById(R.id.tvResultOutput)
        tvResultOutput?.movementMethod = ScrollingMovementMethod()
        tvDegrees = findViewById(R.id.tvDegrees)
        tvRadians = findViewById(R.id.tvRadians)
        switchButton = findViewById(R.id.degreeOrRadians)
        menuClose = findViewById(R.id.CloseApp)
        switchButton?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvRadians?.setTextColor(Color.parseColor("#CDDC39"))
                tvDegrees?.setTextColor(Color.parseColor("#FFFFFFFF"))
                switchOnRadians = true
                switchOnDegrees = false
                makeConvToRadians(tvResultInput?.text.toString())

            } else {
                tvDegrees?.setTextColor(Color.parseColor("#CDDC39"))
                tvRadians?.setTextColor(Color.parseColor("#FFFFFFFF"))
                switchOnDegrees = true
                switchOnRadians = false
                makeConvToDegrees(tvResultInput?.text.toString())
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trig_menu, menu)
        
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        
        R.id.CloseApp->{
         onBackPressed()
            true
        }
        
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun addDigit(view: View) {
        if (view is Button && canAddDigit) {
            tvResultInput?.append(view.text)
            canAddPoint = true
            canAddPi = false
            canAddMinus = false


        }

    }

    fun onClear2(view: View) {
        if (view is Button) {
            tvResultInput?.text = ""
            tvResultOutput?.text = ""
            canAddPoint = false
            contor = 0
            canAddPi = true
            canAddDigit = true
            canAddMinus = true

        }

    }

    fun backSpaceAction2(view: View) {
        if (view is Button){
            val length = tvResultInput?.length()
            if (length != null) {
                if (length > 0)
                    tvResultInput?.text = tvResultInput?.text?.subSequence(0, length - 1)
            }
        }
    }

    fun addPoint2(view: View) {
        if (view is Button && canAddPoint && contor == 0) {
            tvResultInput?.append(view.text)
            contor += 1
            canAddPi = false
            canAddDigit = true
            canAddMinus = false
        }

    }

    fun addMinus2(view: View) {
        if (view is Button && canAddMinus) {
            tvResultInput?.append(view.text)
            canAddDigit = true
            canAddPoint = false
            canAddPi = true
            canAddMinus = false
        }
    }

    fun addPi2(view: View) {
        if (view is Button && canAddPi) {
            tvResultInput?.append(Math.PI.toString())
            canAddDigit = false
            canAddPoint = false
            canAddMinus = false
            canAddPi=false
        }

    }

    fun addTrigFunction(view: View) {
        if (view is Button) {
            tvResultInput?.text = view.text
            tvResultInput?.append(" ")
            canAddPi = true
            canAddDigit = true
            canAddMinus = true
            contor = 0
            tvResultOutput?.text = ""
            stringToRetain = view.text.toString()

        }
    }

    
    @SuppressLint("SetTextI18n")
    fun onEqualsAction(view: View) {
        if (view is Button) {
            val string = tvResultInput?.text.toString()
            if (string.isEmpty() || string == "$stringToRetain ") {
                tvResultOutput?.text = "Invalid input"
            } else {
                if (switchOnDegrees && (!string.contains(" radians") && !string.contains(" degrees"))) {
                    tvResultInput?.append(" degrees")
                    canAddDigit = false
                    if (!containsArc(tvResultInput?.text.toString()) && !containsAllTrig(tvResultInput?.text.toString())) {
                        tvResultOutput?.text = tvResultInput?.text
                    } else when {
                        containsArc(tvResultInput?.text.toString())       -> {
                            tvResultOutput?.text = calculateAsinDegrees(string)
                            tvResultOutput?.append(" degrees")
                        }
                        containsHyperboic(tvResultInput?.text.toString()) -> {
                            tvResultOutput?.text = calculateHyperDegreesWithDouble(string).toString()
                            tvResultOutput?.append(" degrees")
                        }
                        else                                              -> {
                            if ((string.contains("cot") || string.contains("csc")) && (transformInDouble(string) == 0.0 || transformInDouble(string) == 180.00 || transformInDouble(string) == 360.00)) {
                                tvResultOutput?.text = "undefined"
                            } else {
                                tvResultOutput?.text = calculateDegrees().toString()
                                tvResultOutput?.append(" degrees")
                            }
                        }
                    }
                }
                if (switchOnRadians && (!string.contains(" radians") && !string.contains(" degrees"))) {
                    tvResultInput?.append(" radians")
                    canAddDigit = false
                    if (!containsArc(tvResultInput?.text.toString()) && !containsAllTrig(tvResultInput?.text.toString())) {
                        tvResultOutput?.text = tvResultInput?.text
                    } else when {
                        containsArc(tvResultInput?.text.toString()) -> {
                            tvResultOutput?.text = calculateAsinRadians(string)
                            tvResultOutput?.append(" radians")
                        }
                        else                                        -> {
                            tvResultOutput?.text = calculateAllRadians().toString()
                            tvResultOutput?.append(" radians")
                        }
                    }
                }
            }
        }
    }

    private fun transformInDouble(stringToTransform: String): Double {
        val result: Double
        var digitDouble = ""
        val newString: String
        when {
            stringToTransform.contains("radians") -> {
                newString = stringToTransform.substring(0, stringToTransform.indexOf("radians"))
                newString.forEach { character ->
                    if (character.isDigit() || character == '.' || character == '-') {
                        digitDouble += character
                    }
                }
            }
            stringToTransform.contains("degrees") -> {
                newString = stringToTransform.substring(0, stringToTransform.indexOf("degrees"))
                newString.forEach { character ->
                    if (character.isDigit() || character == '.' || character == '-') {
                        digitDouble += character
                    }
                }
            }
            else -> {
                stringToTransform.forEach { character ->
                    if (character.isDigit() || character == '.' || character == '-') {
                        digitDouble += character
                    }
                }
            }
        }

        return if (digitDouble == "") {

            0.0

        } else {
            result = digitDouble.toDoubleOrNull()!!
            result
        }

    }

    private fun calculateDegrees(): Double {
        val stringToCalculate = tvResultInput?.text.toString()
        val doubleDegrees = transformInDouble(stringToCalculate)
        val doubleRad: Double = doubleDegrees * Math.PI / 180.00
        val result: Double = when (stringToRetain) {
            "sin" -> {
                sin(doubleRad)
            }
            "cos" -> {
                cos(doubleRad)
            }
            "tan" -> {
                tan(doubleRad)
            }
            "sec" -> {
                1 / cos(doubleRad)
            }
            "csc" -> {
                1 / sin(doubleRad)
            }
            "cot" -> {
                1 / tan(doubleRad)
            }
            else -> {
                0.0
            }
        }
        return result
    }

    private fun calculateALLDegrees(): Double {
        val stringToCalculate = tvResultInput?.text.toString()
        val doubleDegrees = transformInDouble(stringToCalculate)
        val doubleRad: Double = doubleDegrees * Math.PI / 180.00
        val result: Double = when (stringToRetain) {
            "sin" -> {
                sin(doubleRad)
            }
            "cos" -> {
                cos(doubleRad)
            }
            "tan" -> {
                tan(doubleRad)
            }
            "sec" -> {
                1 / cos(doubleRad)
            }
            "csc" -> {
                1 / sin(doubleRad)
            }
            "cot" -> {
                1 / tan(doubleRad)
            }
            else -> {
                calculateHyperDegreesWithDouble(stringToCalculate)
            }
        }
        return result
    }

    private fun convertToRadians(degreesToTransform: Double): Double {
        return degreesToTransform / 180 * Math.PI
    }

    private fun convertToDegrees(radiansToTransform: Double): Double {
        return Math.toDegrees(radiansToTransform)

    }

    private fun containsDigits(stringToCheck: String): Boolean {
        var flag = false
        stringToCheck.forEach { character ->
            if (character.isDigit()) {
                flag = true
            }
        }

        return flag
    }

    @SuppressLint("SetTextI18n")
    fun makeConvToRadians(stringToCheck: String) {
        if (stringToCheck.isNotEmpty()) {
            if (containsDigits(stringToCheck)) {
                //1 if contains only digit , no trig and no arc
                if (!containsAllTrig(stringToCheck) && !containsArc(stringToCheck)) {
                    //contains digit, no radians and no degrees
                    if (!stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultInput?.append(" degrees")
                        tvResultOutput?.text = convertToRadians(transformInDouble(stringToCheck)).toString()
                        tvResultOutput?.append(" radians")

                    }
                    //contains digit and degrees, no radians
                     if (stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultOutput?.text = convertToRadians(transformInDouble(stringToCheck)).toString()
                        tvResultOutput?.append(" radians")
                    }
                    //contains digit and radians
                    if (stringToCheck.contains(" radians")) {
                        tvResultOutput?.text = tvResultInput?.text
                    }
                }
                //2 contains digit and trig, no arc
                if ( containsAllTrig(stringToCheck) &&!containsArc(stringToCheck)) {
                    // 2.1contains only trig and digit, no degrees or radians
                    if (!stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultInput?.append(" degrees")
                        tvResultOutput?.text = calculateAllDegreesToRadians().toString()
                        tvResultOutput?.append(" radians")

                    }
                    //2.2contains trig digit and degrees, no radians
                     if (stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        if ((stringToCheck.contains("cot") || stringToCheck.contains("csc")) && (transformInDouble(stringToCheck) == 0.0 || transformInDouble(stringToCheck) == 180.00 || transformInDouble(stringToCheck) == 360.00)) {
                            tvResultOutput?.text = "undefined"
                        } else
                            tvResultOutput?.text = calculateAllDegreesToRadians().toString()
                        tvResultOutput?.append(" radians")
                    }
                    //2.3contains trig digit and radians
                    if (stringToCheck.contains(" radians")) {
                        if ((stringToCheck.contains("cot") || stringToCheck.contains("csc")) && (transformInDouble(stringToCheck) == 0.0 || transformInDouble(stringToCheck) == 180.00 || transformInDouble(stringToCheck) == 360.00)) {
                            tvResultOutput?.text = "undefined"
                        } else
                            tvResultOutput?.text = calculateAllRadians().toString()
                        tvResultOutput?.append(" radians")
                    }
                }
                //3 contains Digit and Arc, no trig
                if ( containsArc(stringToCheck)) {
                    // 2.1contains only tArcand digit, no degrees or radians
                    if (!stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultInput?.append(" degrees")
                        tvResultOutput?.text = calculateAsinRadians(stringToCheck)
                        tvResultOutput?.append(" radians")

                    }
                    //2.2contains ARc digit and degrees, no radians
                   if (stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultOutput?.text = calculateAsinRadians(stringToCheck)
                        tvResultOutput?.append(" radians")
                    }
                    //2.3contains trig digit and radians
                    if (stringToCheck.contains(" radians")) {
                        tvResultOutput?.text = calculateAsinRadians(stringToCheck)
                        tvResultOutput?.append(" radians")
                    }
                }
            } else {
                tvResultOutput?.text = "invalid input"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun makeConvToDegrees(stringToCheck: String) {
        if (stringToCheck.isNotEmpty()) {
            if (containsDigits(stringToCheck)) {
                //1 if contains only digit , no trig and no arc
                if (!containsAllTrig(stringToCheck) && !containsArc(stringToCheck)) {
                    //contains digit, no radians and no degrees
                    if (!stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultInput?.append(" radians")
                        tvResultOutput?.text = convertToDegrees(transformInDouble(stringToCheck)).toString()
                        tvResultOutput?.append(" degrees")

                    }
                    //contains digit and radians, no degrees
                     if (stringToCheck.contains(" radians") && !stringToCheck.contains(" degrees")) {
                        tvResultOutput?.text = convertToDegrees(transformInDouble(stringToCheck)).toString()
                        tvResultOutput?.append(" degrees")
                    }
                    //contains digit and degrees
                    if (stringToCheck.contains(" degrees")) {
                        tvResultOutput?.text = tvResultInput?.text
                    }
                }
                //2 contains digit and trig, no arc
                if (containsAllTrig(stringToCheck) && !containsArc(stringToCheck)) {
                    if ((stringToCheck.contains("cot") || stringToCheck.contains("csc")) && (transformInDouble(stringToCheck) == 0.0 || transformInDouble(stringToCheck) == 180.00 || transformInDouble(stringToCheck) == 360.00)) {
                        tvResultOutput?.text = "undefined"
                    }
                    // 2.1contains only trig and digit, no degrees or radians
                    if (!stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultInput?.append(" radians")
                        tvResultOutput?.text = convertToDegrees(calculateAllRadians()).toString()
                        tvResultOutput?.append(" degrees")

                    }
                    //2.2contains trig digit and degrees, no radians
                    if (stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        if ((stringToCheck.contains("cot") || stringToCheck.contains("csc")) && (transformInDouble(stringToCheck) == 0.0 || transformInDouble(stringToCheck) == 180.00 || transformInDouble(stringToCheck) == 360.00)) {
                            tvResultOutput?.text = "undefined"
                        } else
                            tvResultOutput?.text = calculateALLDegrees().toString()
                        tvResultOutput?.append(" degrees")
                    }
                    //2.3contains trig digit and radians
                 
                    if (stringToCheck.contains(" radians")) {
                        if ((stringToCheck.contains("cot") || stringToCheck.contains("csc")) && (transformInDouble(stringToCheck) == 0.0 || transformInDouble(stringToCheck) == 180.00 || transformInDouble(stringToCheck) == 360.00)) {
                            tvResultOutput?.text = "undefined"
                        } else
                            tvResultOutput?.text = convertToDegrees(calculateAllRadians()).toString()
                        tvResultOutput?.append(" degrees")
                    }
                }
                //3 contains Digit and Arc, no trig
                if ( containsArc(stringToCheck)) {
                    // 2.1contains only trig and digit, no degrees or radians
                    if (!stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultInput?.append(" radians")
                        tvResultOutput?.text=calculateAsinDegrees(stringToCheck)
                        tvResultOutput?.append(" degrees")

                    }
                    //2.2contains trig digit and degrees, no radians
                    if (stringToCheck.contains(" degrees") && !stringToCheck.contains(" radians")) {
                        tvResultOutput?.text = calculateAsinDegrees(stringToCheck)
                        tvResultOutput?.append(" degrees")
                    }
                    //2.3contains trig digit and radians
                    if (stringToCheck.contains(" radians")) {
                        tvResultOutput?.text = convertToDegrees(transformInDouble(calculateAsinRadians(stringToCheck))).toString()
                        tvResultOutput?.append(" degrees")
                    }
                }
            } else {
                tvResultOutput?.text = "invalid input"
            }
        }
    }

    private fun containsAllTrig(stringToCheck: String): Boolean {
        return stringToCheck.contains("sin") ||
                stringToCheck.contains("cos") ||
                stringToCheck.contains("tan") ||
                stringToCheck.contains("sec") ||
                stringToCheck.contains("csc") ||
                stringToCheck.contains("cot") ||
                stringToCheck.contains("sinh") ||
                stringToCheck.contains("cosh") ||
                stringToCheck.contains("tanh") ||
                stringToCheck.contains("sech")
    }
  
    private fun calculateAsinDegrees(stringToCheck: String): String {
        var stringToReturn = ""
        val transformedInDouble = transformInDouble(stringToCheck)
        var result: Double
        var resultInDegrees: Double
        if (stringToCheck.contains("arcsin")) {
            if (transformedInDouble < -1 || transformedInDouble > 1) {
                stringToReturn = "NaN"
            } else {
                result = asin(transformedInDouble)
                resultInDegrees = convertToDegrees(result)
                stringToReturn = resultInDegrees.toString()
            }
        }

        if (stringToCheck.contains("arccos")) {
            if (transformedInDouble < -1 || transformedInDouble > 1) {
                stringToReturn = "NaN"
            } else {
                result = acos(transformedInDouble)
                resultInDegrees = convertToDegrees(result)
                stringToReturn = resultInDegrees.toString()
            }
        }

        if (stringToCheck.contains("arctan")) {
            result = atan(transformedInDouble)
            resultInDegrees = convertToDegrees(result)
            stringToReturn = resultInDegrees.toString()
        }

        if (stringToCheck.contains("arccot")) {
            result = atan(1 / transformedInDouble)
            resultInDegrees = convertToDegrees(result)
            stringToReturn = resultInDegrees.toString()
        }

        if (stringToCheck.contains("arcsec")) {
            if (transformedInDouble <= -1 || transformedInDouble >= 1) {
                result = acos(1 / transformedInDouble)
                resultInDegrees = convertToDegrees(result)
                stringToReturn = resultInDegrees.toString()
            } else {
                stringToReturn = "NaN"
            }
        }
        if (stringToCheck.contains("arccsc")) {
            if (transformedInDouble <= -1 || transformedInDouble >= 1) {
                result = PI / 2 - (atan(sqrt(transformedInDouble.pow(2.0) - 1)))
                resultInDegrees = convertToDegrees(result)
                stringToReturn = resultInDegrees.toString()
            } else {
                stringToReturn = "NaN"
            }
        }

        return stringToReturn
    }
    
    private fun calculateAsinRadians(stringToCheck: String): String {
        var stringToReturn = ""
        val transformedInDouble = transformInDouble(stringToCheck)
        var result: Double

        if (stringToCheck.contains("arcsin")) {
            if (transformedInDouble < -1 || transformedInDouble > 1) {
                stringToReturn = "NaN"
            } else {
                result = asin(transformedInDouble)
                stringToReturn = result.toString()

            }
        }

        if (stringToCheck.contains("arccos")) {
            if (transformedInDouble < -1 || transformedInDouble > 1) {
                stringToReturn = "NaN"
            } else {
                result = acos(transformedInDouble)
                stringToReturn = result.toString()
            }
        }

        if (stringToCheck.contains("arctan")) {
            result = atan(transformedInDouble)
            stringToReturn = result.toString()
        }

        if (stringToCheck.contains("arccot")) {
            result = atan(1 / transformedInDouble)
            stringToReturn = result.toString()
        }

        if (stringToCheck.contains("arcsec")) {
            when {
                transformedInDouble >= 1  -> {
                    result = atan(sqrt(transformedInDouble.pow(2.0) - 1))
                    stringToReturn = result.toString()
                }
                transformedInDouble <= -1 -> {
                    result = Math.PI - (atan(sqrt(transformedInDouble.pow(2.0) - 1)))
                    stringToReturn = result.toString()
                }
                else                      -> {
                    stringToReturn = "NaN"
                }
            }
        }
        if (stringToCheck.contains("arccsc")) {
            if (transformedInDouble <= -1 || transformedInDouble >= 1) {
                result = PI / 2 - (atan(sqrt(transformedInDouble.pow(2.0) - 1)))
                stringToReturn = result.toString()
            } else {
                stringToReturn = "NaN"
            }
        }
        return stringToReturn
    }

    private fun calculateHyperDegreesWithDouble(stringToCheck: String): Double {
        var resultInDegrees = 0.0
        val transformedInDouble = transformInDouble(stringToCheck)
        val transformedInRadians = convertToRadians(transformedInDouble)
        var result: Double

        if (stringToCheck.contains("sinh")) {
            result = (Math.E.pow(transformedInRadians) - Math.E.pow(-transformedInRadians)) / 2
            resultInDegrees = convertToDegrees(result)
        }
        if (stringToCheck.contains("cosh")) {
            result = (Math.E.pow(transformedInRadians) + Math.E.pow(-transformedInRadians)) / 2
            resultInDegrees = convertToDegrees(result)
        }
        if (stringToCheck.contains("tanh")) {
            result = ((Math.E.pow(transformedInRadians) - Math.E.pow(-transformedInRadians)) / 2) /
                    ((Math.E.pow(transformedInRadians) + Math.E.pow(-transformedInRadians)) / 2)
            resultInDegrees = convertToDegrees(result)
        }
        if (stringToCheck.contains("sech")) {
            result = ((1 / ((Math.E.pow(transformedInRadians) + Math.E.pow(-transformedInRadians)) / 2)))
            resultInDegrees = convertToDegrees(result)
        }
        return resultInDegrees
    }
    
    private fun calculateAllDegreesToRadians(): Double {
        val stringToCalculate = tvResultInput?.text.toString()
        val doubleDegrees = transformInDouble(stringToCalculate)
        val doubleRad: Double = doubleDegrees * Math.PI / 180.00
        val result: Double = when (stringToRetain) {
            "sin" -> {
                convertToRadians(sin(doubleRad))
            }
            "cos" -> {
                convertToRadians(cos(doubleRad))
            }
            "tan" -> {
                convertToRadians(tan(doubleRad))
            }
            "sec" -> {
                convertToRadians(1 / cos(doubleRad))
            }
            "csc" -> {
                convertToRadians(1 / sin(doubleRad))
            }
            "cot" -> {
                convertToRadians(1 / tan(doubleRad))
            }
            "sinh" -> {
                ((Math.E.pow(doubleRad)) - (Math.E.pow(-doubleRad))) / 2
            }
            "cosh" -> {
                (Math.E.pow(doubleRad) + Math.E.pow(-doubleRad)) / 2
            }
            "tanh" -> {
                ((Math.E.pow(doubleRad) - Math.E.pow(-doubleRad)) / 2) /
                        ((Math.E.pow(doubleRad) + Math.E.pow(-doubleRad)) / 2)
            }
            "sech" -> {
                (2 / (Math.E.pow(doubleRad) + Math.E.pow(-doubleRad)))
            }

            else -> {
                0.0
            }
        }
        return result

    }
    
    private fun calculateAllRadians(): Double {
        val stringToCalculate = tvResultInput?.text.toString()
        val doubleDegrees = transformInDouble(stringToCalculate)
        val result: Double = when (stringToRetain) {
            "sin" -> {
                sin(doubleDegrees)
            }
            "cos" -> {
                cos(doubleDegrees)
            }
            "tan" -> {
                tan(doubleDegrees)
            }
            "sec" -> {
                1 / cos(doubleDegrees)
            }
            "csc" -> {
                1 / sin(doubleDegrees)
            }
            "cot" -> {
                1 / tan(doubleDegrees)
            }
            "sinh" -> {
                (Math.E.pow(doubleDegrees) - Math.E.pow(-doubleDegrees)) / 2
            }
            "cosh" -> {
                (Math.E.pow(doubleDegrees) + Math.E.pow(-doubleDegrees)) / 2
            }
            "tanh" -> {
                ((Math.E.pow(doubleDegrees) - Math.E.pow(-doubleDegrees)) / 2) /
                        ((Math.E.pow(doubleDegrees) + Math.E.pow(-doubleDegrees)) / 2)
            }
            "sech" -> {
                ((1 / ((Math.E.pow(doubleDegrees) + Math.E.pow(-doubleDegrees)) / 2)))

            }
            else -> {
                0.0
            }
        }

        return result
    }

    private fun containsArc(stringToCheck: String): Boolean {

        return stringToCheck.contains("arcsin") || stringToCheck.contains("arccos") ||
                stringToCheck.contains("arctan") || stringToCheck.contains("arccot") ||
                stringToCheck.contains("arcsec") || stringToCheck.contains("arccsc")
    }

    private fun containsHyperboic(stringToCheck: String): Boolean {
        return stringToCheck.contains("sinh") ||
                stringToCheck.contains("cosh") ||
                stringToCheck.contains("tanh") ||
                stringToCheck.contains("sech")
    }

}