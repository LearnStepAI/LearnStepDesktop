package ai.learnstep.desktop.net

import ai.learnstep.desktop.net.model.ChatMessage
import ai.learnstep.desktop.net.model.ModelInfo
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OpenRouterClient(
    private val apiKeyProvider: () -> String?,
) {
    private val client = OkHttpClient()
    private val baseUrl = "https://openrouter.ai/api/v1"

    fun listModels(): Result<List<ModelInfo>> {
        val request = buildRequest {
            url("$baseUrl/models")
            get()
        }

        return executeRequest(request) { responseBody ->
            parseModelsResponse(responseBody)
        }
    }

    fun chatCompletion(
        model: String,
        messages: List<ChatMessage>,
        temperature: Double? = null,
    ): Result<String> {
        val requestBody = createChatCompletionBody(model, messages, temperature)

        val request = buildRequest {
            url("$baseUrl/chat/completions")
            post(requestBody)
        }

        return executeRequest(request) { responseBody ->
            parseChatCompletionResponse(responseBody)
        }
    }

    private fun buildRequest(block: Request.Builder.() -> Request.Builder): Request {
        return Request.Builder()
            .let(block)
            .let(::addAuthHeaders)
            .build()
    }

    private fun addAuthHeaders(builder: Request.Builder): Request.Builder {
        val key = apiKeyProvider()
        if (!key.isNullOrBlank()) {
            builder.addHeader("Authorization", "Bearer $key")
        }

        builder.addHeader("HTTP-Referer", "https://learnstep.local")
        builder.addHeader("X-Title", "LearnStep Desktop")

        return builder
    }

    private fun <T> executeRequest(
        request: Request,
        parser: (String) -> T
    ): Result<T> {
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.failure(IOException("HTTP ${response.code}"))
                }

                val body = response.body?.string()
                    ?: return Result.failure(IOException("Empty body"))

                Result.success(parser(body))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseModelsResponse(responseBody: String): List<ModelInfo> {
        val json = JSONObject(responseBody)
        val data = json.optJSONArray("data") ?: JSONArray()

        return buildList {
            for (i in 0 until data.length()) {
                val obj = data.getJSONObject(i)
                val id = obj.optString("id")
                val name = obj.optString("name", id)
                val context = obj.optJSONObject("top_provider")?.optInt("context_length")
                add(ModelInfo(id = id, name = name, contextLength = context))
            }
        }
    }

    private fun parseChatCompletionResponse(responseBody: String): String {
        val root = JSONObject(responseBody)
        val choices = root.optJSONArray("choices") ?: JSONArray()

        return if (choices.length() > 0) {
            val message = choices.getJSONObject(0).optJSONObject("message")
            message?.optString("content") ?: ""
        } else ""
    }

    private fun createChatCompletionBody(
        model: String,
        messages: List<ChatMessage>,
        temperature: Double?
    ): RequestBody {
        val json = JSONObject()
            .put("model", model)
            .put("messages", JSONArray().apply {
                messages.forEach { message ->
                    put(JSONObject()
                        .put("role", message.role)
                        .put("content", message.content)
                    )
                }
            })

        if (temperature != null) {
            json.put("temperature", temperature)
        }

        return json.toString().toRequestBody("application/json".toMediaType())
    }
}
