package intae;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SentimentAnalyzer {

    @SystemMessage("너는 감정 분석가야.")
    Sentiment analyze(@UserMessage String text);
}
