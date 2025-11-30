package ju.mad.tuitioncounter.data.service

import ju.mad.tuitioncounter.data.service.model.OpenRouterRequest
import ju.mad.tuitioncounter.data.service.model.OpenRouterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AiService {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Body request: OpenRouterRequest
    ): Response<OpenRouterResponse>
}