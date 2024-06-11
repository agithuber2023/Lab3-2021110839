package software.engineer;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CalcShortestPathTest extends TestCase {

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

    private static void showDirectedGraph(Graph g, List<Edge> path, String filename) throws IOException {
        imageFrame.draw(g, path, filename);
    }

    private static String calcShortestPath(Graph graph, String word1, String word2) throws IOException{
        List<List<Object>> paths = graph.Dijkstra(word1);
        if (paths == null) {
            if (graph.getVertexes().contains(word2)) {
                return "\"" + word1 + "\" is not exist";
            }
            else {
                return "\"" + word1 + "\" and \"" + word2 + "\" don't exist";
            }
        }
//        if (paths == null) return "\"" + word1 + "\" is not exist";
        if (!graph.getVertexes().contains(word2)) return "\"" + word2 + "\" is not exist";
        List<Object> res = paths.get(graph.getVertex(word2));

        // 突出显示路径
        List<Edge> path = new ArrayList<>();
        String[] nodes = ((String) res.get(0)).split("-->");
        String preword = null;
        for (String node : nodes){
            if (preword != null) path.add(new Edge(preword, node, graph.getEdge(preword, node)));
            preword = node;
        }
//        showDirectedGraph(graph, path, "graph_path.svg");
        System.out.println("(" + res.get(1) + "): " + res.get(0));
        return ("(" + res.get(1) + "): " + res.get(0));
    }

    @Before
    public void setup() throws Exception{
        initGraph();
    }

    @Test
    public void testCalcShortPath1() throws Exception{
        initGraph();
        assertEquals("(2): to-->seek-->out", calcShortestPath(graph, "to", "out"));
    }

    @Test
    public void testCalcShortPath2() throws Exception{
        initGraph();
        assertEquals("(-1): our -x strange 不可达", calcShortestPath(graph, "our", "strange"));
    }

    @Test
    public void testCalcShortPath3() throws Exception{
        initGraph();
        assertEquals("\"apple\" is not exist", calcShortestPath(graph, "apple", "new"));
    }

    public void testCalcShortPath4() throws Exception{
        initGraph();
        assertEquals("\"orange\" is not exist", calcShortestPath(graph, "world", "orange"));
    }

    @Test
    public void testCalcShortPath5() throws Exception{
        initGraph();
        assertEquals("\"beef1\" and \"pork2\" don't exist", calcShortestPath(graph, "beef1", "pork2"));
    }

    @Test
    public void testCalcShortPath6() throws Exception{
        initGraph();
        assertEquals("(0): to-->to", calcShortestPath(graph, "to", "to"));
    }
}