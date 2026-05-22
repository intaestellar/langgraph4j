package intae;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface JavaTutor {

    @SystemMessage("너는 친절한 자바 강사야. 짧게 답해.")
    String ask(@MemoryId String userId, @UserMessage String question);


}
