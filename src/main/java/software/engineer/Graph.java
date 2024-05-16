package software.engineer;

import java.util.List;

/**
 * 图结构的接口
 * 目前实现：
 * -[x] 邻接矩阵的图结构
 * -[ ] 邻接表的图结构
 */
public interface Graph {
    public int size(); // 顶点数

    /* 顶点 */
    public void addVertex(String vertex);
    public List<String> getVertexes(); // 顶点列表V
    public int getVertex(String vertex); // vertex -> index
    public String getVertex(int index); // index -> vertex
    public List<String> getNeighbors(String v); // 邻居节点

    /* 边 */
    public void addEdge(String a, String b) throws Exception; // 添加边（边权重+1）
    public void addEdge(String a, String b, int value) throws Exception; // 设置边权重为 value
    public int getEdge(String a, String b);
    public List<Edge> getEdges(); // 边列表E


    /* 显示 */
    public void print();

    /* 图算法 */
    List<List<Object>> Dijkstra(String v); // [路径, 路径长度]
}
