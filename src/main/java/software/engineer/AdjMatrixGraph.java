package software.engineer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 邻接矩阵数据结构的图实现
 */
class AdjMatrixGraph implements Graph{
    private final List<String> vertexes;
    private int[][] edges;
    private int size;

    public AdjMatrixGraph(String[] vertexes, int[][] edges) {
        this.vertexes = new ArrayList<>(Arrays.asList(vertexes));
        this.edges = edges;
    }
    public AdjMatrixGraph(String[] vertexes) {
        this.vertexes = new ArrayList<>(Arrays.asList(vertexes));
        this.size = this.vertexes.size();
        this.edges = new int[this.size][this.size];
    }
    public AdjMatrixGraph(){
        this.vertexes = new ArrayList<>();
        this.size = 0;
        this.edges = new int[0][];
    }
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void addVertex(String vertex) {
        this.vertexes.add(vertex);
        int[][] newEdges = new int[this.size+1][this.size+1];
        for (int i=0; i<this.size; i++){
            System.arraycopy(this.edges[i], 0, newEdges[i], 0, this.size);
        }
        this.edges = newEdges;
        this.size++;
    }

    @Override
    public List<String> getVertexes() {
        return vertexes;
    }

    @Override
    public List<String> getNeighbors(String v) {
        List<String> results = new ArrayList<>();
        if (vertexes.contains(v)){
            int index = vertexes.indexOf(v);
            for (int i=0; i<this.size; i++){
                if (this.edges[index][i] > 0) results.add(vertexes.get(i));
            }
        }
        return results;
    }

    @Override
    public int getVertex(String vertex) {
        return vertexes.indexOf(vertex);
    }

    @Override
    public String getVertex(int index) {
        if (index < this.size && index >=0)
            return vertexes.get(index);
        else {
            throw new IndexOutOfBoundsException(index);
        }
    }


    @Override
    public void addEdge(String a, String b, int value) throws Exception {
        if (vertexes.contains(a) && vertexes.contains(b)){
            int index_a = vertexes.indexOf(a);
            int index_b = vertexes.indexOf(b);
            this.edges[index_a][index_b] = value;
        }
        else {
            throw new Exception("Vertex is not exist");
        }
    }

    @Override
    public void addEdge(String a, String b) throws Exception {
        if (vertexes.contains(a) && vertexes.contains(b)){
            int index_a = vertexes.indexOf(a);
            int index_b = vertexes.indexOf(b);
            this.edges[index_a][index_b]++;
        }
        else {
            throw new Exception("Vertex is not exist");
        }
    }

    @Override
    public List<Edge> getEdges() {
        List<Edge> results = new ArrayList<>();
        for (int i=0; i<this.size; i++){
            for (int j=0; j<this.size; j++){
                if (edges[i][j] > 0)
                    results.add(new Edge(vertexes.get(i), vertexes.get(j), edges[i][j]));
            }
        }
        return results;
    }

    @Override
    public int getEdge(String a, String b) {
        if (vertexes.contains(a) && vertexes.contains(b)) {
            int index_a = vertexes.indexOf(a);
            int index_b = vertexes.indexOf(b);
            return edges[index_a][index_b];
        }
        else {
            return -1;
        }
    }

    @Override
    public void print() {
        System.out.println("Adjacency Matrix:");
        System.out.println(this.vertexes.toString());
        for (int[] row : this.edges) {
            System.out.println(Arrays.toString(row));
        }
    }

    private int distance(int[][] dis, int i, int j){
        if (dis[i][j] > 0) return dis[i][j];
        else return 10000000;
    }

    @Override
    public List<List<Object>> Dijkstra(String v) {
        if (!this.vertexes.contains(v)) return null;
        int start = vertexes.indexOf(v);
        int[] visit = new int[this.size];
        int[] bestmin = new int[this.size];
        String[] path = new String[this.size];
        int max = 10000000;
        int[][] dis = new int[this.size][this.size];
        for(int i=0; i<this.size; i++) dis[i]=Arrays.copyOf(edges[i], this.size);
        visit[start] = 1;
        bestmin[start] = 0;

        //大循环（搞定这里就算搞定该算法了，后面的输出什么的可以不看）
        for(int l = 0; l < this.size; l++) {
            int Dtemp = max;
            int k = -1;

            //步骤① 找出与源点距离最短的那个点，即遍历distance[1][1]，distance[1][2],.....distance[1][N]中的最小值
            for(int i = 0; i < this.size; i++) {
                if(visit[i] == 0 && distance(dis, start, i) < Dtemp) {
                    Dtemp = distance(dis, start, i);
                    k = i;
                }
            }
            if (k == -1) continue;
            visit[k] = 1;
            bestmin[k] = Dtemp;

            //步骤② 松弛操作
            for(int i = 0; i < this.size; i++) {
                if(visit[i] == 0 && (distance(dis, start, k) + distance(dis, k, i)) < distance(dis, start, i)) {
                    dis[start][i] = distance(dis, start, k) + distance(dis, k, i);
                    path[i] = (path[k]==null?(v+"-->"+ vertexes.get(k)):path[k]) + "-->" + vertexes.get(i);
                }
                if (path[i] == null && (bestmin[i] > 0 || i==start)) path[i] = v+"-->"+ vertexes.get(i);
            }
        }

        //输出路径
        List<List<Object>> results = new ArrayList<>();
        for(int i=0; i<this.size; i++) {
            List<Object> t = new ArrayList<>();
            t.add(path[i]!=null?path[i]:v+" -x "+ vertexes.get(i)+" 不可达");
            t.add(path[i]!=null?bestmin[i]:-1);
            results.add(t);
        }
        return results;
    }
}
