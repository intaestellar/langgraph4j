package intae.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.Tool;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PokemonTools {

    private final ObjectMapper mapper = new ObjectMapper();

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_1_1)   // 일관성 위해
            .build();

    private final int maxChars;      // 너무 길면 컨텍스트 낭비

    public PokemonTools() {
        this(1200);
    }

    public PokemonTools(int max) {
        this.maxChars = max;
    }

    @Tool("포켓몬의 타입을 가져옵니다.")
    public String getPokemonType(String pokemon) {

        System.out.println("pokemon : " + pokemon);
        try {
            String url = "https://pokeapi.co/api/v2/pokemon/%s"
                    .formatted(pokemon.toLowerCase());

            HttpResponse<String> res = http.send(
                    HttpRequest.newBuilder(URI.create(url))
                            .header("User-Agent", "langgraph4j-study (https://example.com)")
                            .GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() == 404) return "해당 제목의 문서를 찾을 수 없습니다: " + pokemon;
            if (res.statusCode() != 200) return "위키 요청 실패(" + res.statusCode() + ")";

            JsonNode root = mapper.readTree(res.body());
            List<String> types = new ArrayList<>();
            for (JsonNode t : root.path("types")) {
                types.add(t.path("type").path("name").asText());
            }
            return String.join(", ", types);
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
