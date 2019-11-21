package fr.thomas.lefebvre.realestatemanager.util

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import fr.thomas.lefebvre.realestatemanager.database.Agent



fun formatListAgentToListString(agents: List<Agent>): List<String> {
    val listNameAgent = mutableListOf<String>()
    agents.forEach {
        listNameAgent.add(it.name)
    }
    return listNameAgent
}
