// port-lint: source core/src/error.rs
package ai.solace.coder.core

import ai.solace.coder.core.ExecToolCallOutput
import ai.solace.coder.core.KnownPlan
import ai.solace.coder.core.PlanType
import ai.solace.coder.core.StreamOutput
import ai.solace.coder.protocol.CodexErrorInfo
import ai.solace.coder.protocol.RateLimitSnapshot
import ai.solace.coder.protocol.RateLimitWindow
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

private fun rateLimitSnapshot(): RateLimitSnapshot {
    val primaryResetAt = Instant.parse("2024-01-01T01:00:00Z").epochSeconds
    val secondaryResetAt = Instant.parse("2024-01-01T02:00:00Z").epochSeconds
    return RateLimitSnapshot(
        primary = RateLimitWindow(
            usedPercent = 50.0,
            windowMinutes = 60,
            resetsAt = primaryResetAt,
        ),
        secondary = RateLimitWindow(
            usedPercent = 30.0,
            windowMinutes = 120,
            resetsAt = secondaryResetAt,
        ),
        credits = null,
    )
}

private inline fun <T> withNowOverride(now: Instant, f: () -> T): T {
    nowOverride = now
    return try {
        f()
    } finally {
        nowOverride = null
    }
}

private fun usageLimitReachedErrorFormatsPlusPlan() {
    val err = UsageLimitReachedError(
        planType = PlanType.Known(KnownPlan.Plus),
        resetsAt = null,
        rateLimits = rateLimitSnapshot(),
    )
    assertEquals(
        "You've hit your usage limit. Upgrade to Pro (https://openai.com/chatgpt/pricing), visit https://chatgpt.com/codex/settings/usage to purchase more credits or try again later.",
        err.toString(),
    )
}

private fun sandboxDeniedUsesAggregatedOutputWhenStderrEmpty() {
    val output = ExecToolCallOutput(
        exitCode = 77,
        stdout = StreamOutput(""),
        stderr = StreamOutput(""),
        aggregatedOutput = StreamOutput("aggregate detail"),
        duration = 10.milliseconds,
        timedOut = false,
    )
    val err = CodexErr.Sandbox(SandboxErr.Denied(output))
    assertEquals("aggregate detail", getErrorMessageUi(err))
}

private fun sandboxDeniedReportsBothStreamsWhenAvailable() {
    val output = ExecToolCallOutput(
        exitCode = 9,
        stdout = StreamOutput("stdout detail"),
        stderr = StreamOutput("stderr detail"),
        aggregatedOutput = StreamOutput(""),
        duration = 10.milliseconds,
        timedOut = false,
    )
    val err = CodexErr.Sandbox(SandboxErr.Denied(output))
    assertEquals("stderr detail\nstdout detail", getErrorMessageUi(err))
}

private fun sandboxDeniedReportsStdoutWhenNoStderr() {
    val output = ExecToolCallOutput(
        exitCode = 11,
        stdout = StreamOutput("stdout only"),
        stderr = StreamOutput(""),
        aggregatedOutput = StreamOutput(""),
        duration = 8.milliseconds,
        timedOut = false,
    )
    val err = CodexErr.Sandbox(SandboxErr.Denied(output))
    assertEquals("stdout only", getErrorMessageUi(err))
}

private fun sandboxDeniedReportsExitCodeWhenNoOutputAvailable() {
    val output = ExecToolCallOutput(
        exitCode = 13,
        stdout = StreamOutput(""),
        stderr = StreamOutput(""),
        aggregatedOutput = StreamOutput(""),
        duration = 5.milliseconds,
        timedOut = false,
    )
    val err = CodexErr.Sandbox(SandboxErr.Denied(output))
    assertEquals(
        "command failed inside sandbox with exit code 13",
        getErrorMessageUi(err),
    )
}

private fun toErrorEventHandlesResponseStreamFailed() {
    val source =
        object : Throwable() {
            override fun toString(): String =
                "HTTP status client error (429 Too Many Requests) for url (http://example.com/)"
        }
    val err = CodexErr.ResponseStreamFailed(
        ResponseStreamFailed(
            source = source,
            requestId = "req-123",
            status = 429,
        )
    )

    val event = err.toErrorEvent("prefix")

    assertEquals(
        "prefix: Error while reading the server response: HTTP status client error (429 Too Many Requests) for url (http://example.com/), request id: req-123",
        event.message,
    )
    assertEquals(
        CodexErrorInfo.ResponseStreamConnectionFailed(httpStatusCode = 429),
        event.codexErrorInfo,
    )
}

