package ru.zotreex.telescope.auth.code

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

class CodeAuthModel : ScreenModel {

    val codeField = MutableStateFlow("")
}