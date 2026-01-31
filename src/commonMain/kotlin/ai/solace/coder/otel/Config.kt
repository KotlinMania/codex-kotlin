// port-lint: source otel/src/config.rs
package ai.solace.coder.otel

import okio.Path

data class OtelSettings(
    val environment: String,
    val serviceName: String,
    val serviceVersion: String,
    val codexHome: Path,
    val exporter: OtelExporter
)

enum class OtelHttpProtocol {
    /**
     * HTTP protocol with binary protobuf
     */
    Binary,
    /**
     * HTTP protocol with JSON payload
     */
    Json
}

data class OtelTlsConfig(
    val caCertificate: Path? = null,
    val clientCertificate: Path? = null,
    val clientPrivateKey: Path? = null
)

sealed class OtelExporter {
    object None : OtelExporter()

    data class OtlpGrpc(
        val endpoint: String,
        val headers: HashMap<String, String>,
        val tls: OtelTlsConfig?
    ) : OtelExporter()

    data class OtlpHttp(
        val endpoint: String,
        val headers: HashMap<String, String>,
        val protocol: OtelHttpProtocol,
        val tls: OtelTlsConfig?
    ) : OtelExporter()
}
