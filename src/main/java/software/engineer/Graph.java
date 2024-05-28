package software.engineer;

import java.util.List;


/**
 * 图结构的接口
 * 后期可能需要用不同的数据结构实现程序：
 * -[x] 邻接矩阵的图结构（稠密图）
 * -[ ] 邻接表的图结构（稀疏图）
 */
interface Graph {
    int size(); // 顶点数

    /* 顶点 */
    void addVertex(String vertex);
    List<String> getVertexes(); // 顶点列表V
    int getVertex(String vertex); // vertex -> index
    String getVertex(int index); // index -> vertex
    List<String> getNeighbors(String v); // 邻居节点

    /* 边 */
    void addEdge(String a, String b) throws Exception; // 添加边（边权重+1）
    void addEdge(String a, String b, int value) throws Exception; // 设置边权重为 value
    int getEdge(String a, String b);
    List<Edge> getEdges(); // 边列表E


    /* 显示 */
    void print();

    /* 图算法 */
    List<List<Object>> Dijkstra(String v); // [路径, 路径长度]
}