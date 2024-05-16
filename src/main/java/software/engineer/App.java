package software.engineer;

import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

import java.io.*;
import java.util.*;


public class App
{
    private static Graph graph;
    private static final GraphPrinter graphPrinter = new GraphPrinter();

    /**
     * 主程序入口，接收用户输入文件，生成图，并允许用户选择后续各项功能
     * @param args -f 或者 --file 指定输入文件路径
     */
    public static void main(String[] args) throws Exception {
        InputFile inputFile = new InputFile(args);
        String[] words = inputFile.getWords();
        graph = buildGraph(words);
        //graph.print();
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean flag = true;
        while (flag) {
            System.out.println("====================");
            System.out.println("请选择后续功能：");
            System.out.println("1. 展示有向图");
            System.out.println("2. 查询桥接词");
            System.out.println("3. 根据桥接词生成新文本");
            System.out.println("4. 计算两个单词之间的最短路径");
            System.out.println("5. 随机游走");
            System.out.println("0. 退出");

            int choice;
            while (!(scanner.hasNextInt())) {
                scanner.next();
                System.out.println("请输入一个数字：");
            }
            choice=scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> showDirectedGraph(graph);
                case 2 -> {
                    System.out.println("请输入两个单词(" + graph.getVertexes().toString() + "): ");
                    input = scanner.nextLine();
                    words = input.split("\\s+");
                    while (words.length < 2) {
                        System.out.println("请输入『两个』单词");
                        input = scanner.nextLine();
                        words = input.split("\\s+");
                    }
                    queryBridgeWords(words[0], words[1], true);
                }
                case 3 -> {
                    System.out.println("请输入一个句子");
                    input = scanner.nextLine();
                    System.out.println(generateNewText(input));
                }
                case 4 -> {
                    System.out.println("请输入一个或者两个单词(" + graph.getVertexes().toString() + "): ");
                    input = scanner.nextLine();
                    words = input.split("\\s+");
                    while (words.length < 1) {
                        System.out.println("请输入『一个或者两个』单词");
                        input = scanner.nextLine();
                        words = input.split("\\s+");
                    }
                    if (words.length == 1) System.out.println(calcShortestPath(words[0]));
                    else System.out.println(calcShortestPath(words[0], words[1]));
                }
                case 5 -> {
                    String randomwalk = randomWalk();
                    System.out.println(randomwalk);
                    stringToFile("randomWalk.txt", randomwalk);
                }
                case 0 -> {
                    flag = false;
                }
                default -> System.out.println("无效选择，请重新输入");
            }
        }
        System.exit(0);
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

    /**
     * 展示生成的有向图
     * ✅ 可选功能：将生成的有向图以图形文件形式保存到磁盘，可以调用外部
     * 绘图库或绘图工具API自动生成有向图，但不能采用手工方式绘图
     * @param g 有向图
     * @param path 突出标注路径
     * @param filename 保存文件名
     */
    private static void showDirectedGraph(Graph g, List<Edge> path, String filename) throws IOException {
        org.jgrapht.Graph<String, String> graph;
        graph = GraphTypeBuilder.<String, String>directed()
                .edgeSupplier(SupplierUtil.createStringSupplier())
                .vertexSupplier(SupplierUtil.createStringSupplier())
                .buildGraph();
        List<String> vertexes = g.getVertexes();
        List<Edge> edges = g.getEdges();
        for (String vertex : vertexes) graph.addVertex(vertex);
        for (Edge edge : edges) {
            if (path.contains(edge)) graph.addEdge(edge.getFrom(), edge.getTo(), "[b]"+edge);
            else graph.addEdge(edge.getFrom(), edge.getTo(), edge.toString());
        }
//        System.out.println("The graph: \n" + graph);

        graphPrinter.draw(graph);
        graphPrinter.save(filename);
    }

    private static void showDirectedGraph(Graph g) throws IOException {
        showDirectedGraph(g, new ArrayList<>(), "graph.png");
    }

