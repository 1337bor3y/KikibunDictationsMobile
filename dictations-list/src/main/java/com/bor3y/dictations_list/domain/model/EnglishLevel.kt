package com.bor3y.dictations_list.domain.model

sealed class EnglishLevel(val description: String) {
    data object A1 : EnglishLevel("Beginner")
    data object A2 : EnglishLevel("Elementary")
    data object B1 : EnglishLevel("Intermediate")
    data object B2 : EnglishLevel("Upper-Intermediate")
    data object C1 : EnglishLevel("Advanced")
    data object C2 : EnglishLevel("Proficient")
}