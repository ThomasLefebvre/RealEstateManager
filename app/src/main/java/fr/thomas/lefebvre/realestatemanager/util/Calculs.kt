package fr.thomas.lefebvre.realestatemanager.util


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