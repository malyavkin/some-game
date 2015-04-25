import org.w3c.dom.css.Rect;

import java.security.PublicKey;

/**
 * Created by lexa on 4/23/2015.
 */
public class Geom {


}

class Rectangle {
    Point position;
    Point size;

    public Rectangle(Point position, Point size) {
        this.position = position;
        this.size = size;
    }
    public boolean intersects(Rectangle rectangle) {
        boolean a,b,c,d;
        //left boundary of current rect is between left and right boundaries of other rect
        a = (   this.position.x >= rectangle.position.x &&
                this.position.x <= rectangle.position.x+ rectangle.position.x);
        //right boundary of current rect is between left and right boundaries of other rect
        b = (   this.position.x+this.size.x >= rectangle.position.x &&
                this.position.x+this.size.x <= rectangle.position.x+ rectangle.size.x);
        //top boundary of current rect is between top and bottom boundaries of other rect
        c = (   this.position.y >= rectangle.position.y &&
                this.position.y <= rectangle.position.y+ rectangle.position.y);
        //bottom boundary of current rect is between top and bottom boundaries of other rect
        d = (   this.position.y+this.size.y >= rectangle.position.y &&
                this.position.y+this.size.y <= rectangle.position.y+ rectangle.size.y);

        // at least one of horizontal and at least one of vertical boundaries (=> one angle) is visible
        return (a|b) & (c|d);
    }
    public Rectangle same() {
        return new Rectangle(this.position.same(), this.size.same());
    }

    public Rectangle scale(int n) {
        Rectangle r = this.same();
        r.size.mul(n);
        return r;
    }
}

enum Direction {
    NONE, UP, RIGHT, DOWN, LEFT;
}

class Point {
    public int x;
    public int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point add(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }
    public Point sub(Point p) {
        return new Point(this.x - p.x, this.y - p.y);
    }
    public Point mul(int m) {
        return new Point(this.x *m, this.y *m);
    }
    public Point mul(Point p) {
        return new Point(this.x *p.x, this.y *p.y);
    }
    public Point negative() {
        return new Point(-this.x , -this.y);
    }
    public boolean eq(Point p) {
        return (this.x == p.x && this.y == p.y);
    }
    public Point onlyX() {
        return new Point(this.x,0);
    }
    public Point onlyY() {
        return new Point(0,this.y);
    }
    public Point same() {
        return new Point(this.x,this.y);
    }
    public static Point up = new Point(0,-1);
    public static Point right =new Point(1, 0);
    public static Point down = new Point(0, 1);
    public static Point left = new Point(-1,0);
}