package rip.dfg;
public class Geometry {

    public static final double EPSILON = 0.000001;

    /**
     * Calculate the cross product of two points.
     * @param a A point
     * @param b B point
     * @return the value of the cross product
     */
    public static double crossProduct(DoublePoint a, DoublePoint b) {
        return a.x * b.y - b.x * a.y;
    }

    /**
     * Check if bounding boxes do intersect. If one bounding box
     * touches the other, they do intersect.
     * @param a A bounding box
     * @param b B bounding box
     * @return <code>true</code> if they intersect,
     *         <code>false</code> otherwise.
     */
    public static boolean doBoundingBoxesIntersect(DoublePoint[] a, DoublePoint[] b) {
        return a[0].x <= b[1].x && a[1].x >= b[0].x && a[0].y <= b[1].y
                && a[1].y >= b[0].y;
    }

    /**
     * Checks if a Point is on a line
     * @param line line (interpreted as line, although given as line
     *                segment)
     * @param E point
     * @return <code>true</code> if point is on line, otherwise
     *         <code>false</code>
     */
    public static boolean isPointOnLine(LineSegment line, DoublePoint E) {
        double r = crossProduct(line.vector(), E.sub(line.A));
        return Math.abs(r) < EPSILON;
    }

    /**
     * Checks if a point is right of a line. If the point is on the
     * line, it is not right of the line.
     * @param line line segment interpreted as a line
     * @param E the point
     * @return <code>true</code> if the point is right of the line,
     *         <code>false</code> otherwise
     */
    public static boolean isPointRightOfLine(LineSegment line, DoublePoint E) {

        return crossProduct(line.vector(), E.sub(line.A)) < 0;
    }

    /**
     * Check if line segment A touches or crosses the line that is
     * defined by line segment B.
     *
     * @param a line segment interpreted as line
     * @param b line segment
     * @return <code>true</code> if line segment A touches or
     *                           crosses line B,
     *         <code>false</code> otherwise.
     */
    public static boolean lineSegmentTouchesOrCrossesLine(LineSegment a, LineSegment b) {
        return isPointOnLine(a, b.A)
                || isPointOnLine(a, b.B)
                || (isPointRightOfLine(a, b.A) ^ isPointRightOfLine(a, b.B));
    }

    /**
     * Check if line segments intersect
     * @param a A line segment
     * @param b B line segment
     * @return <code>true</code> if lines do intersect,
     *         <code>false</code> otherwise
     */
    public static boolean doLinesIntersect(LineSegment a, LineSegment b) {
        DoublePoint[] box1 = a.getBoundingBox();
        DoublePoint[] box2 = b.getBoundingBox();
        return doBoundingBoxesIntersect(box1, box2)
                && lineSegmentTouchesOrCrossesLine(a, b)
                && lineSegmentTouchesOrCrossesLine(b, a);
    }

    
  
}

class LineSegment {
    DoublePoint A;
    DoublePoint B;

    public LineSegment(DoublePoint a, DoublePoint b) {
        this.A = a;
        this.B = b;
    }

    public DoublePoint[] getBoundingBox() {
        DoublePoint[] result = new DoublePoint[2];
        result[0] = new DoublePoint(
                Math.min(this.A.x, this.B.x),
                Math.min(this.A.y, this.B.y));
        result[1] = new DoublePoint(
                Math.max(this.A.x, this.B.x),
                Math.max(this.A.y, this.B.y));
        return result;
    }
    public DoublePoint vector(){
        return this.B.sub(this.A);
    }


}