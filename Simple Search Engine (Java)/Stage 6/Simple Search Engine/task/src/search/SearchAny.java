package search;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class SearchAny implements ISearch {

    @Override
    public Set<Integer> search(String query, Map<String, Set<Integer>> indexMap, int linesSize) {
        String[] searchArr = queryToArray(query);
        Set<Integer> indexes = new HashSet<>();
        for(String word : searchArr){
            if(indexMap.containsKey(word.toUpperCase())){
                indexes.addAll(indexMap.get(word.toUpperCase()));
            }
        }
        return indexes;
    }
}
