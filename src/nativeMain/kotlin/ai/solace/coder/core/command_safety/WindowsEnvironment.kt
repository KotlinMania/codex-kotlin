// port-lint: source windows-sandbox-rs/src/env.rs
package ai.solace.coder.core.command_safety

import ai.solace.coder.utils.Environment

/**
 * Normalizes null device references in environment variables to Windows 'NUL'.
 */
fun normalizeNullDeviceEnv(envMap: MutableMap<String, String>) {
    val keys = envMap.keys.toList()
    for (k in keys) {
        val v = envMap[k]
        if (v != null) {
            val t = v.trim().lowercase()
            if (t == "/dev/null" || t == "\\\\dev\\null" || t == "\\\\\\\\dev\\\\\\\\null") {
                envMap[k] = "NUL"
            }
        }
    }
}

/**
 * Ensures a non-interactive pager is used.
 */
fun ensureNonInteractivePager(envMap: MutableMap<String, String>) {
    if (!envMap.containsKey("GIT_PAGER")) {
        envMap["GIT_PAGER"] = "more.com"
    }
    if (!envMap.containsKey("PAGER")) {
        envMap["PAGER"] = "more.com"
    }
    if (!envMap.containsKey("LESS")) {
        envMap["LESS"] = ""
    }
}

/**
 * Prepends a path to the PATH environment variable.
 */
private fun prependPath(envMap: MutableMap<String, String>, prefix: String) {
    val existing = envMap["PATH"] ?: Environment.get("PATH") ?: ""
    val parts = existing.split(';')
    if (parts.firstOrNull()?.equals(prefix, ignoreCase = true) == true) {
        return
    }
    val newPath = if (existing.isEmpty()) {
        prefix
    } else {
        "$prefix;$existing"
    }
    envMap["PATH"] = newPath
}

/**
 * Reorders PATHEXT so that stubs (.BAT, .CMD) are tried first.
 */
private fun reorderPathExtForStubs(envMap: MutableMap<String, String>) {
    val default = envMap["PATHEXT"] ?: Environment.get("PATHEXT") ?: ".COM;.EXE;.BAT;.CMD"
    val exts = default.split(';').filter { it.isNotEmpty() }
    val extsNorm = exts.map { it.uppercase() }
    val want = listOf(".BAT", ".CMD")
    
    val front = mutableListOf<String>()
    for (w in want) {
        val idx = extsNorm.indexOf(w)
        if (idx != -1) {
            front.add(exts[idx])
        }
    }
    
    val rest = exts.filterIndexed { i, _ ->
        val up = extsNorm[i]
        up != ".BAT" && up != ".CMD"
    }
    
    val combined = front + rest
    envMap["PATHEXT"] = combined.joinToString(";")
}

/**
 * Ensures denybin stubs exist for the given tools.
 * Note: This implementation is a placeholder for actual file IO which might be platform specific.
 */
private fun ensureDenybin(tools: List<String>, denybinDir: String?): String {
    val base = denybinDir ?: (Environment.HOME?.let { "$it/.sbx-denybin" } ?: ".sbx-denybin")
    // In a real port, we would use a file system API to create the directory and files.
    // For now, we assume the environment where this runs will handle the actual creation
    // or we will use a platform-specific FS utility if available.
    // TODO: Implement actual file creation using a Multiplatform FS API or platform-specific posix calls.
    return base
}

/**
 * Applies no-network settings to the environment map.
 */
fun applyNoNetworkToEnv(envMap: MutableMap<String, String>) {
    envMap["SBX_NONET_ACTIVE"] = "1"
    if (!envMap.containsKey("HTTP_PROXY")) envMap["HTTP_PROXY"] = "http://127.0.0.1:9"
    if (!envMap.containsKey("HTTPS_PROXY")) envMap["HTTPS_PROXY"] = "http://127.0.0.1:9"
    if (!envMap.containsKey("ALL_PROXY")) envMap["ALL_PROXY"] = "http://127.0.0.1:9"
    if (!envMap.containsKey("NO_PROXY")) envMap["NO_PROXY"] = "localhost,127.0.0.1,::1"
    if (!envMap.containsKey("PIP_NO_INDEX")) envMap["PIP_NO_INDEX"] = "1"
    if (!envMap.containsKey("PIP_DISABLE_PIP_VERSION_CHECK")) envMap["PIP_DISABLE_PIP_VERSION_CHECK"] = "1"
    if (!envMap.containsKey("NPM_CONFIG_OFFLINE")) envMap["NPM_CONFIG_OFFLINE"] = "true"
    if (!envMap.containsKey("CARGO_NET_OFFLINE")) envMap["CARGO_NET_OFFLINE"] = "true"
    if (!envMap.containsKey("GIT_HTTP_PROXY")) envMap["GIT_HTTP_PROXY"] = "http://127.0.0.1:9"
    if (!envMap.containsKey("GIT_HTTPS_PROXY")) envMap["GIT_HTTPS_PROXY"] = "http://127.0.0.1:9"
    if (!envMap.containsKey("GIT_SSH_COMMAND")) envMap["GIT_SSH_COMMAND"] = "cmd /c exit 1"
    if (!envMap.containsKey("GIT_ALLOW_PROTOCOLS")) envMap["GIT_ALLOW_PROTOCOLS"] = ""

    // Block interactive network tools
    val base = ensureDenybin(listOf("ssh", "scp"), null)
    
    // In Rust, it removes curl/wget stubs. Here we would do the same if we had FS access.
    // TODO: Implement actual file removal.
    
    prependPath(envMap, base)
    reorderPathExtForStubs(envMap)
}
