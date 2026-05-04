package intae;

import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class GreeterNode implements NodeAction<SimpleState> {
    @Override
    public Map<String, Object> apply(SimpleState simpleState) {
        System.out.println("GreeterNode executing. Current messages: " + simpleState.messages());

        return Map.of(SimpleState.MESSAGES_KEY, "Hello from GreeterNode!");
    }
}
