package xyz.osamusasa.drawGraph;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
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
    static UndirectedGraph getRandomData(int size, int w, int h){
        java.util.Random r = new java.util.Random();
        UndirectedGraph g = new UndirectedGraph();
        Node[] nodes = new Node[size];
        Set<Edge> edges = new HashSet<>();
        for(int i=0;i<size;i++){
            nodes[i] = g.new Node(String.valueOf(i), new Point(r.nextInt(w), r.nextInt(h)));
        }
        for(int i=0;i<size;i++){
            int j;
            do{
                j = r.nextInt(size);
            }while(i==j);
            edges.add(g.new Edge(nodes[i], nodes[j]));
        }
        for(int i=0;i<r.nextInt(size*size);i++){
            int j,k;
            do{
                j = r.nextInt(size);
                k = r.nextInt(size);
            }while (j==k);
            edges.add(g.new Edge(nodes[j], nodes[k]));
        }

        g.nodes = nodes;
        g.edges = edges.toArray(new Edge[0]);

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

    public int getNodeSize(){
        return nodes.length;
    }
    public boolean isConnected(Node n1, Node n2){
        boolean r[] = {false};
        forEachEdges(e->{
            if(e.equals(n1,n2)){
                r[0] = true;
            }
        });
        return r[0];
    }

    /**
     * 画面上でのノード間の距離を返す
     *
     * @param node1 ノード１
     * @param node2 ノード２
     * @return 画面上でのノード間の距離
     * @throws NoSuchElementException 対応するノードオブジェクトが無いとき
     */
    public int getScreenDistance(String node1, String node2){
        Node n1 = searchNodeFromName(node1);
        Node n2 = searchNodeFromName(node2);

        return getScreenDistance(n1, n2);
    }
    /**
     * 画面上でのノード間の距離を返す
     *
     * @param node1 ノード１
     * @param node2 ノード２
     * @return 画面上でのノード間の距離
     * @throws NoSuchElementException 対応するノードオブジェクトが無いとき
     */
    public int getScreenDistance(Node node1, Node node2){
        if(node1==null||node2==null){
            throw new NoSuchElementException();
        }

        return getPosDistance(node1, node2);
    }

    /**
     * ノードの名前から対応するノードオブジェクトを返す。
     *
     * @param name ノードの名前
     * @return 対応するノードオブジェクト
     * @throws NoSuchElementException 対応するノードオブジェクトが無いとき
     */
    private Node searchNodeFromName(String name){
        final Node[] ret = {null};
        forEachNode(n->{
            if(n.name.equals(name)){
                ret[0] = n;
            }
        });
        if(ret[0]!=null){
            return ret[0];
        }else {
            throw new NoSuchElementException();
        }
    }
    /**
     * 二つのノードの位置に対する距離を計算する
     *
     * @param n1 ノード１
     * @param n2 ノード２
     * @return ノード間の距離
     */
    private int getPosDistance(Node n1, Node n2){
        return (int)n1.pos.distance(n2.pos);
    }

    class Node{
        String name;
        Point2D pos;

        public Node(String name, Point2D pos) {
            this.name = name;
            this.pos = pos;
        }

        public void move(double dx, double dy){
            pos.setLocation(pos.getX()+dx, pos.getY()+dy);
        }
        public void move(Point2D dp){
            move(dp.getX(), dp.getY());
        }

        public boolean lessThan(Node n){
            if(equals(n))return false;
            return name.compareTo(n.name)<0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", pos=" + pos +
                    '}';
        }
    }
    class Edge{
        Node n1, n2;

        public Edge(Node n1, Node n2) {
            if(n1.equals(n2)){
                throw new IllegalArgumentException();
            }
            if(n1.lessThan(n2)){
                this.n1 = n1;
                this.n2 = n2;
            }else{
                this.n1 = n2;
                this.n2 = n1;
            }
        }

        public boolean equals(Node n1, Node n2){
            return (this.n1.equals(n1)&&this.n2.equals(n2))
                    ||(this.n1.equals(n2)&&this.n2.equals(n1));
        }
        @Override
        public String toString() {
            return "Edge{" +
                    "n1=" + n1 +
                    ", n2=" + n2 +
                    '}';
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(n1, edge.n1) &&
                    Objects.equals(n2, edge.n2);
        }
        @Override
        public int hashCode() {
            return Objects.hash(n1, n2);
        }
    }

    @Override
    public String toString() {
        return "UndirectedGraph{" +
                "edges=" + Arrays.toString(edges) +
                '}';
    }
}
