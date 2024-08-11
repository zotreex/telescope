package ru.zotreex.telescope.experenets

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch

class DataScreenModel(val id: String) : ScreenModel {
    fun fetchData(){
        screenModelScope.launch {
            //fetch data code
        }
    }
}