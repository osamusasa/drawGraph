package xyz.osamusasa.drawGraph;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

public class UndirectedGraph {
    private Node[] nodes;
    private Edge[] edges;

    static UndirectedGraph getTestData(){
        UndirectedGraph g = new UndirectedGraph();
        g.nodes = new Node[4];
        g.nodes[0] = g.new Node("A", new Point( 50, 50));
        g.nodes[1] = g.new Node("B", new Point(150, 50));
        g.nodes[2] = g.new Node("C", new Point( 50,150));
        g.nodes[3] = g.new Node("D", new Point(150,150));

        g.edges = new Edge[4];
        g.edges[0] = g.new Edge(g.nodes[0], g.nodes[1]);
        g.edges[1] = g.new Edge(g.nodes[1], g.nodes[2]);
        g.edges[2] = g.new Edge(g.nodes[2], g.nodes[3]);
        g.edges[3] = g.new Edge(g.nodes[3], g.nodes[0]);

        return g;
    }

    public void forEachNode(Consumer<Node> c){
        for(Node node : nodes) {
            c.accept(node);
        }
    }
    public void forEachEdges(Consumer<Edge> c){
        for(Edge edge : edges){
            c.accept(edge);
        }
    }

    class Node{
        String name;
        Point2D pos;

        public Node(String name, Point2D pos) {
            this.name = name;
            this.pos = pos;
        }
    }
    class Edge{
        Node n1, n2;

        public Edge(Node n1, Node n2) {
            this.n1 = n1;
            this.n2 = n2;
        }
    }
}
