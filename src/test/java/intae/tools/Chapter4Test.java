package intae.tools;

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import intae.QwenModel;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

class Chapter4Test {

    interface CalculatorAgent {

        @SystemMessage("""
                당신은 정확한 계산기입니다. 필요한 산술은 반드시 도구를 사용해 계산하세요.
                사고 과정(<think> 등)은 출력하지 말고, 한국어로 짧게 결과만 답하세요.
                """)
        String chat(String userMessage);
    }

    interface Agent {
        String chat(String userMessage);
    }

    @Test
    void toolsTest() {
        ChatModel llm = QwenModel.getModel();

        CalculatorAgent agent = AiServices.builder(CalculatorAgent.class)
                .chatModel(llm)
                .tools(new CalculatorTools())
                .build();

        String answer = agent.chat("393 * 12.25는 얼마인가요? 그리고 11 + 49는요?");

        // Qwen3가 <think>...</think>를 답에 섞을 수 있어 후처리
        String clean = answer.replaceAll("(?s)<think>.*?</think>\\s*", "").trim();
        System.out.println("최종 답변: " + clean);
    }

    @Test
    void wikipediaTest() {
        ChatModel llm = QwenModel.getModel();

        Agent agent = AiServices.builder(Agent.class)
                .chatModel(llm)
                .tools(new WikipediaTools())
                .build();

        String answer = agent.chat("Buzz Aldrin의 주요 업적은 무엇인가요?");

        String clean = answer.replaceAll("(?s)<think>.*?</think>\\s*", "").trim();
        System.out.println("최종 답변: " + clean);

    }

    @Test
    void pokemonTest() {
        ChatModel model = QwenModel.getModel();

        Agent agent = AiServices.builder(Agent.class)
                .chatModel(model)
                .tools(new PokemonTools())
                .build();

        String answer = agent.chat("피카츄의 타입은 무엇인가요?");

        String clean = answer.replaceAll("(?s)<think>.*?</think>\\s*", "").trim();
        System.out.println("최종 답변: " + clean);
    }

}