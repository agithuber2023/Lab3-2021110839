package software.engineer;


import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkSource;
import guru.nidi.graphviz.model.Node;
import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.graph;

/**
 * GUI窗口，用于展示有向图
 * 利用 Graphviz 生成图片并保存，读取图片展示在界面上
 */
class ImageFrame extends JFrame {

    public void draw(Graph graph, List<Edge> path, String filename) throws IOException {
        setTitle("Graph Display");

        // 移除所有组件
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.repaint();
        contentPane.revalidate();

        generateImage(graph, path, filename);

        // 使用Batik创建SVG画布
        JSVGCanvas canvas = new JSVGCanvas();
        canvas.setURI(filename);

        // 创建一个标签并设置画布
        JScrollPane scrollPane = new JScrollPane(canvas);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

        // 设置窗口大小
        setSize(new Dimension(500, 500));

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void generateImage(Graph graph, List<Edge> path, String filename) throws IOException {
        List<String> vertexes = graph.getVertexes();
        List<Edge> edges = graph.getEdges();
        Map<String, Node> nodes = vertexes.stream().collect(Collectors.toMap(vertex -> vertex, Factory::node, (a, b) -> b));
        LinkSource[] linkSources = new LinkSource[edges.size()];
        int index = 0;
        for (Edge edge:edges){
            linkSources[index++] = nodes.get(edge.from())
                    .link(
                            Link.to(nodes.get(edge.to()))
                                    .with(guru.nidi.graphviz.attribute.Label.of(Integer.
                                            toString(edge.value())), path.contains(edge)? guru.nidi.graphviz.attribute.Color.RED: Color.BLACK)
                    );
        }
        guru.nidi.graphviz.model.Graph g = graph("text").directed()
                .linkAttr().with("class", "link-class")
                .with(linkSources);
        Graphviz.fromGraph(g).render(Format.SVG).toFile(new File(filename));
    }
}
