// port-lint: source chatgpt/src/chatgpt_token.rs
package io.github.kotlinmania.codex.chatgpt

import io.github.kotlinmania.codex.core.CodexAuth
import io.github.kotlinmania.codex.core.TokenData
import io.github.kotlinmania.codex.core.auth.AuthCredentialsStoreMode
import io.github.kotlinmania.codex.core.codexAuthFromAuthStorage
import kotlinx.io.files.Path

@kotlin.concurrent.Volatile
private var chatgptToken: TokenData? = null

fun getChatgptTokenData(): TokenData? = chatgptToken

fun setChatgptTokenData(value: TokenData) {
    chatgptToken = value
}

/** Initialize the ChatGPT token from auth.json file */
suspend fun initChatgptTokenFromAuth(
    codexHome: Path,
    authCredentialsStoreMode: AuthCredentialsStoreMode,
) {
    val auth: CodexAuth =
        codexAuthFromAuthStorage(codexHome, authCredentialsStoreMode).getOrThrow()
            ?: return
    val tokenData = auth.getTokenData().getOrThrow()
    setChatgptTokenData(tokenData)
}
