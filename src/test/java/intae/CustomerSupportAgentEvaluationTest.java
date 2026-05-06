package intae;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.CompiledGraph;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerSupportAgentEvaluationTest {

    @Test
    void minimalEvaluation() throws Exception {

        CompiledGraph<SupportAgentState> graph = Main.constructGraph();

        Map<String, Object> exampleOrder = Map.of("orderId", "B73973");
        List<UserMessage> convo = List.of(
                UserMessage.from("""
                        더 저렴한 곳을 찾았습니다.
                        주문 #B73973을 취소해 주세요.
                        """)
        );

        SupportAgentState result = graph.invoke(Map.of(
                "order", exampleOrder,
                "messages", convo
        )).orElseThrow();

        List<ChatMessage> messages = result.messages();

        boolean hasToolCall = messages.stream().anyMatch(m ->
                (m instanceof AiMessage ai && ai.hasToolExecutionRequests()
                        || m instanceof ToolExecutionResultMessage));

        assertTrue(hasToolCall, "주문 취소 도구가 호출되지 않음");

        boolean hasConfirmation = messages.stream().anyMatch(m -> textOf(m).contains("취소"));

        assertTrue(hasConfirmation, "확인 메시지가 누락됨");

        System.out.println("✅ 에이전트가 최소 평가 기준을 통과했습니다.");
    }

    private static String textOf(ChatMessage m) {
        return switch (m) {
            case AiMessage ai -> ai.text() == null ? "" : ai.text();
            case ToolExecutionResultMessage t -> t.text();
            case UserMessage u -> u.singleText();
            default -> m.toString();
        };
    }
}
