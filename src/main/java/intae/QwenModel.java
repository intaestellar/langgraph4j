package intae;

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.net.http.HttpClient;

public class QwenModel {

    public static ChatModel getModel() {
        JdkHttpClientBuilder httpClientBuilder = JdkHttpClient.builder()
                .httpClientBuilder(HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1));

        return OpenAiChatModel.builder()
                .httpClientBuilder(httpClientBuilder)
                .apiKey("OPENAI_API_KEY")
                .modelName("/models/Qwen3-14B")
                .baseUrl("http://192.168.35.48:5008/v1")
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
