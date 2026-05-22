package intae;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ChefAi {

    @SystemMessage("너는 한식 요리사야. JSON 객체로 답해.")
    Recipe createRecipe(@UserMessage String dish);

}
