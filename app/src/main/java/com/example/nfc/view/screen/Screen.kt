package com.example.nfc.view.screen

sealed class Screen(val route : String){
    data object Login: Screen("Login")
    data object Register: Screen("Register")
    data object Home: Screen("Home")
    data object Error: Screen("Error")

    fun withArgs(vararg args: Pair<String,String>):String{
        var newRoute = route
        args.forEach { (key,value) ->
            newRoute = newRoute.replace("{$key}",value)
        }
        return newRoute
    }
}