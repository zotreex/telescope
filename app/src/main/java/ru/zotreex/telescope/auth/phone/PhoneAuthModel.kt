package ru.zotreex.telescope.auth.phone

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneAuthModel : ScreenModel {

    val phoneField = MutableStateFlow("")
}