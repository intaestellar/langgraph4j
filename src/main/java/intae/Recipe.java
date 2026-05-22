package intae;

import java.util.List;

public record Recipe(
        String name,
        List<String> ingredients,
        int cookingMinutes,
        String difficulty
) {
}
