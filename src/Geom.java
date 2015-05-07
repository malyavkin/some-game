public class Geom {


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
                angle = position.add(size).add(new Point(-1,-1));
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

enum Direction {
    UP, RIGHT, DOWN, LEFT, NONE
}

class Point {
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

    public static Point from8way(Direction direction) {
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

}