package rip.dfg;
import java.util.ArrayList;

public class Geom {



    public static boolean doIntersect(DoublePoint p1, DoublePoint q1, DoublePoint p2, DoublePoint q2)
    {

        return Geometry.doLinesIntersect(new LineSegment(p1,q1),new LineSegment(p2,q2));
    }
    public static boolean doIntersect(DoublePoint p1, DoublePoint q1, DoubleRectangle rectangle){
        DoublePoint p2,q2;

        p2 = rectangle.getAngle(0);
        q2 = rectangle.getAngle(1);
        if(doIntersect(p1,q1,p2,q2)) return true;
        p2 = rectangle.getAngle(1);
        q2 = rectangle.getAngle(0);
        if(doIntersect(p1,q1,p2,q2)) return true;

        p2 = rectangle.getAngle(1);
        q2 = rectangle.getAngle(2);
        if(doIntersect(p1,q1,p2,q2)) return true;
        p2 = rectangle.getAngle(2);
        q2 = rectangle.getAngle(1);
        if(doIntersect(p1,q1,p2,q2)) return true;

        p2 = rectangle.getAngle(2);
        q2 = rectangle.getAngle(3);
        if(doIntersect(p1,q1,p2,q2)) return true;
        p2 = rectangle.getAngle(3);
        q2 = rectangle.getAngle(2);
        if(doIntersect(p1,q1,p2,q2)) return true;

        p2 = rectangle.getAngle(3);
        q2 = rectangle.getAngle(0);
        if(doIntersect(p1,q1,p2,q2)) return true;
        p2 = rectangle.getAngle(0);
        q2 = rectangle.getAngle(3);
        if(doIntersect(p1,q1,p2,q2)) return true;

        return false;
    }
    public static boolean doIntersect(DoublePoint p1, DoublePoint q1, ArrayList<DoubleRectangle> rectangles){
        for(DoubleRectangle rect : rectangles) {
            if(doIntersect(p1,q1,rect)) return true;
        }
        return false;
    }
}

enum Direction {
    UP, RIGHT, DOWN, LEFT, NONE
}
class Rectangle {
    Point position;
    Point size;

    public Rectangle(Point position, Point size) {
        this.position = position;
        this.size = size;
    }
    public Rectangle(int x, int y, int w, int h) {
        this.position = new Point(x,y);
        this.size = new Point(w,h);
    }
    // if the given point is hasInside this rectangle
    public boolean hasInside(Point point) {
        return point.x >= this.position.x &&
                point.x <= this.position.x + this.size.x -1 &&
                point.y >= this.position.y &&
                point.y <= this.position.y + this.size.y -1;
    }
    public Point getAngle(int n) {
        Point angle;
        switch (n) {
            case 0:
            default:
                angle = position;
                break;
            case 1:
                angle = position.add(size.onlyX()).add(Point.left);
                break;
            case 2:
                angle = position.add(size).add(-1,-1);
                break;
            case 3:
                angle = position.add(size.onlyY()).add(Point.up);
                break;
        }
        return angle;
    }

    public boolean intersects(Rectangle rectangle) {
        boolean a= false,e;
        //left boundary of current rect is between left and right boundaries of other rect
        for (int i = 0; i < 4; i++) {
            if(this.hasInside(rectangle.getAngle(i))) {
                a = true;
            }
        }

        if (!a) {
            for (int i = 0; i < 4; i++) {
                if(rectangle.hasInside(this.getAngle(i))) {
                    a = true;
                }
            }
        }
        // special case: other rectangle is around this rectangle
        e = (   this.position.x >= rectangle.position.x &&
                this.position.y >= rectangle.position.y &&
                (this.position.x+this.size.x) <= (rectangle.position.x+rectangle.size.x) &&
                (this.position.y+this.size.y) <= (rectangle.position.y+rectangle.size.y));

        // at least one of horizontal and at least one of vertical boundaries (=> one angle) is visible
        return a | e ;
    }
    public Rectangle same() {
        return new Rectangle(this.position.same(), this.size.same());
    }

