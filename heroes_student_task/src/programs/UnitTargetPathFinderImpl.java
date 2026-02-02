package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int[][] NEIGHBORS = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0},  // вертикально/горизонтально
            {1, 1}, {1, -1}, {-1, -1}, {-1, 1} // диагонали
    };
    private static class Point {
        final int x, y;
        int moveCost, everyCost, weight; // g - стоимость от начала, h - эвристика, f = g + h
        Point parent;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Ваше решение
        List<Edge> result = new ArrayList<>();

        PriorityQueue<Point> openSet = new PriorityQueue<>(
                Comparator.comparingInt(point -> point.weight)
        );
        Set<Point> closedList = new HashSet<>();
        Map<Point, Point> openList = new HashMap<>();
        Set<Point> barrier = new HashSet<>();

        for(Unit item: existingUnitList){
            if(item.equals(attackUnit) || item.equals(targetUnit)){
                continue;
            }
            barrier.add((new Point(item.getxCoordinate(), item.getyCoordinate())));
        }

        Point start = new Point(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());//Координаты - атакующего юнита
        Point end = new Point(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());//Координаты - цели
        start.moveCost = 0;
        start.everyCost = createEvery(start, end);
        start.weight = start.moveCost + start.everyCost;

        openSet.add(start);
        openList.put(start, start);

        while (!openSet.isEmpty()){
            Point active = openSet.poll();
            openList.remove(active);
            if (active.x == end.x && active.y == end.y) {
                result = reconstructPath(active);
                return result;
            }

            closedList.add(active);

            for(int[] offset : NEIGHBORS){
                boolean statusFind = false;
                int Xnew = active.x + offset[0];
                int Ynew = active.y + offset[1];
                Point neighbor = new Point(Xnew, Ynew);
                if (Xnew < 0 || Xnew >= 27 || Ynew < 0 || Ynew >= 21) {
                    continue; // Пропускаем соседей за границами поля
                }
                if(barrier.contains(neighbor)){
                    continue;
                }
                if(closedList.contains(neighbor)){
                    continue;
                }

                    neighbor.parent = active;
                    neighbor.everyCost = createEvery(active, end);
                    neighbor.moveCost = active.moveCost +
                            ((Math.abs(offset[0]) == 1 && Math.abs(offset[1]) == 1) ? 14 : 10);
                    neighbor.weight = neighbor.moveCost + neighbor.everyCost;

                    Point existingNeighbor = openList.get(neighbor);
                    if(existingNeighbor == null || neighbor.moveCost < existingNeighbor.moveCost){
                        if (existingNeighbor == null) {
                            openSet.add(neighbor);
                            openList.put(neighbor, neighbor);
                        } else {
                            openSet.remove(existingNeighbor);
                            openSet.add(neighbor);
                            openList.put(neighbor, neighbor);
                        }
                    }
            }
        }
        return Collections.emptyList();
    }

    private static List<Edge> reconstructPath(Point point) {
        List<Point> path = new ArrayList<>();
        List<Edge> result = new ArrayList<>();
        while (point != null) {
            path.add(point);
            point = point.parent;
        }
        Collections.reverse(path);
        for(Point item: path){
            result.add(new Edge(item.x, item.y));
        }
        return result;
    }

    public static int createEvery(Point a, Point b){
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        return 10 * (dx + dy) + (14 - 2 * 10) * Math.min(dx, dy);
    }
}
