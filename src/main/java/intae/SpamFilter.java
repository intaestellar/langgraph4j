package intae;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SpamFilter {

    @SystemMessage("다음 이메일이 스팸인지 답하시오.")
    boolean isSpam(@UserMessage String email);
}
