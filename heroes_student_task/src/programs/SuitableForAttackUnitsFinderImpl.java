package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.*;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        // Ваше решение
        List<Unit> result = new ArrayList<>();
        Set<Integer> arrayCoordinationY = new HashSet<>();
        int[] numLine;
        if (isLeftArmyTarget) {
            // Атакует армия игрока -> цель на x=0..2
            numLine = new int[]{2, 1, 0};
        } else {
            // Атакует армия компьютера -> цель на x=24..26
            numLine = new int[]{24, 25, 26};
        }
            for(int targetLine : numLine){
                for (List<Unit> row : unitsByRow) {
                    if (row.isEmpty()) {
                        continue;
                    }
                    if (row.get(0).getxCoordinate() == targetLine) {
                        // Добавляем юнитов с уникальными Y-координатами
                        for (Unit unit : row) {
                            if(!unit.isAlive()){
                                continue;
                            }
                            int y = unit.getyCoordinate();
                            if (!arrayCoordinationY.contains(y)) {
                                arrayCoordinationY.add(y);
                                result.add(unit);
                            }
                        }
                        break;
                    }
                }
            }
        return result;
    }
}
