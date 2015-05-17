package rip.dfg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Test {
    static Map map;
    static RouteBuilder routeBuilder;
    static Graph<Point> pointGraph;
    public static void hasinside(){
        Rectangle one = new Rectangle(0,0,10,10), two;

        System.out.println("hasInside tests");


        System.out.println("test 1 " + one.hasInside(new Point(0,0)));
        System.out.println("test 2 " + one.hasInside(new Point(0,5)));
        System.out.println("test 3 " + one.hasInside(new Point(0,9)));
        System.out.println("test 4 " + !one.hasInside(new Point(0,10)));
        System.out.println("test 5 " + one.hasInside(new Point(5,0)));
        System.out.println("test 6 " + one.hasInside(new Point(5,5)));
        System.out.println("test 7 " + one.hasInside(new Point(5,9)));
        System.out.println("test 8 " + !one.hasInside(new Point(5,10)));
        System.out.println("test 9 " + one.hasInside(new Point(9,0)));
        System.out.println("test 10 " + one.hasInside(new Point(9,5)));
        System.out.println("test 11 " + one.hasInside(new Point(9,9)));
        System.out.println("test 12 " + !one.hasInside(new Point(9,10)));
        System.out.println("test 13 " + !one.hasInside(new Point(10,0)));
        System.out.println("test 14 " + !one.hasInside(new Point(10,5)));
        System.out.println("test 15 " + !one.hasInside(new Point(10,9)));
        System.out.println("test 16 " + !one.hasInside(new Point(10,10)));


        System.out.println("Intersection tests");
        System.out.println("Following tests should be true");
        // ?????? ????????????
        two = new Rectangle(0,0,10,10);
        System.out.println("test 1 " + one.intersects(two));
        System.out.println("test 1 " + two.intersects(one));

        // ????????? ????????
        two = new Rectangle(-1,-1,10,10);
        System.out.println("test 2 " + one.intersects(two));
        System.out.println("test 2 " + two.intersects(one));

        two = new Rectangle(1,1,10,10);
        System.out.println("test 3 " + one.intersects(two));
        System.out.println("test 3 " + two.intersects(one));

        two = new Rectangle(1,-1,10,10);
        System.out.println("test 4 " + one.intersects(two));
        System.out.println("test 4 " + two.intersects(one));

        two = new Rectangle(-1,1,10,10);
        System.out.println("test 5 " + one.intersects(two));
        System.out.println("test 5 " + two.intersects(one));

        // ?????? ?????? ???????
        two = new Rectangle(-1,-1,20,20);
        System.out.println("test 6 " + one.intersects(two));
        System.out.println("test 6 " + two.intersects(one));

        // ... ??????
        two = new Rectangle(1,1,5,5);
        System.out.println("test 7 " + one.intersects(two));
        System.out.println("test 7 " + two.intersects(one));

        // ?????? ?? ????? ????? ????? ???????
        two = new Rectangle(-1,1,5,5);
        System.out.println("test 8 " + one.intersects(two));
        System.out.println("test 8 " + two.intersects(one));

        two = new Rectangle(1,-1,5,5);
        System.out.println("test 9 " + one.intersects(two));
        System.out.println("test 9 " + two.intersects(one));

        // ?????? ????? ? ??????
        System.out.println("Following tests should be false");
        two = new Rectangle(10,0,10,10);
        System.out.println("test 10 " + one.intersects(two));
        System.out.println("test 10 " + two.intersects(one));

        two = new Rectangle(0,10,10,10);
        System.out.println("test 11 " + one.intersects(two));
        System.out.println("test 11 " + two.intersects(one));

        two = new Rectangle(-10,0,10,10);
        System.out.println("test 12 " + one.intersects(two));
        System.out.println("test 12 " + two.intersects(one));

        two = new Rectangle(0,-10,10,10);
        System.out.println("test 13 " + one.intersects(two));
        System.out.println("test 13 " + two.intersects(one));

    }
    public static void tiles(){
        System.out.println("free tiles test");

        map =new Map(
                new int[][]
                        {
                                {0,0,0},
                                {1,1,0},
                                {0,1,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );

        routeBuilder = new RouteBuilder(map);
        System.out.println("test 1: 1");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1,1))));

        map =new Map(
                new int[][]
                        {
                                {0,1,0},
                                {1,1,0},
                                {0,0,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 2: 3");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));
        map =new Map(
                new int[][]
                        {
                                {0,1,0},
                                {0,1,1},
                                {0,0,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 3: 5");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));
        map =new Map(
                new int[][]
                        {
                                {0,0,0},
                                {0,1,1},
                                {0,1,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 4: 7");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));
        map =new Map(
                new int[][]
                        {
                                {1,0,1},
                                {0,1,1},
                                {0,0,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 5: 5");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));

        map =new Map(
                new int[][]
                        {
                                {0,1,0},
                                {0,1,0},
                                {0,0,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 6: 3,5");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));

        map =new Map(
                new int[][]
                        {
                                {0,0,0},
                                {0,1,0},
                                {0,0,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 7: 1,3,5,7");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));
        map =new Map(
                new int[][]
                        {
                                {1,0,1},
                                {0,1,0},
                                {0,0,0}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 8: 3,5");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1,1))));
        map =new Map(
                new int[][]
                        {
                                {1,0,1},
                                {0,1,0},
                                {0,0,1}},
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 9: none");
        System.out.println(routeBuilder.angles2(routeBuilder.getSurroundings(new Point(1, 1))));
    }

    public static void fullmap(){
        System.out.println("fullmap test");


        map =new Map(
                new int[][]
                        {
                                {0,0,0,0,0,0,0,0,0,0,0,0},
                                {0,1,1,1,0,1,1,1,1,1,1,0},
                                {0,1,1,1,0,1,1,1,1,1,1,0},
                                {0,1,0,0,0,1,1,0,0,0,0,0},
                                {0,1,0,0,0,1,1,0,0,0,0,0},
                                {0,1,0,0,0,0,0,0,0,1,1,0},
                                {0,1,1,1,1,0,0,0,0,1,1,0},
                                {0,1,1,1,1,1,0,1,1,1,1,0},
                                {0,1,1,1,1,1,0,1,1,1,1,0},
                                {0,0,0,0,0,0,0,0,0,0,0,0}

                                //{0,0,0},{0,1,0},{0,0,0}


                        },
                new int[]{TileType.GROUND, TileType.WALL}
        );
        routeBuilder = new RouteBuilder(map);
        System.out.println("test 1:");
        /*boolean [] wp= routeBuilder.getWayPoints();
        ArrayList<DoubleRectangle> blocks = new ArrayList<>();
        ArrayList<DoublePoint> wayPoints = new ArrayList<>();

        for (int i = 0; i < wp.length; i++) {
            if(wp[i]) {
                wayPoints.add(new DoublePoint(map.getXY(i)).add(0.5, 0.5));
            }

            if(map.levelData[i].canCollide()) {
                blocks.add(new DoubleRectangle(new DoublePoint(map.getXY(i)), new DoublePoint(1,1)));
            }
        }
        for(DoublePoint doublePoint : wayPoints){
            System.out.println((int)doublePoint.x + "," + (int)doublePoint.y + ": ");
            for(DoublePoint doublePoint2 : wayPoints){
                if(doublePoint != doublePoint2) {
                    if(!Geom.doIntersect(doublePoint, doublePoint2,blocks)) {
                        System.out.println("    " + (int)doublePoint2.x + "," + (int)doublePoint2.y);
                    }
                }
            }
        }*/
        routeBuilder.buildGraph();
        System.out.println(routeBuilder.waypointsVisibleFrom(new Point(2,4)));
    }

    public static void main(String[] args) {

        fullmap();
        HashMap<Integer, Integer> integerIntegerHashMap = new HashMap<>();
        integerIntegerHashMap.put(1,2);
        integerIntegerHashMap.put(1,3);
        integerIntegerHashMap.put(1,4);
        integerIntegerHashMap.put(1,5);
        integerIntegerHashMap.put(1,6);

    }
}
