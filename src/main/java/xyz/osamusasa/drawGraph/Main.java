package xyz.osamusasa.drawGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.DoubleSupplier;

public class Main {

    public static void main(String args[]){
        JFrame frame = new JFrame("draw graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //from https://nat.hatenadiary.com/entry/20050513/p1
        //--
        java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        // 変数desktopBoundsにデスクトップ領域を表すRectangleが代入される
        java.awt.Rectangle desktopBounds = env.getMaximumWindowBounds();
        //--
        //windowサイズを画面の最大サイズに設定
        frame.setBounds(desktopBounds);

        int nodeRadius = 5;
        UndirectedGraph graph = UndirectedGraph.getRandomData(20,desktopBounds.width,desktopBounds.height);
        DynamicModel model = DynamicModel.defaultForceModel(
                desktopBounds.width,
                desktopBounds.height,
                graph.getNodeSize(),
                0.3,
                new DoubleSupplier() {
                    int i=0;
                    @Override
                    public double getAsDouble() {
                        return desktopBounds.height / 10.0 * Math.pow(Math.E, -(i++/10.0));
                    }
                }
        );

        Canvas canvas = new Canvas(){
            @Override
            public void paint(Graphics g) {
                graph.forEachEdges(e->{
                    g.setColor(Color.BLACK);
                    g.drawLine((int)e.n1.pos.getX(), (int)e.n1.pos.getY(), (int)e.n2.pos.getX(), (int)e.n2.pos.getY());
                });

                graph.forEachNode(e->{
                    g.setColor(Color.BLACK);
                    g.drawOval(
                            (int)e.pos.getX()-nodeRadius,
                            (int)e.pos.getY()-nodeRadius,
                            2*nodeRadius,
                            2*nodeRadius);
                    g.setColor(Color.WHITE);
                    g.fillOval(
                            (int)e.pos.getX()-nodeRadius,
                            (int)e.pos.getY()-nodeRadius,
                            2*nodeRadius,
                            2*nodeRadius);
                });
            }
        };
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()=='u'){
                    model.update(graph);
                    canvas.repaint();
                }
            }
        });

        frame.getContentPane().add(canvas);
        canvas.repaint();
        frame.setVisible(true);
    }
}
