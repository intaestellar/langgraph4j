package intae;

import org.bsc.langgraph4j.GraphDefinition;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;

import java.util.Map;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws GraphStateException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println(String.format("Hello and welcome!"));

        GreeterNode greeterNode = new GreeterNode();
        ResponderNode responderNode = new ResponderNode();

        var stateGraph = new StateGraph<>(SimpleState.SCHEMA, initData -> new SimpleState(initData))
                .addNode("greeter", node_async(greeterNode))
                .addNode("responder", node_async(responderNode))
                .addEdge(START, "greeter")
                .addEdge("greeter", "responder")
                .addEdge("responder", END)
                ;

        var compiledGraph = stateGraph.compile();

        for (var item : compiledGraph.stream(Map.of(SimpleState.MESSAGES_KEY, "Let's, begin!"))) {
            System.out.println(item);
        }
    }
}
