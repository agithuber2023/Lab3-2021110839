package software.engineer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//        AdjMatrixGraph graph = new AdjMatrixGraph();
//        graph.addVertex("a");
//        graph.addVertex("b");
////        System.out.println(graph.getVertex().toString());
////        System.out.println(graph.getEdges().toString());
//        graph.print();
    }

    private Graph graph;
    private void initGraph() throws Exception {
        String[] arg = {"-f", "article.txt"};
        InputFile inputFile = null;
        inputFile = new InputFile(arg);
        String[] words = inputFile.getWords();
        graph = new AdjMatrixGraph();
        String previousWord = null;
        for (String word : words) {
            graph.addVertex(word);
            if (previousWord != null) {
                graph.addEdge(previousWord, word);
            }
            previousWord = word;
        }
    }
    public void testFindNeighbor() throws Exception {
        initGraph();
        System.out.println(graph.getNeighbors("to").toString());

    }

    public void testBridgeWord() throws Exception {
        initGraph();
        assertEquals("No bridge words from \"seek\" to \"to\"!", queryBridgeWords(graph, "seek", "to"));
        assertEquals("No bridge words from \"to\" to \"explore\"!", queryBridgeWords(graph, "to", "explore"));
        assertEquals("The bridge word from \"explore\" to \"new\" is: strange", queryBridgeWords(graph, "explore", "new"));
    }

    private static String queryBridgeWords(Graph g, String word1, String word2){
        List<String> results = new ArrayList<>();
        if (g.getVertexes().contains(word1) && g.getVertexes().contains(word2)){
            List<String> pos = g.getNeighbors(word1);
            for (String p : pos){
                if (g.getNeighbors(p).contains(word2))
                    results.add(p);
            }
        }else {
            return ("No \"" + word1 + "\" or \"" + word2 + "\" in the graph!");
        }

        // 输出提示词
        if (results.size() == 0){
            return ("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
        } else if (results.size() == 1) {
            return ("The bridge word from \"" + word1 + "\" to \"" + word2 + "\" is: "+results.get(0));
        } else {
            StringJoiner joiner = new StringJoiner(", ", "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ", ".");
            for (int i = 0; i < results.size(); i++) {
                if (i == results.size() - 1 && i != 0) joiner.add("and " + results.get(i));
                else joiner.add(results.get(i));
            }
            return (joiner.toString());
        }
    }
}
