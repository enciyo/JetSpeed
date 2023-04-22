package com.enciyo.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "settings", strict = false)
class Settings {
    @field:Element(required = false, name = "servers")
    lateinit var servers: Servers
}



