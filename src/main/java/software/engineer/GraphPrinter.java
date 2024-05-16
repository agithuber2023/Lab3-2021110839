package software.engineer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


public class GraphPrinter extends JFrame {
    private SparseGraph g;
    private VisualizationViewer<String, String> vv;

    private static SparseGraph initGraph() {
        SparseGraph graph = new SparseGraph();
        for (int i = 1; i < 10; i++) {
            graph.addVertex(i);
            graph.addEdge(Integer.toString(i), 1, i + 1, EdgeType.DIRECTED);
        }
        System.out.println("The graph: \n" + graph.toString());
        return graph;
    }

    public GraphPrinter() {
        this.g = new SparseGraph();
    }

    /**
     * 可视化显示图
     * https://blog.csdn.net/sunquan291/article/details/81487141
     */
    public void draw(SparseGraph g) {
        this.g = g;
        this.setTitle("Graph");
        this.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        this.setBackground(Color.white);// 设置窗口背景颜色

        //创建viewer 圆形布局结构(V,E节点和链路类型)
        vv = new VisualizationViewer<String, String>(new FRLayout2<>(g));

        // 设置顶点文本标签
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        // 设置顶点颜色
        vv.getRenderContext().setVertexFillPaintTransformer(s -> Color.WHITE);

        // 设置边的文本标签
        vv.getRenderContext().setEdgeLabelTransformer(s -> s.split("=")[1]);
        // 设置边的线型
        vv.getRenderContext().setEdgeStrokeTransformer(p -> {
//            System.out.println(p);
            if (p.startsWith("[b]")) return new BasicStroke(3f);
            else return new BasicStroke(1f);
        });

        DefaultModalGraphMouse<Integer, String> gm = new DefaultModalGraphMouse<Integer, String>();
        gm.setMode(Mode.PICKING);
        vv.setGraphMouse(gm);
        // 将上述对象放置在一个Swing容器中并显示之
        getContentPane().add(vv);
        pack();

//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    /**
     * 保存为图片
     * https://stackoverflow.com/questions/10420779/jung-save-whole-graph-not-only-visible-part-as-image
     * @throws IOException
     */
    public void save(String filename) throws IOException {
        // Create the VisualizationImageServer
        // vv is the VisualizationViewer containing my graph
        VisualizationImageServer<String, String> vis =
                new VisualizationImageServer<String, String>(vv.getGraphLayout(),
                        vv.getGraphLayout().getSize());


        vis.setBackground(Color.WHITE);

        // 设置顶点文本标签
        vis.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        // 设置顶点颜色
        vis.getRenderContext().setVertexFillPaintTransformer(s -> Color.WHITE);

        // 设置边的文本标签
        vis.getRenderContext().setEdgeLabelTransformer(s -> s.split("=")[1]);
        // 设置边的线型
        vis.getRenderContext().setEdgeStrokeTransformer(p -> {
//            System.out.println(p);
            if (p.startsWith("[b]")) return new BasicStroke(3f);
            else return new BasicStroke(1f);
        });

        // Create the buffered image
        BufferedImage image = (BufferedImage) vis.getImage(
                new Point2D.Double(vv.getGraphLayout().getSize().getWidth() / 2,
                        vv.getGraphLayout().getSize().getHeight() / 2),
                new Dimension(vv.getGraphLayout().getSize()));

        // Write image to a png file
        File outputfile = new File(filename);

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            // Exception handling
        }
    }

    public void save() throws IOException {
        this.save("graph.png"); // 默认存储路径
    }

    public static void main(String[] args) {
        SparseGraph g = initGraph();
        GraphPrinter frame = new GraphPrinter();
        frame.draw(g);
    }
}