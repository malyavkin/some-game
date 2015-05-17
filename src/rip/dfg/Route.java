package rip.dfg;
import java.util.*;


public class Route {
    ArrayList<Node> nodes;
    double length;

    public Route() {
    }

    public Route(Node startNode) {
        this.nodes = new ArrayList<>();
        nodes.add(startNode);
        length = 0;
    }

    public Route same(){
        Route r = new Route();
        r.length = this.length;
        r.nodes = new ArrayList<>();
        r.nodes.addAll(nodes);
        return r;
    }
}

class RouteBuilder {
    private Map map;
    private ArrayList<DoubleRectangle> blocks = new ArrayList<>();
    private ArrayList<Point> wayPoints = new ArrayList<>();
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

    public HashSet<Point> waypointsVisibleFrom(Point point) {

        HashSet<Point> visibles = new HashSet<>();
        for(Point point2 : wayPoints){
            if(!point.equals(point2)) {
                if(!Geom.doIntersect(new DoublePoint(point), new DoublePoint(point2),blocks)) {
                    visibles.add(point2);
                }
            }
        }
        return visibles;

    }
    public void buildGraph(){
        graph = new Graph<>();
        HashSet<Point> wayPointCells= getWayPoints();
        for (Point p : wayPointCells) {
            this.wayPoints.add(p);
        }
        for (int i = 0; i < map.levelData.length; i++) {
            if(map.levelData[i].canCollide()) {
                Point point = map.getXY(i);
                this.blocks.add(new DoubleRectangle(new DoublePoint(point).sub(0.5, 0.5), new DoublePoint(1,1)));
            }
        }
        for(Point p : wayPoints){
            Point node = p.same();
            HashSet<Point> others = waypointsVisibleFrom(node);
            for(Point other : others){
                graph.link_1way(node, other, node.sub(other).len());
            }
        }
    }

    public Route aStar (Point from, Point to){
        HashSet<Point> visibleStart = waypointsVisibleFrom(from);
        HashSet<Point> visibleFinish = waypointsVisibleFrom(to);
        Node<Point> fromNode = graph.obtain(from);
        Node<Point> toNode = graph.obtain(to);
        Route result = null;
        // Extending graph

        for(Point point: visibleStart) {
            graph.link_2way(from, point, from.sub(point).len());
        }
        for(Point point: visibleFinish) {
            graph.link_2way(to, point, to.sub(point).len());
        }

        //pathfinding logic

        HashMap<Point, Double> distances = new HashMap<>();
        for(Node<Point> node: graph.nodes) {
            distances.put(node.data, node.data.sub(to).len());
        }


        HashMap<Node<Point>, Route> front = new HashMap<>();
        HashSet<Point> closed = new HashSet<>();

        Route frontRoute = new Route(fromNode);
        front.put(fromNode, frontRoute);

        while (result == null && front.size() != 0) {
            // selecting best node in front
            Node<Point> bestNode = null;
            for(Node<Point> node: front.keySet()){

                if(bestNode == null || (front.get(bestNode).length > front.get(node).length)) {
                    bestNode = node;
                }
                // put all adjacent (and not in close) to the best nodes to front
                for(Node<Point> adjacent : bestNode.links.keySet()){
                    // if one of adjacent nodes is already in front, compare routes and switch if the new one is shorter
                    if(!front.containsKey(adjacent) || (
                            front.containsKey(adjacent) && (front.get(adjacent).length > front.get(bestNode).length + bestNode.links.get(adjacent))
                    )){
                        Route newRoute = front.get(bestNode).same();
                        newRoute.nodes.add(adjacent);
                        front.put(adjacent, newRoute);
                        if(adjacent == toNode) {
                            result = newRoute;
                        }
                    }
                }
                // remove best from front
                front.remove(bestNode);
            }
        }

        //Detaching
        graph.unlink_all(from);
        graph.unlink_all(to);

        return result;
    }
}
