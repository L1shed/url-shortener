package me.lished.service

import me.lished.dao.Visit

object VisitDatabase : AbstractDatabase<Visit>(
    { getCollection("ips") }
)