    /**
     * 在生成有向图之后，用户输入任意两个英文单词word1、word2，程
     * 序从图中查询它们的“桥接词”。
     * @param word1 单词1
     * @param word2 单词2
     * @param message 是否显示提示信息（功能3需要显示。功能4复用函数时不显示）
     * @return 桥接词列表
     */
    private static List<String> queryBridgeWords(String word1, String word2, boolean message){
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

    private static List<String> queryBridgeWords(String word1, String word2) {
        return queryBridgeWords(word1, word2, false);
    }

    /**
     * 用户输入一行新文本，程序根据之前输入文件生
     * 成的图，计算该新文本中两两相邻的单词的
     * bridge word，将bridge word插入新文本的两个
     * 单词之间，输出到屏幕上展示。
     * *********************
     * 测试输入：Seek to explore new and exciting synergies
     * 预期输出：Seek to explore strange new life and exciting synergies
     * *********************
     *  @param inputText 用户输入的一行新文本
     * @return 新生成的字符串
     */
    private static String generateNewText(String inputText) {
        String filter_non_alphabet = inputText.replaceAll("[^A-Za-z]", " "); // 将非字母字符替换为空格
        String[] words = filter_non_alphabet.split("\\s+"); // 分割处理后的文本
        String preword = null;
        StringBuilder result = new StringBuilder();
        for (String word : words){
            if (preword != null){
                List<String> bridges = queryBridgeWords(preword, word.toLowerCase());
                if (bridges.size() == 1) result.append(bridges.get(0)).append(" ");
                else if (bridges.size() > 1) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(bridges.size());
                    result.append(bridges.get(randomIndex)).append(" ");
                }
            }
            result.append(word).append(" ");
            preword = word.toLowerCase();
        }
        return result.toString();
    }

    /**
     * 用户输入两个单词，程序计算它们之间在图中的
     * 最短路径（路径上所有边权值之和最小），以某
     * 种突出的方式将路径标注在原图并展示在屏幕上
     * ，同时展示路径的长度（所有边权值之和）。
     * *********************
     * ✅ 可选功能：如果用户只输入一个单词，则程序计
     * 算出该单词到图中其他任一单词的最短路径，并
     * 逐项展示出来。
     * *********************
     * @param word1 单词1
     * @param word2 单词2
     * @return 最短路径的字符串
     */
    private static String calcShortestPath(String word1, String word2) throws IOException {
        List<List<Object>> paths = graph.Dijkstra(word1);
        if (paths == null) return "\"" + word1 + "\" is not exist";
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
        showDirectedGraph(graph, path, "graph_path.png");
        return ("(" + res.get(1) + "): " + res.get(0));
    }

    private static String calcShortestPath(String word1) {
        List<List<Object>> paths = graph.Dijkstra(word1);
        if (paths == null) return "\"" + word1 + "\" is not exist";
        StringBuilder res = new StringBuilder();
        for (List<Object> path : paths){
            res.append("(").append(path.get(1)).append("): ").append(path.get(0)).append("\n");
        }
        return res.toString();
    }

    /**
     * 进入该功能时，程序随机的从图中选择一个节
     * 点，以此为起点沿出边进行随机遍历，记录经
     * 过的所有节点和边，直到出现第一条重复的边
     * 为止，或者进入的某个节点不存在出边为止。
     * 在遍历过程中，用户也可随时停止遍历。
     * *********************
     * @return 随机路径的字符串
     */
    private static String randomWalk() {
        boolean[] visited = new boolean[graph.size()];
        // 随机起点
        Random random = new Random();
        int randomIndex = random.nextInt(graph.size());
        String v = graph.getVertex(randomIndex);

        StringBuilder path = new StringBuilder(v);
        visited[randomIndex] = true;
        List<String> neighbors;

        while ((neighbors = graph.getNeighbors(v)) != null) {
            // 随机选择邻居
            randomIndex = random.nextInt(graph.size());
            while (!neighbors.contains(graph.getVertex(randomIndex))) randomIndex = random.nextInt(graph.size());

            path.append("-->").append(graph.getVertex(randomIndex));
            if (visited[randomIndex]) break;
            else visited[randomIndex] = true;
            v = graph.getVertex(randomIndex);
        }
        return path.toString();
    }

    private static void stringToFile(String filepath, String content) {
        try {
            PrintWriter out = new PrintWriter(filepath);
            out.print(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
