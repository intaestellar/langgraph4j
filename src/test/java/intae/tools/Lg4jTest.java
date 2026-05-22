package intae.tools;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import intae.*;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.List;

public class Lg4jTest {
    @Test
    void firstChat() {
        JdkHttpClientBuilder httpClientBuilder = JdkHttpClient.builder()
                .httpClientBuilder(HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1));

        ChatModel model = OpenAiChatModel.builder()
                .httpClientBuilder(httpClientBuilder)
                .apiKey("OPENAI_API_KEY")
                .modelName("/models/Qwen3-14B")
                .baseUrl("http://192.168.35.48:5008/v1")
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .build();

        String answer = model.chat("자바의 record가 뭐야?");

        System.out.println(answer);
    }

    @Test
    void messageTest() {
        ChatModel model = QwenModel.getModel();

        ChatRequest request = ChatRequest.builder()
                .messages(
                        SystemMessage.from("너는 친절한 자바 강사야. 짧게 답해."),
                        UserMessage.from("record가 뭐야?"),
                        AiMessage.from("record는 불변 데이터 클래스예요."),
                        UserMessage.from("그럼 class랑 뭐가 달라?")
                ).build();

        ChatResponse response = model.chat(request);

        String answer = response.aiMessage().text();

        System.out.println(answer);
    }

    @Test
    void memoryTest() {
        ChatModel model = QwenModel.getModel();

        ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

        memory.add(SystemMessage.from("너는 친절한 자바 강사야. 짧게 답해"));

        // 1
        memory.add(UserMessage.from("record가 뭐야?"));
        ChatResponse r1 = model.chat(ChatRequest.builder().messages(memory.messages()).build());
        memory.add(r1.aiMessage());

        memory.add(UserMessage.from("class랑 뭐가 달라?"));
        ChatResponse r2 = model.chat(ChatRequest.builder().messages(memory.messages()).build());
        memory.add(r2.aiMessage());

        System.out.println(r2.aiMessage().text());
        System.out.println("=== 메모장에 남은 메시지 수: " + memory.messages().size());
    }

    @Test
    void aiServiceTest() {
        ChatModel model = QwenModel.getModel();

        JavaTutor tutor = AiServices.builder(JavaTutor.class)
                .chatModel(model)
                .chatMemoryProvider(userId -> MessageWindowChatMemory.withMaxMessages(20))
                .build();

        System.out.println(tutor.ask("alice", "record가 뭐야?"));
        System.out.println(tutor.ask("alice", "class랑 뭐가 달라?"));
        System.out.println(tutor.ask("bob", "record가 뭐야?"));
    }

    @Test
    void isSpamTest() {
        ChatModel model = QwenModel.getModel();

        SpamFilter spamFilter = AiServices.builder(SpamFilter.class)
                .chatModel(model)
                .build();

        System.out.println("" + spamFilter.isSpam("로또 당첨! 지금 클릭!"));
    }

    @Test
    void sentimentTest() {

        SentimentAnalyzer analyzer = AiServices.builder(SentimentAnalyzer.class)
                .chatModel(QwenModel.getModel())
                .build();

        Sentiment sentiment = analyzer.analyze("오늘 너무 행복해요!");

        System.out.println("sentiment: " + sentiment);
    }

    @Test
    void recipeTest() {
        ChefAi chefAi = AiServices.builder(ChefAi.class)
                .chatModel(QwenModel.getModel())
                .build();

        Recipe r = chefAi.createRecipe("김치볶음밥");

        System.out.println(r.name());
        System.out.println(r.ingredients());
        System.out.println(r.cookingMinutes());
    }

    @Test
    void movieRecommendationTest() {
        MovieAi movieAi = AiServices.builder(MovieAi.class)
                .chatModel(QwenModel.getModel())
                .build();

        MovieRecommendations recommends = movieAi.recommend("우울할 때 위로받고 싶어");

        recommends.recommendations()
                .forEach(m -> System.out.println(m.title() + " (" + m.year() + ")"));
    }
}
