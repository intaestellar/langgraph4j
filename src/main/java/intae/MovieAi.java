package intae;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface MovieAi {

    @SystemMessage("너는 영화 추천가야. JSON 배열로 답해.")
    MovieRecommendations recommend(@UserMessage String mood);
}
