package fr.thomas.lefebvre.realestatemanager.provider

import android.content.ContentValues
import fr.thomas.lefebvre.realestatemanager.database.Agent

fun agentFromContentValues(values: ContentValues): Agent {

    val agent= Agent()
    if (values.containsKey("idAgent"))agent.idAgent=values.getAsLong("idAgent")
    if(values.containsKey("name"))agent.name=values.getAsString("name")
    if(values.containsKey("mail"))agent.mail=values.getAsString("mail")
    return agent

}