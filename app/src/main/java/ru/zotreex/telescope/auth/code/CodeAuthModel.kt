package ru.zotreex.telescope.auth.code

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient

class CodeAuthModel(authorizationHandler: AuthorizationHandler, tdClient: TdClient) : ScreenModel {

    val codeField = MutableStateFlow("")

    //.send(TdApi.CheckAuthenticationCode(it.text.toString()))
}