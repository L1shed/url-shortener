package me.lished.service

import me.lished.dao.UrlEntry

object UrlDatabase : AbstractDatabase<UrlEntry>(
    { getCollection("urls") }
)