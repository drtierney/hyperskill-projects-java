package search;

import java.util.Map;
import java.util.Set;

public interface ISearch {
        Set<Integer> search(String query, Map<String, Set<Integer>> indexMap, int linesSize);

    default String[] queryToArray(String query) {
        return query.trim().toUpperCase().split("\\s+");
    }
}
