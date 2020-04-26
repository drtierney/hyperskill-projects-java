package search;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class SearchNone implements ISearch {

    @Override
    public Set<Integer> search(String query, Map<String, Set<Integer>> indexMap, int linesSize) {
        String[] searchArr = queryToArray(query);
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < linesSize; i++) {
            indexes.add(i);
        }
        for (String word : searchArr) {
            if (indexMap.containsKey(word.toUpperCase())) {
                indexes.removeAll(indexMap.get(word.toUpperCase()));
            }

        }
        return indexes.size() == 0 ? null : indexes;
    }
}