private fun usageLimitReachedErrorFormatsFreePlan() {
    val err = UsageLimitReachedError(
        planType = PlanType.Known(KnownPlan.Free),
        resetsAt = null,
        rateLimits = rateLimitSnapshot(),
    )
    assertEquals(
        "You've hit your usage limit. Upgrade to Plus to continue using Codex (https://openai.com/chatgpt/pricing).",
        err.toString(),
    )
}

private fun usageLimitReachedErrorFormatsDefaultWhenNone() {
    val err = UsageLimitReachedError(
        planType = null,
        resetsAt = null,
        rateLimits = rateLimitSnapshot(),
    )
    assertEquals(
        "You've hit your usage limit. Try again later.",
        err.toString(),
    )
}

private fun usageLimitReachedErrorFormatsTeamPlan() {
    val base = Instant.parse("2024-01-01T00:00:00Z")
    val resetsAt = base.plus(kotlin.time.Duration.parse("PT1H"))
    withNowOverride(base) {
        val expectedTime = formatRetryTimestamp(resetsAt)
        val err = UsageLimitReachedError(
            planType = PlanType.Known(KnownPlan.Team),
            resetsAt = resetsAt,
            rateLimits = rateLimitSnapshot(),
        )
        val expected =
            "You've hit your usage limit. To get more access now, send a request to your admin or try again at $expectedTime."
        assertEquals(expected, err.toString())
    }
}

private fun usageLimitReachedErrorFormatsBusinessPlanWithoutReset() {
    val err = UsageLimitReachedError(
        planType = PlanType.Known(KnownPlan.Business),
        resetsAt = null,
        rateLimits = rateLimitSnapshot(),
    )
    assertEquals(
        "You've hit your usage limit. To get more access now, send a request to your admin or try again later.",
        err.toString(),
    )
}

private fun usageLimitReachedErrorFormatsDefaultForOtherPlans() {
    val err = UsageLimitReachedError(
        planType = PlanType.Known(KnownPlan.Enterprise),
        resetsAt = null,
        rateLimits = rateLimitSnapshot(),
    )
    assertEquals(
        "You've hit your usage limit. Try again later.",
        err.toString(),
    )
}

private fun usageLimitReachedErrorFormatsProPlanWithReset() {
    val base = Instant.parse("2024-01-01T00:00:00Z")
    val resetsAt = base.plus(kotlin.time.Duration.parse("PT1H"))
    withNowOverride(base) {
        val expectedTime = formatRetryTimestamp(resetsAt)
        val err = UsageLimitReachedError(
            planType = PlanType.Known(KnownPlan.Pro),
            resetsAt = resetsAt,
            rateLimits = rateLimitSnapshot(),
        )
        val expected =
            "You've hit your usage limit. Visit https://chatgpt.com/codex/settings/usage to purchase more credits or try again at $expectedTime."
        assertEquals(expected, err.toString())
    }
}

private fun usageLimitReachedIncludesMinutesWhenAvailable() {
    val base = Instant.parse("2024-01-01T00:00:00Z")
    val resetsAt = base.plus(kotlin.time.Duration.parse("PT5M"))
    withNowOverride(base) {
        val expectedTime = formatRetryTimestamp(resetsAt)
        val err = UsageLimitReachedError(
            planType = null,
            resetsAt = resetsAt,
            rateLimits = rateLimitSnapshot(),
        )
        val expected = "You've hit your usage limit. Try again at $expectedTime."
        assertEquals(expected, err.toString())
    }
}

private fun unexpectedStatusCloudflareHtmlIsSimplified() {
    val err = UnexpectedResponseError(
        status = 403,
        body = "<html><body>Cloudflare error: Sorry, you have been blocked</body></html>",
        requestId = "ray-id",
    )
    assertEquals(
        "Access blocked by Cloudflare. This usually happens when connecting from a restricted region (status 403), request id: ray-id",
        err.toString(),
    )
}

private fun unexpectedStatusNonHtmlIsUnchanged() {
    val err = UnexpectedResponseError(
        status = 403,
        body = "plain text error",
        requestId = null,
    )
    assertEquals(
        "unexpected status 403: plain text error",
        err.toString(),
    )
}

private fun usageLimitReachedIncludesHoursAndMinutes() {
    val base = Instant.parse("2024-01-01T00:00:00Z")
    val resetsAt = base.plus(kotlin.time.Duration.parse("PT3H32M"))
    withNowOverride(base) {
        val expectedTime = formatRetryTimestamp(resetsAt)
        val err = UsageLimitReachedError(
            planType = PlanType.Known(KnownPlan.Plus),
            resetsAt = resetsAt,
            rateLimits = rateLimitSnapshot(),
        )
        val expected =
            "You've hit your usage limit. Upgrade to Pro (https://openai.com/chatgpt/pricing), visit https://chatgpt.com/codex/settings/usage to purchase more credits or try again at $expectedTime."
        assertEquals(expected, err.toString())
    }
}

private fun usageLimitReachedIncludesDaysHoursMinutes() {
    val base = Instant.parse("2024-01-01T00:00:00Z")
    val resetsAt = base.plus(kotlin.time.Duration.parse("PT51H5M"))
    withNowOverride(base) {
        val expectedTime = formatRetryTimestamp(resetsAt)
        val err = UsageLimitReachedError(
            planType = null,
            resetsAt = resetsAt,
            rateLimits = rateLimitSnapshot(),
        )
        val expected = "You've hit your usage limit. Try again at $expectedTime."
        assertEquals(expected, err.toString())
    }
}

private fun usageLimitReachedLessThanMinute() {
    val base = Instant.parse("2024-01-01T00:00:00Z")
    val resetsAt = base.plus(kotlin.time.Duration.parse("PT30S"))
    withNowOverride(base) {
        val expectedTime = formatRetryTimestamp(resetsAt)
        val err = UsageLimitReachedError(
            planType = null,
            resetsAt = resetsAt,
            rateLimits = rateLimitSnapshot(),
        )
        val expected = "You've hit your usage limit. Try again at $expectedTime."
        assertEquals(expected, err.toString())
    }
}

class ErrorTest {
    @Test
    fun usageLimitReachedErrorFormatsPlusPlan() {
        usageLimitReachedErrorFormatsPlusPlan()
    }

    @Test
    fun sandboxDeniedUsesAggregatedOutputWhenStderrEmpty() {
        sandboxDeniedUsesAggregatedOutputWhenStderrEmpty()
    }

    @Test
    fun sandboxDeniedReportsBothStreamsWhenAvailable() {
        sandboxDeniedReportsBothStreamsWhenAvailable()
    }

    @Test
    fun sandboxDeniedReportsStdoutWhenNoStderr() {
        sandboxDeniedReportsStdoutWhenNoStderr()
    }

    @Test
    fun sandboxDeniedReportsExitCodeWhenNoOutputAvailable() {
        sandboxDeniedReportsExitCodeWhenNoOutputAvailable()
    }

    // Rust constructs a reqwest::Error from an HTTP 429 response. In the
    // Kotlin port `ResponseStreamFailed.source` is a generic `Throwable` and
    // the HTTP status comes from the explicit `status` field, so we simulate
    // the same surface: a synthetic exception with the formatted reqwest
    // message and the 429 status passed alongside it.
    @Test
    fun toErrorEventHandlesResponseStreamFailed() {
        toErrorEventHandlesResponseStreamFailed()
    }

    @Test
    fun usageLimitReachedErrorFormatsFreePlan() {
        usageLimitReachedErrorFormatsFreePlan()
    }

    @Test
    fun usageLimitReachedErrorFormatsDefaultWhenNone() {
        usageLimitReachedErrorFormatsDefaultWhenNone()
    }

    @Test
    fun usageLimitReachedErrorFormatsTeamPlan() {
        usageLimitReachedErrorFormatsTeamPlan()
    }

    @Test
    fun usageLimitReachedErrorFormatsBusinessPlanWithoutReset() {
        usageLimitReachedErrorFormatsBusinessPlanWithoutReset()
    }

    @Test
    fun usageLimitReachedErrorFormatsDefaultForOtherPlans() {
        usageLimitReachedErrorFormatsDefaultForOtherPlans()
    }

    @Test
    fun usageLimitReachedErrorFormatsProPlanWithReset() {
        usageLimitReachedErrorFormatsProPlanWithReset()
    }

    @Test
    fun usageLimitReachedIncludesMinutesWhenAvailable() {
        usageLimitReachedIncludesMinutesWhenAvailable()
    }

    @Test
    fun unexpectedStatusCloudflareHtmlIsSimplified() {
        unexpectedStatusCloudflareHtmlIsSimplified()
    }

    @Test
    fun unexpectedStatusNonHtmlIsUnchanged() {
        unexpectedStatusNonHtmlIsUnchanged()
    }

    @Test
    fun usageLimitReachedIncludesHoursAndMinutes() {
        usageLimitReachedIncludesHoursAndMinutes()
    }

    @Test
    fun usageLimitReachedIncludesDaysHoursMinutes() {
        usageLimitReachedIncludesDaysHoursMinutes()
    }

    @Test
    fun usageLimitReachedLessThanMinute() {
        usageLimitReachedLessThanMinute()
    }
}
