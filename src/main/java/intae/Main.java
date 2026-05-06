package intae;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.langchain4j.serializer.std.LC4jStateSerializer;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static CompiledGraph<SupportAgentState> constructGraph() throws Exception {
        Map<String, Channel<?>> schema = Map.of(
                "messages", Channels.<ChatMessage>appender(List::of)
        );

        var stateSerializer = new LC4jStateSerializer<>(SupportAgentState::new);

        return new StateGraph<>(schema, stateSerializer)
                .addNode("assistant", node_async(new CallModelAction()))
                .addEdge(START, "assistant")
                .addEdge("assistant", END)
                .compile();
    }

    static void main() throws Exception {
        CompiledGraph<SupportAgentState> graph = constructGraph();

        Map<String, Object> input = Map.of(
                "order", Map.of("orderId", "B73973"),
                "messages", List.of(UserMessage.from("주문 #B73973를 취소해주세요."))
        );

        SupportAgentState result = graph.invoke(input)
                .orElseThrow();

        for (ChatMessage msg : result.messages()) {
            System.out.println(msg.type() + ": " + msg);
        }
    }
}
