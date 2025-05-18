package com.charmflex.cp.flexiexpensesmanager

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform