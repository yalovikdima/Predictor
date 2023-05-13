package com.origins.predictor.data

import io.ktor.client.*
import io.ktor.client.request.*

fun HttpRequestBuilder.addParams(params: Map<String, String?>) {
    params.forEach {
        if (it.value != null) {
            parameter(it.key, it.value)
        }
    }
}

suspend inline fun <reified T> HttpClient.get(
    path: String,
    query: Map<String, String?>,
    block: (HttpRequestBuilder.() -> Unit) = {}
): T {
    return this.get(path) {
        addParams(query)
        block.invoke(this)
    }
}