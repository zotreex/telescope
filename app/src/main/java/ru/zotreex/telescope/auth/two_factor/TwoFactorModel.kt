package ru.zotreex.telescope.auth.two_factor

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

class TwoFactorModel: ScreenModel {

    val passwordField = MutableStateFlow("")
}