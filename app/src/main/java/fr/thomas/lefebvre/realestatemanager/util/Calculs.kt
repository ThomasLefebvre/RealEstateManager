@file:Suppress("DEPRECATION")

package fr.thomas.lefebvre.realestatemanager.util

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.material.textfield.TextInputEditText


fun calculLoan(amout:Double,term:Double,bring:Double,interest:Double):List<String> {
    //init variables for the calcul

    val listCalcul=ArrayList<String>()

    val interestPourcent = interest / 100

    val up = ((amout - bring) * (interestPourcent / 12.0))
    val downPow=Math.pow((1 + interestPourcent / 12),-(12*term))
    val down= 1 - downPow
    val paymentMensuality=(up/down).toInt()
    val totalCostInt=(paymentMensuality*(term*12)).toInt()
    val totalInterest=totalCostInt-amout.toInt()

    listCalcul.add(paymentMensuality.toString())
    listCalcul.add(totalCostInt.toString())
    listCalcul.add(totalInterest.toString())

    return listCalcul


}

 fun initMaxQuery(editable: TextInputEditText, maxValue:Long):Long{
    val max:Long
    if(editable.text.toString()!=""&&editable.text.toString()<= Long.MAX_VALUE.toString()){
        max=editable.text.toString().toLong()
    }
    else max=maxValue

    return max
}

fun initMinQuery(editable: TextInputEditText):Long{
    val min:Long
    if(editable.text.toString()!=""){
        min=editable.text.toString().toLong()
    }
    else min=0

    return min
}




fun isInternetAvailableCorrection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork=connectivityManager.activeNetworkInfo
    return activeNetwork!=null
}