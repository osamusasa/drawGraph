package xyz.osamusasa.drawGraph;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class UndirectedGraph {
    private Node[] nodes;
    private Edge[] edges;

    /**
     * 隣接するノードを指定してグラフを初期化する
     *
     * リストの先頭から2つごとに区切り、その組みをグラフの辺として追加する。
     * 辺を構成する全ての節点が、グラフの頂点集合になる。
     * 辺に接続していない節点は追加できない。
     *
     * @param n　隣接するノードを並べたリスト
     */
    public UndirectedGraph(Node... n){
        if(n.length%2!=0){
            throw new IllegalArgumentException(n[n.length-1]+" don't have pair.");
        }

        Set<Node> uniqueNodeList = new HashSet<>();
        edges = new Edge[n.length/2];

        for(int i=0;i<edges.length;i++){
            edges[i] = new Edge(n[2*i], n[2*i+1]);
            uniqueNodeList.add(n[2*i]);
            uniqueNodeList.add(n[2*i+1]);
        }
        nodes = uniqueNodeList.toArray(new Node[0]);
    }

    static UndirectedGraph getTestData(){
        UndirectedGraph g = new UndirectedGraph();
        g.nodes = new Node[4];

        Node a = g.new Node("A", new Point( 50, 50));
        Node b = g.new Node("B", new Point(150, 50));
        Node c = g.new Node("C", new Point( 50,150));
        Node d = g.new Node("D", new Point(150,150));

        return new UndirectedGraph(
                a,b,
                b,c,
                c,d,
                d,a
        );
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name) &&
                    Objects.equals(pos, node.pos);
        }
        @Override
        public int hashCode() {
            return Objects.hash(name, pos);
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
