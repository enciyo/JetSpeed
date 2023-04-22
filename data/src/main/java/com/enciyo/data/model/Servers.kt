package com.enciyo.data.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "servers", strict = false)
class Servers {
    @field:ElementList(required = false, name = "server", inline = true)
    lateinit var server: List<Server>
}