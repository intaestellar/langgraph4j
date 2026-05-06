package intae;


import dev.langchain4j.agent.tool.Tool;

public class OrderTools {

    @Tool("배송되지 않은 주문을 취소합니다.")
    public String cancelOrder(String orderId) {
        return "주문 %s이(가) 취소되었습니다."
                .formatted(orderId);
    }
}