    public Rectangle scale(int n) {
        Rectangle r = this.same();
        r.size = r.size.mul(n);
        return r;
    }
    public Rectangle move(Point point){
        Rectangle r = this.same();
        r.position = r.position.add(point);
        return r;
    }
}
class DoubleRectangle{
    DoublePoint position;
    DoublePoint size;

    public DoubleRectangle(Rectangle rectangle) {
        this.position = new DoublePoint(rectangle.position);
        this.size = new DoublePoint(rectangle.size);
    }
    public DoubleRectangle(DoublePoint position, DoublePoint size) {
        this.position = position;
        this.size = size;
    }
    public DoublePoint getAngle(int n) {
        DoublePoint angle;
        switch (n) {
            case 0:
            default:
                angle = position;
                break;
            case 1:
                angle = position.add(size.onlyX());
                break;
            case 2:
                angle = position.add(size);
                break;
            case 3:
                angle = position.add(size.onlyY());
                break;
        }
        return angle;
    }
}
class Point implements Comparable<Point>{
    public int x;
    public int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
        this.x = 0;
        this.y = 0;
    }
    public Point add(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }
    public Point add(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }
    public Point sub(Point p) {
        return new Point(this.x - p.x, this.y - p.y);
    }
    public Point div(int m) {
        return new Point(this.x /m, this.y /m);
    }
    public Point mul(int m) {
        return new Point(this.x *m, this.y *m);
    }
    public Point mul(Point p) {
        return new Point(this.x *p.x, this.y *p.y);
    }
    /*public Point negative() {
        return new Point(-this.x , -this.y);
    }
    */
    public Point onlyX() {
        return new Point(this.x,0);
    }
    public Point onlyY() {
        return new Point(0,this.y);
    }
    public Point same() {
        return new Point(this.x,this.y);
    }
    public double len(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }
    public static Point up = new Point(0,-1);
    public static Point right =new Point(1, 0);
    public static Point down = new Point(0, 1);
    public static Point left = new Point(-1,0);

    public static Point fromDirection(Direction direction) {
        switch (direction){
            case UP:
                return up;
            case RIGHT:
                return right;
            case DOWN:
                return down;
            case LEFT:
                return left;

        }
        return new Point();
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        String one = "1";
        if(this.x < 0) one = "2";
        String two = "1";
        if(this.y < 0) two = "2";
        return  Integer.parseInt(one + Math.abs(this.x) + two +  Math.abs(this.y));
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass())
            return false;
        Point other = ((Point) o);
        return (this.x == other.x && this.y == other.y);
    }
    @Override
    public int compareTo(Point o) {
        if(this.equals(o))
            return 0;
        return -1;
    }
}
class DoublePoint implements Comparable<DoublePoint>{
    double x;
    double y;
    public DoublePoint(){
        this(0,0);
    }
    public DoublePoint(Point point){
        this(point.x, point.y);
    }
    public DoublePoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public DoublePoint add(DoublePoint p) {
        return new DoublePoint(this.x + p.x, this.y + p.y);
    }
    public DoublePoint add(Point p) {
        return new DoublePoint(this.x + p.x, this.y + p.y);
    }
    public DoublePoint add(double x, double y) {
        return new DoublePoint(this.x + x, this.y + y);
    }
    public DoublePoint sub(double x, double y) {
        return new DoublePoint(this.x - x, this.y - y);
    }

    public DoublePoint sub(DoublePoint p) {
        return new DoublePoint(this.x - p.x, this.y - p.y);
    }

    public DoublePoint onlyX() {
        return new DoublePoint(this.x,0);
    }
    public DoublePoint onlyY() {
        return new DoublePoint(0,this.y);
    }

    @Override
    public String toString() {
        return "DoublePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        String one = "1";
        if(this.x < 0) one = "2";
        String two = "1";
        if(this.y < 0) two = "2";

        long x,y;
        x = Double.doubleToLongBits(this.x);
        y = Double.doubleToLongBits(this.y);

        return  Integer.parseInt(one + (int)(x ^ (x >>> 32))%37 + two +  (int)(y ^ (y >>> 32))%37);
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass())
            return false;
        Point other = ((Point) o);
        return (this.x == other.x && this.y == other.y);
    }
    @Override
    public int compareTo(DoublePoint o) {
        if(this.equals(o))
            return 0;
        return -1;
    }

}