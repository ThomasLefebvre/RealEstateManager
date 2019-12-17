package fr.thomas.lefebvre.realestatemanager.provider

import android.content.ContentValues
import fr.thomas.lefebvre.realestatemanager.database.Agent
import fr.thomas.lefebvre.realestatemanager.database.Property

fun agentFromContentValues(values: ContentValues): Agent { //set agent with content provider keys

    val agent= Agent()
    if (values.containsKey("idAgent"))agent.idAgent=values.getAsLong("idAgent")
    if(values.containsKey("name"))agent.name=values.getAsString("name")
    if(values.containsKey("mail"))agent.mail=values.getAsString("mail")
    return agent

}

fun propertyFromContentValues(values: ContentValues): Property {//set property with content provider keys

    val property= Property()
    if (values.containsKey("idProperty"))property.idProperty=values.getAsLong("idProperty")
    if(values.containsKey("price"))property.price=values.getAsLong("price")
    if(values.containsKey("type"))property.type=values.getAsString("type")
    if(values.containsKey("surface"))property.surface=values.getAsInteger("surface")
    if(values.containsKey("numberRoom"))property.numberRoom=values.getAsInteger("numberRoom")
    if(values.containsKey("description"))property.description=values.getAsString("description")
    if(values.containsKey("address"))property.address=values.getAsString("address")
    if(values.containsKey("lat"))property.lat=values.getAsDouble("lat")
    if(values.containsKey("lng"))property.lng=values.getAsDouble("lng")
    if(values.containsKey("parc"))property.parc=values.getAsBoolean("parc")
    if(values.containsKey("sport"))property.sport=values.getAsBoolean("sport")
    if(values.containsKey("school"))property.school=values.getAsBoolean("school")
    if(values.containsKey("transport"))property.transport=values.getAsBoolean("transport")
    if(values.containsKey("stateProperty"))property.stateProperty=values.getAsBoolean("stateProperty")
    if(values.containsKey("creationDate"))property.creationDate=values.getAsLong("creationDate")
    if(values.containsKey("saleDate"))property.saleDate=values.getAsLong("saleDate")
    if(values.containsKey("idAgent"))property.idAgent=values.getAsLong("idAgent")

    return property

}