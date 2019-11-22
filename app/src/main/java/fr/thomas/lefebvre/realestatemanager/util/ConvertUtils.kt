package fr.thomas.lefebvre.realestatemanager.util


import fr.thomas.lefebvre.realestatemanager.database.Agent
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