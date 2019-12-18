package fr.thomas.lefebvre.realestatemanager.util


import fr.thomas.lefebvre.realestatemanager.database.Agent
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun formatListAgentToListString(agents: List<Agent>): List<String> {
    val listNameAgent = mutableListOf<String>()
    agents.forEach {
        listNameAgent.add(it.name)
    }
    return listNameAgent
}


val EMAIL_PATTERN =
    "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"

val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
lateinit var matcher: Matcher

fun validateEmail(email: String): Boolean {
    matcher = pattern.matcher(email)
    return matcher.matches()
}

fun formatSurfaceToStringSurface(surface: Int?): String {
    val stringSurface: String
    if (surface != null) {
        stringSurface = "$surface m²"
    } else {
        stringSurface = "To inform surface"
    }
    return stringSurface
}

fun formatPriceToStringPriceDollar(price: Long?): String {
    val stringPrice: String
    if (price != null) stringPrice = "$price $"
    else {
        stringPrice = "To inform price"
    }
    return stringPrice
}

fun formatPriceToStringPriceEuro(price: Long?): String {
    val stringPrice: String
    if (price != null) stringPrice = "$price €"
    else {
        stringPrice = "To inform price"
    }
    return stringPrice
}

fun formatNumberRoomToString(room: Int?): String {
    val stringRoom: String
    if (room != null) stringRoom = "$room rooms"
    else {
        stringRoom = "To inform number room"
    }
    return stringRoom
}

fun formatStringDescription(description: String?): String {
    val stringDescription: String
    if (description != null) {
        stringDescription = description
    } else {
        stringDescription = "Complete the description for users"
    }
    return stringDescription
}

fun formatDateLongToString(dateLong: Long?): String {
    val date = Date(dateLong!!)
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    return simpleDateFormat.format(date)
}


fun formatAddress(address: String?): String {
    val addresseString: String
    if (address != null&&address!="") {
        addresseString = address.substring(address.indexOf(",") + 2)
    } else {
        addresseString = "To inform address"
    }
    return addresseString
}

fun convertDollarToEuro(dollars: Long): Long {
    val euro=Math.round(dollars*0.812)
    return euro
}



