package software.engineer;

import com.kitfox.svg.A;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class QueryBridgeWordsTest extends TestCase {
    private Graph graph;

    private static final ImageFrame imageFrame = new ImageFrame();

    private void initGraph() throws Exception {
        String[] arg = {"-f", "article.txt"};
        InputFile inputFile = null;
        inputFile = new InputFile(arg);
        String[] words = inputFile.getWords();
        graph = buildGraph(words);
    }

    private static Graph buildGraph(String[] words) throws Exception {
        Set<String> set = new HashSet<>(Arrays.asList(words));
        Graph graph = new AdjMatrixGraph(set.toArray(new String[0]));
        String previousWord = null;
        for (String word : words) {
            if (previousWord != null) {
                graph.addEdge(previousWord, word);
            }
            previousWord = word;
        }
        return graph;
    }

    private static List<String> queryBridgeWords(Graph graph, String word1, String word2, boolean message){
        List<String> results = new ArrayList<>();
        if (graph.getVertexes().contains(word1) && graph.getVertexes().contains(word2)){
            List<String> pos = graph.getNeighbors(word1);
            for (String p : pos){
                if (graph.getNeighbors(p).contains(word2))
                    results.add(p);
            }
        }
        else if(message){
            System.out.println("No \"" + word1 + "\" or \"" + word2 + "\" in the graph!");
            return results;
        }

        // 输出提示词
        if (message){
            if (results.size() == 0){
                System.out.println("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
            } else if (results.size() == 1) {
                System.out.println("The bridge word from \"" + word1 + "\" to \"" + word2 + "\" is: "+results.get(0));
            } else {
                StringJoiner joiner = new StringJoiner(", ", "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ", ".");
                for (int i = 0; i < results.size(); i++) {
                    if (i == results.size() - 1) joiner.add("and " + results.get(i));
                    else joiner.add(results.get(i));
                }
                System.out.println(joiner);
            }
        }
        return results;
    }

    @Before
    public void setup() throws Exception{
        initGraph();
    }

    @Test
    public void testQueryBridgeWords1() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>(0);
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "update", "more", false);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords2() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>(0);
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "new", "more", false);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords3() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>(0);
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "discover", "world", true);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords4() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>(0);
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "world", "and", false);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords5() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>();
        list_a.add("out");
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "seek", "new", false);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords6() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>();
        list_a.add("explore");
        list_a.add("discover");
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "to", "strange", false);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords7() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>();
        list_a.add("seek");
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "to", "out", true);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords8() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>();
        list_a.add("explore");
        list_a.add("discover");
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "to", "strange", true);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }

    @Test
    public void testQueryBridgeWords9() throws Exception{
        initGraph();
        List<String> list_a = new ArrayList<>();
        Collections.sort(list_a);
        List<String> list_b = queryBridgeWords(graph, "orange", "strange", true);
        Collections.sort(list_b);
        assertEquals(list_a, list_b);
    }
}