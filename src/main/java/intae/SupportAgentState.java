package intae;

import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.state.AgentState;

import java.util.List;
import java.util.Map;

public class SupportAgentState extends AgentState {

    public SupportAgentState(Map<String, Object> initData) {
        super(initData);
    }

    public Map<String, Object> order() {
        return this.<Map<String, Object>>value("order")
                .orElse(Map.of("orderId", "UNKNOWN"));
    }

    public List<ChatMessage> messages() {
        return this.<List<ChatMessage>>value("messages")
                .orElse(List.of());
    }
}
