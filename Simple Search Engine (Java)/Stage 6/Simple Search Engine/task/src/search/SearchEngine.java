package search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchEngine {
    private ISearch iSearch;
    private List<String> rows;
    private Map<String, Set<Integer>> indexMap;

    public SearchEngine(List<String> rows) {
        this.rows = rows;
        indexMap = new HashMap<>();
        createIndexMap();
    }

    private void createIndexMap() {
        indexMap.clear();
        for (int i = 0; i < rows.size(); i++) {
            for (String word : rows.get(i).split(",|\\s+")) {
                word = word.toUpperCase();
                if (indexMap.containsKey(word)) {
                    indexMap.get(word).add(i);
                } else {
                    indexMap.put(word, new HashSet<>(Set.of(i)));
                }
            }
        }
    }

    public String search(String query) {
        StringBuilder output = new StringBuilder();
        var indexes = iSearch.search(query, indexMap, rows.size());
        if (indexes == null) {
            output.append("No matching rows found.");
        } else {
            for (int i : indexes) {
                output.append(rows.get(i)).append("\n");
            }
        }
        return output.toString();
    }

    public void setSearchStrategy(ISearch searchStrategy) {
        this.iSearch = searchStrategy;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("\n=== List of rows ===\n");
        for (String line : rows) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

}
