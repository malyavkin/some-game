public class Geom {
    private boolean onSegment(Point p, Point q, Point r)
    {
        return  q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }
    // To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are collinear
// 1 --> Clockwise
// 2 --> Counterclockwise
    int orientation(Point p, Point q, Point r)
    {
        // See 10th slides from following link for derivation of the formula
        // http://www.dcs.gla.ac.uk/~pat/52233/slides/Geometry1x1.pdf
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;  // collinear
        return (val > 0)? 1: 2; // clock or counter clock wise
    }
    // The main function that returns true if line segment 'p1q1'
// and 'p2q2' intersect.
    boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
    {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        // General case
        if (o1 != o2 && o3 != o4)
            return true;
        // Special Cases
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        // p1, q1 and p2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        return o4 == 0 && onSegment(p2, q1, q2);
    }
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