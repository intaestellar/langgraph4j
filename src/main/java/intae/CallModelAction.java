package intae;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import intae.tools.OrderTools;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CallModelAction implements NodeAction<SupportAgentState> {

    private final ChatModel llm = OpenAiChatModel.builder()
            .apiKey("OPENAI_API_KEY")
            .modelName("/models/Qwen3-14B")
            .baseUrl("http://192.168.35.48:5008/v1")
            .temperature(0.0)
            .logRequests(true)
            .logResponses(true)
            .build();

    private final OrderTools tools = new OrderTools();

    @Override
    public Map<String, Object> apply(SupportAgentState state) throws Exception {
        List<ChatMessage> messages = state.messages();

        Map<String, Object> order = state.order();

        String prompt = """
                당신은 이커머스 지원 에이전트입니다.
                주문 ID: %s
                고객이 취소를 요청하면 cancelOrder(orderId)를 호출하고
                간단한 확인 메시지를 보내세요.
                그렇지 않으면 일반적으로 응답하세요.
                """.formatted(order.get("orderId"));

        List<ChatMessage> full = new ArrayList<>();
        full.add(SystemMessage.from(prompt));
        full.addAll(messages);

        var toolSpec = ToolSpecifications.toolSpecificationsFrom(tools);

        AiMessage first = llm.chat(ChatRequest.builder()
                .messages(full)
                .toolSpecifications(toolSpec)
                .build()).aiMessage();

        List<ChatMessage> out = new ArrayList<>();
        out.add(first);

        if (first.hasToolExecutionRequests()) {
            ToolExecutionRequest req = first.toolExecutionRequests().get(0);
            String result = new DefaultToolExecutor(tools, req).execute(req, null);
            out.add(ToolExecutionResultMessage.from(req, result));

            List<ChatMessage> followUp = new ArrayList<>(full);
            followUp.addAll(out);

            AiMessage second = llm.chat(ChatRequest.builder()
                    .messages(followUp)
                    .toolSpecifications(toolSpec)
                    .build()).aiMessage();

            out.add(second);
        }

        return Map.of("messages", out);
    }
}
