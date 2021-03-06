package xyz.osamusasa.drawGraph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;
import java.util.function.IntUnaryOperator;

public class DynamicModel {
    private double tParam;
    private DoubleSupplier tParamSupplier;
    private final IntUnaryOperator attractionForce;
    private final IntUnaryOperator repulsionForce;

    /**
     * コンストラクタ
     *
     * @param f_a 引力
     * @param f_r 斥力
     * @param t 温度パラメータ
     */
    public DynamicModel(IntUnaryOperator f_a, IntUnaryOperator f_r, DoubleSupplier t){
        tParam = t.getAsDouble();
        tParamSupplier = t;
        attractionForce = f_a;
        repulsionForce = f_r;
    }

    /**
     * デフォルトの引力・斥力の式で初期化
     *
     * @param width 画面の横幅
     * @param height 画面の縦幅
     * @param n 頂点の数
     * @param c 弾性係数
     * @param t 温度パラメータ
     * @return 力学モデルのオブジェクト
     */
    public static DynamicModel defaultForceModel(int width, int height, int n, double c, DoubleSupplier t){
        double k = c * Math.sqrt(width * height /n);
        System.out.println("k:"+k);
        return new DynamicModel(d->(int)(d*d/k), d->(int)(-k*k/d), t);
    }

    /**
     * アルゴリズムを１回計算し、グラフの位置を更新する
     *
     * 引数の無向グラフの位置も更新される
     *
     * @param g 無向グラフ
     * @return 更新した無向グラフ
     */
    public UndirectedGraph update(UndirectedGraph g){
        Map<UndirectedGraph.Node, Vector2D> force = new HashMap<>(g.getNodeSize());

        //各頂点に作用する力を計算
        g.forEachNode(n->{
            force.put(n,Vector2D.ZERO);
            g.forEachNode(m->{
                if(n.equals(m)){
                    return;
                }
                int d = g.getScreenDistance(n,m);
                Vector2D u = Vector2D.getUnitVector(n.pos, m.pos);
                if(g.isConnected(n,m)){
                    force.put(
                            n,
                            force.get(n).add(u.mul(attractionForce.applyAsInt(d)))
                    );
                }
                force.put(
                        n,
                        force.get(n).add(u.mul(repulsionForce.applyAsInt(d)))
                );
            });
        });

        //計算した力を元に頂点の位置を更新
        g.forEachNode(n->{
            Vector2D v = force.get(n);
            double dx = tParam > Math.abs(v.x)
                    ?v.x
                    :v.x > 0
                        ?tParam
                        :-tParam;
            double dy = tParam > Math.abs(v.y)
                    ?v.y
                    :v.y > 0
                        ?tParam
                        :-tParam;
            n.move(dx, dy);
        });

        tParam = tParamSupplier.getAsDouble();
        System.out.println("t param:"+tParam);

        return g;
    }
}
class Vector2D{
    double x,y;

    static Vector2D ZERO = new Vector2D(0.0,0.0);

    Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void setSize(double l){
        double coef = l / getSize();
        x *= coef;
        y *= coef;
    }
    double getSize(){
        return Math.sqrt(x*x+y*y);
    }

    Vector2D add(Vector2D v){
        return new Vector2D(x+v.x, y+v.y);
    }

    Vector2D mul(double c){
        return new Vector2D(x*c, y*c);
    }

    static Vector2D getUnitVector(Point2D base, Point2D direction){
        Vector2D v = new Vector2D(direction.getX()-base.getX(),direction.getY()-base.getY());
        v.setSize(1);
        return v;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}