package rip.dfg;
import java.util.*;


public class Route {

}

class RouteBuilder {
    private Map map;
    private ArrayList<DoubleRectangle> blocks = new ArrayList<>();
    private ArrayList<DoublePoint> wayPoints = new ArrayList<>();
    private Graph<Point> graph;
    public RouteBuilder(Map map) {
        this.map = map;
    }
    private Point getOffsetFrom8way(Point point, int value){
        switch (value){
            case 0:
                return point.add( 0,-1);
            case 1:
                return point.add( 1,-1);
            case 2:
                return point.add( 1, 0);
            case 3:
                return point.add( 1, 1);
            case 4:
                return point.add( 0, 1);
            case 5:
                return point.add(-1, 1);
            case 6:
                return point.add(-1, 0);
            case 7:
                return point.add(-1,-1);

        }
        return point;
    }
    public Tile[] getSurroundings(Point point){
        return new Tile[] {
                map.getTile(getOffsetFrom8way(point,0)),
                map.getTile(getOffsetFrom8way(point,1)),
                map.getTile(getOffsetFrom8way(point,2)),
                map.getTile(getOffsetFrom8way(point,3)),
                map.getTile(getOffsetFrom8way(point,4)),
                map.getTile(getOffsetFrom8way(point,5)),
                map.getTile(getOffsetFrom8way(point,6)),
                map.getTile(getOffsetFrom8way(point,7))
        };
    }
    public ArrayList<Integer> angles2(Tile[] tiles) {
        int len = tiles.length;
        boolean[] spaces = new boolean[len];
        for (int i = 0; i < len; i++) {
            spaces[i] = tiles[i] != null && !tiles[i].canCollide();

        }
        ArrayList<Integer> angles = new ArrayList<>();
        int[] possibleAngles = {1,3,5,7};
        for (int possibleAngle : possibleAngles) {
            if (spaces[possibleAngle]) {
                // counting free cells counter-clockwise
                int ccw = 0;
                for (int i = 1; i < len; i++) {

                    int realIndex = possibleAngle-i;
                    while (realIndex < 0) realIndex += len;
                    while (realIndex >= len) realIndex -= len;
                    if(spaces[realIndex]){
                        ccw++;
                    } else {
                        break;
                    }
                }
                // counting free cells clockwise
                int cw = 0;
                for (int i = 1; i < len; i++) {

                    int realIndex = possibleAngle+i;
                    while (realIndex < 0) realIndex += len;
                    while (realIndex >= len) realIndex -= len;
                    if(spaces[realIndex]){
                        cw++;
                    } else {
                        break;
                    }
                }

                if (ccw + cw >= 3 && ccw != 0 && cw != 0) {
                    angles.add(possibleAngle);
                }
            }

        }
        return angles;

    }
    /*private void print(){
        boolean [] wp= getWayPoints();
        for (int i = 0; i < wp.length; i++) {
            if (i%map.w == 0) {
                System.out.print("\n");
            }

            System.out.print(wp[i]? "+" : map.levelData[i].canCollide()?"#":" ");

        }
    }*/
    public HashSet<Point> getWayPoints(){
        HashSet<Point> wp= new HashSet<>();
        //boolean[] wp = new boolean[map.levelData.length];
        for (int i = 0; i < map.levelData.length; i++) {
            Tile tile = map.levelData[i];
            if(tile.canCollide()){
                Point XY = map.getXY(i);
                ArrayList<Integer> angles = angles2(getSurroundings(XY));
                for (Integer angle : angles) {
                    Point anglePos = getOffsetFrom8way(XY, angle);
                    wp.add(new Point(anglePos.x, anglePos.y));


                }
            }
        }
        return wp;
    }
    public HashSet<DoublePoint> waypointsVisibleFrom(DoublePoint doublePoint) {
        HashSet<DoublePoint> visibles = new HashSet<>();
        for(DoublePoint doublePoint2 : wayPoints){
            if(doublePoint != doublePoint2) {
                if(!Geom.doIntersect(doublePoint, doublePoint2,blocks)) {
                    visibles.add(doublePoint2);
                }
            }
        }
        return visibles;

    }
    public void buildGraph(){
        graph = new Graph<>();
        HashSet<Point> wayPointCells= getWayPoints();
        for (Point p : wayPointCells) {
            this.wayPoints.add(new DoublePoint(p));
        }
        for (int i = 0; i < map.levelData.length; i++) {
            if(map.levelData[i].canCollide()) {
                Point point = map.getXY(i);
                this.blocks.add(new DoubleRectangle(new DoublePoint(point).sub(0.5, 0.5), new DoublePoint(1,1)));
            }
        }
        for(DoublePoint doublePoint : wayPoints){
            Node<Point> node = graph.findOrAdd(new Point((int)doublePoint.x, (int)doublePoint.y));
            HashSet<DoublePoint> others = waypointsVisibleFrom(doublePoint);
            for(DoublePoint other : others){
                Node<Point> node2 = graph.findOrAdd(new Point((int)other.x, (int)other.y));
                node.link(node2, node.data.sub(node2.data).len());
            }
        }
    }
}
