package com.example.csslearn

data class PageData(val title: String, val route: String, val imageRes: Int)

val screens = listOf(
    PageData("Home","home", R.drawable.quiz),
    PageData("Quiz","quiz", R.drawable.quiz),
    PageData("Management", "management", R.drawable.admin),
    PageData("CSSSelectors", "cssselectors", R.drawable.css_icon),
    PageData("PositioningGame", "home", R.drawable.construction),
)
