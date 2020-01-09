

package fr.thomas.lefebvre.realestatemanager.util



fun calculLoan(amout: Double, term: Double, bring: Double, interest: Double): List<String> {
    //init variables for the calcul

    val listCalcul = ArrayList<String>()
    val interestPourcent = interest / 100
    val monthOfYear: Double = 12.0

    val up = ((amout - bring) * (interestPourcent / monthOfYear))
    val downPow = Math.pow((1 + interestPourcent / monthOfYear), -(monthOfYear * term))
    val down = 1 - downPow
    val paymentMensuality = (up / down).toInt()
    val totalCostInt = (paymentMensuality * (term * monthOfYear)).toInt()
    val totalInterest = totalCostInt - (amout-bring).toInt()

    listCalcul.add(paymentMensuality.toString())
    listCalcul.add(totalCostInt.toString())
    listCalcul.add(totalInterest.toString())

    return listCalcul


}

fun initMaxQuery(text: String, maxValue: Long): Long {
    val max: Long
    if (text == "") {
        max = maxValue
    } else if (text.toLong() <= maxValue) {
        max = text.toLong()
    } else {
        max=maxValue
    }

    return max
}

fun initMinQuery(text: String): Long {
    val min: Long
    if (text != "") {
        min = text.toLong()
    } else min = 0

    return min
}





