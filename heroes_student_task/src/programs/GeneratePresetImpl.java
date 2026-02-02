package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;
class Point{
int x;
int y;
public Point(int x, int y){
    this.x = x;
    this.y = y;
}
}
public class GeneratePresetImpl implements GeneratePreset {
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {

        // Ваше решение
        final int MAX_UNIT_ONE_TYPE = 11;
        Random random = new Random();
        List<Unit> attackEffective = new ArrayList<>(unitList);
        List<Unit> healthEffective = new ArrayList<>(unitList);
        List<Point> points = new LinkedList<>();
        List<Unit> army = new LinkedList<>();
        HashMap<String, Integer> typeCount = new HashMap<>();

        for (Unit unit : unitList) {
            typeCount.put(unit.getUnitType(), 1);
        }
        attackEffective.sort((u1, u2) -> {
            double reatio1 = u1.getBaseAttack() / (double) u1.getCost();
            double reatio2 = u2.getBaseAttack() / (double) u2.getCost();
            return Double.compare(reatio2, reatio1);
        });
        healthEffective.sort((u1, u2) -> {
            double reatio1 = u1.getHealth() / (double) u1.getCost();
            double reatio2 = u2.getHealth() / (double) u2.getCost();
            return Double.compare(reatio2, reatio1);
        });
        int avg = maxPoints/2;
        for(Unit item: attackEffective){
            String type = item.getUnitType();
            int currentCount = typeCount.get(type);
            for(int i = currentCount; i <= MAX_UNIT_ONE_TYPE; i++){
                if((avg - item.getCost()) < 0){
                    break;
                }
                Unit unit = new Unit(item.getName(),item.getUnitType(),
                        item.getHealth(), item.getBaseAttack(), item.getCost(), item.getAttackType(),
                        item.getAttackBonuses(), item.getDefenceBonuses(),
                        item.getxCoordinate(), item.getyCoordinate());
                unit.setName(item.getName() + " " + i);
                unit.setxCoordinate(random.nextInt(3));
                unit.setyCoordinate(random.nextInt(21));
                for(int j = 0; j < points.size(); j++ ){
                    Point point = points.get(j);
                    if(point.x == unit.getxCoordinate() && point.y == unit.getyCoordinate()){
                        unit.setxCoordinate(random.nextInt(3));
                        unit.setyCoordinate(random.nextInt(21));
                        j = 0;
                    }
                }
                points.add(new Point(unit.getxCoordinate(), unit.getyCoordinate()));
                avg -= item.getCost();
                typeCount.put(type, i);
                army.add(unit);
            }
            if(avg <= 0){
                break;
            }
        }
        avg = maxPoints - maxPoints/2;
        for(Unit item: healthEffective){
            String type = item.getUnitType();
            int currentCount = typeCount.get(type);
            for(int i = currentCount; i <= MAX_UNIT_ONE_TYPE; i++){
                if((avg - item.getCost()) < 0){
                    break;
                }
                Unit unit = new Unit(item.getName(),item.getUnitType(),
                        item.getHealth(), item.getBaseAttack(), item.getCost(), item.getAttackType(),
                        item.getAttackBonuses(), item.getDefenceBonuses(),
                        item.getxCoordinate(), item.getyCoordinate());
                unit.setName(item.getName() + " " + i);
                unit.setxCoordinate(random.nextInt(3));
                unit.setyCoordinate(random.nextInt(21));
                for(int j = 0; j < points.size(); j++ ){
                    Point point = points.get(j);
                    if(point.x == unit.getxCoordinate() && point.y == unit.getyCoordinate()){
                        unit.setxCoordinate(random.nextInt(3));
                        unit.setyCoordinate(random.nextInt(21));
                        j = -1;
                    }
                }
                points.add(new Point(unit.getxCoordinate(), unit.getyCoordinate()));
                avg -= item.getCost();
                typeCount.put(type, i);
                army.add(unit);
            }
            if(avg <= 0){
                break;
            }
        }
        return new Army(army);
    }
}