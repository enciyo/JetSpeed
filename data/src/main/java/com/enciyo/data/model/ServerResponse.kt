package com.enciyo.data.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "server", strict = false)
class ServerResponse {
    @field:Attribute(required = true, name = "url")
    lateinit var url: String

    @field:Attribute(required = true)
    lateinit var name: String

    @field:Attribute(required = true)
    lateinit var country: String

    @field:Attribute(required = true)
    lateinit var sponsor: String

    @field:Attribute(required = true)
    lateinit var host: String


    override fun toString(): String {
        return "Server(url=$url,name =$name,country=$country,sponsor=$sponsor,host=$host)"
    }
}