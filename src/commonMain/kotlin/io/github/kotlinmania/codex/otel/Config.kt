// port-lint: source config.rs
package io.github.kotlinmania.codex.otel

import kotlinx.io.files.Path

data class OtelSettings(
    val environment: String,
    val serviceName: String,
    val serviceVersion: String,
    val codexHome: Path,
    val exporter: OtelExporter,
)

enum class OtelHttpProtocol {
    /** HTTP protocol with binary protobuf */
    Binary,
    /** HTTP protocol with JSON payload */
    Json,
}

data class OtelTlsConfig(
    val caCertificate: Path?,
    val clientCertificate: Path?,
    val clientPrivateKey: Path?,
)

sealed class OtelExporter {
    data object None : OtelExporter()

    data class OtlpGrpc(
        val endpoint: String,
        val headers: Map<String, String>,
        val tls: OtelTlsConfig?,
    ) : OtelExporter()

    data class OtlpHttp(
        val endpoint: String,
        val headers: Map<String, String>,
        val protocol: OtelHttpProtocol,
        val tls: OtelTlsConfig?,
    ) : OtelExporter()
}
