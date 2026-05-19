package intae.tools;

import dev.langchain4j.agent.tool.Tool;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class WikipediaTools {

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_1_1)   // 일관성 위해
            .build();

    private final String lang;       // "ko", "en" 등
    private final int maxChars;      // 너무 길면 컨텍스트 낭비

    public WikipediaTools()                        { this("ko", 1200); }
    public WikipediaTools(String lang, int max)    { this.lang = lang; this.maxChars = max; }

    @Tool("주어진 제목으로 한국어 Wikipedia 문서의 요약을 가져옵니다. 사람·장소·개념 등 명확한 주제어를 인자로 주세요.")
    public String summarize(String title) {
        try {
            String url = "https://%s.wikipedia.org/api/rest_v1/page/summary/%s"
                    .formatted(lang, URLEncoder.encode(title, StandardCharsets.UTF_8));

            HttpResponse<String> res = http.send(
                    HttpRequest.newBuilder(URI.create(url))
                            .header("User-Agent", "langgraph4j-study (https://example.com)")
                            .GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() == 404) return "해당 제목의 문서를 찾을 수 없습니다: " + title;
            if (res.statusCode() != 200) return "위키 요청 실패(" + res.statusCode() + ")";

            // 매우 단순한 추출: JSON 파서를 쓰면 깔끔하지만, 데모로 정규식
            String body = res.body();
            String extract = pick(body, "\"extract\":\"", "\"");
            return extract.isBlank() ? body : truncate(unescape(extract));
        } catch (Exception e) {
            return "오류: " + e.getMessage();
        }
    }

    private String truncate(String s) {
        return s.length() <= maxChars ? s : s.substring(0, maxChars) + "…";
    }

    private static String pick(String src, String start, String end) {
        int i = src.indexOf(start);
        if (i < 0) return "";
        int j = src.indexOf(end, i + start.length());
        return j < 0 ? "" : src.substring(i + start.length(), j);
    }

    private static String unescape(String s) {
        return s.replace("\\\"", "\"").replace("\\n", "\n").replace("\\\\", "\\");
    }
}
