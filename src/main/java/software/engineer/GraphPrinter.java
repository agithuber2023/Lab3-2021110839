package software.engineer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.jgrapht.Graph;
import org.jungrapht.visualization.decorators.EdgeShape;
import org.jungrapht.visualization.decorators.EllipseShapeFunction;
import org.jungrapht.visualization.decorators.IconShapeFunction;
import org.jungrapht.visualization.layout.algorithms.SugiyamaLayoutAlgorithm;
import org.jungrapht.visualization.renderers.Renderer;
import org.jungrapht.visualization.util.IconCache;


public class GraphPrinter extends JFrame {
    private Graph<String, String> graph;
    private org.jungrapht.visualization.VisualizationViewer<String, String> vv;

    private org.jungrapht.visualization.VisualizationViewer<String, String> configureVisualizationViewer(
            Graph<String, String> graph) {
        org.jungrapht.visualization.VisualizationViewer<String, String> vv =
                org.jungrapht.visualization.VisualizationViewer.builder(graph)
                        .layoutSize(new Dimension(700, 700))
                        .viewSize(new Dimension(700, 700))
                        .build();

        vv.getRenderContext().setEdgeShapeFunction(EdgeShape.line());
        vv.setVertexToolTipFunction(Object::toString);
        vv.getRenderContext().setArrowFillPaintFunction(n -> Color.lightGray);

        vv.getRenderContext().setVertexLabelPosition(org.jungrapht.visualization.renderers.Renderer.VertexLabel.Position.CNTR);

        // 设置顶点
        // vv.getRenderContext().setVertexLabelDrawPaintFunction(c -> Color.WHITE))
        vv.getRenderContext().setVertexFillPaintFunction(s -> Color.WHITE);
//        vv.getRenderContext().setVertexLabelFunction(Object::toString);
        vv.getRenderContext().setVertexLabelPosition(Renderer.VertexLabel.Position.CNTR);

        IconCache<String> iconCache =
                IconCache.<String>builder(Object::toString)
                        .vertexShapeFunction(vv.getRenderContext().getVertexShapeFunction())
                        .stylist(
                                (label, vertex, colorFunction) -> {
                                    label.setForeground(Color.black);
                                    label.setBackground(Color.white);
                                    Border lineBorder = BorderFactory.createEtchedBorder();
                                    Border marginBorder = BorderFactory.createEmptyBorder(4, 4, 4, 4);
                                    label.setBorder(new CompoundBorder(lineBorder, marginBorder));
                                })
                        .build();

        final IconShapeFunction<String> vertexImageShapeFunction =
                new IconShapeFunction<>(new EllipseShapeFunction<>());
        vertexImageShapeFunction.setIconFunction(iconCache);

        vv.getRenderContext().setVertexShapeFunction(vertexImageShapeFunction);
        vv.getRenderContext().setVertexIconFunction(iconCache);

        // 设置边
        vv.getRenderContext().setEdgeLabelFunction(s -> s.split("=")[1]);
        vv.getRenderContext().setEdgeStrokeFunction(p -> new BasicStroke(p.startsWith("[b]") ? 3f : 1.5f));
        return vv;
    }

    public GraphPrinter() {
        this.graph = null;
    }

    /**
     * 可视化显示图
     * https://github.com/tomnelson/jungrapht-visualization
     */
    public void draw(Graph<String, String> g) {

        this.graph = g;
        this.setTitle("Graph");
        this.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        this.setBackground(Color.white); // 设置窗口背景颜色

        JPanel container = new JPanel(new BorderLayout());

        vv = configureVisualizationViewer(graph);
        vv.getRenderContext().setEdgeShapeFunction(EdgeShape.line());

//        TreeLayoutSelector<String, Integer> treeLayoutSelector =
//                TreeLayoutSelector.<String, Integer>builder(vv)
//                        .initialSelection(2)
//                        .vertexShapeFunction(vv.getRenderContext().getVertexShapeFunction())
//                        .alignFavoredEdges(false)
//                        .columns(3)
//                        .build();

        SugiyamaLayoutAlgorithm<String, String> layoutAlgorithm =
                SugiyamaLayoutAlgorithm.<String, String>edgeAwareBuilder().build();
        layoutAlgorithm.setVertexBoundsFunction(vv.getRenderContext().getVertexBoundsFunction());
        vv.getVisualizationModel().setLayoutAlgorithm(layoutAlgorithm);

        container.add(vv.getComponent());

//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(container);

//        Box controls = Box.createHorizontalBox();
//        controls.add(ControlHelpers.getCenteredContainer("Layout Controls", treeLayoutSelector));
//        add(controls, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * 保存为图片
     * @throws IOException
     */
    public void save(String filename) throws IOException {
        Dimension size = vv.getComponent().getSize(); // 获取组件大小
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB); // 创建一个和组件一样大小的BufferedImage对象
        Graphics2D g2d = image.createGraphics(); // 创建Graphics2D对象
        vv.getComponent().paint(g2d);            // 将组件内容绘制到Graphics2D对象，也就是绘制到了BufferedImage对象
        g2d.dispose();                           // 释放Graphics2D对象
        ImageIO.write(image, "png", new File(filename));
    }
}