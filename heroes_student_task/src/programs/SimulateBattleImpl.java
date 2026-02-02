package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Ваше решение
        playerArmy.getUnits().sort((u1, u2) -> {
            double reatio1 = u1.getBaseAttack() / (double) u1.getCost();
            double reatio2 = u2.getBaseAttack() / (double) u2.getCost();
            return Double.compare(reatio2, reatio1);
        });
        computerArmy.getUnits().sort((u1, u2) -> {
            double reatio1 = u1.getBaseAttack() / (double) u1.getCost();
            double reatio2 = u2.getBaseAttack() / (double) u2.getCost();
            return Double.compare(reatio2, reatio1);
        });
        List<Unit> battle = new ArrayList<>();
        int i = 0;
        while(true){
            battle = createListBattle(playerArmy.getUnits(), computerArmy.getUnits());
            if(battle.isEmpty()){
                break;
            }
            for(; i < battle.size(); i++){
                Unit target = battle.get(i).getProgram().attack();
                if(target == null){
                    continue;
                }
                printBattleLog.printBattleLog(battle.get(i), target);
                if(!target.isAlive()){
                    break;
                }
            }
            if(i >= battle.size()){
                i = 0;
                battle.clear();
            }
        }
    }
    public static List<Unit> createListBattle(List<Unit> army_player, List<Unit> army_compucter){
        List<Unit> war = new LinkedList<>();
        int maxLength = Math.max(army_compucter.size(), army_player.size());
        int sumUnitPlayer = 0;
        int sumUnitComputer = 0;
        for (int j = 0; j < maxLength; j++) {
            if (j < army_player.size() && army_player.get(j).isAlive()) {
                war.add(army_player.get(j));
                sumUnitPlayer++;
            }
            if (j < army_compucter.size() && army_compucter.get(j).isAlive()) {
                war.add(army_compucter.get(j));
                sumUnitComputer++;
            }
        }
        if(sumUnitPlayer == 0 || sumUnitComputer == 0){
            war.clear();
        }
        return war;
    }
}