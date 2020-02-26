package xyz.osamusasa.drawGraph;

import javax.swing.*;
import java.awt.*;

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
        Canvas canvas = new Canvas(){
            @Override
            public void paint(Graphics g) {
                UndirectedGraph graph = UndirectedGraph.getTestData();

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

        frame.getContentPane().add(canvas);
        canvas.repaint();
        frame.setVisible(true);
    }
}
