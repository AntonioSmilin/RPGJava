public class BattleScene {
    public void fight(FantasyCharacter hero, FantasyCharacter monster, Realm.FightCallback fightCallback) {
        Runnable runnable = () -> {
            int turn = 1;
            boolean isFightEnded = false;
            while (!isFightEnded) {
                System.out.println("-=Ход " + turn + "=-");
                if (turn++ % 2 != 0) {
                    isFightEnded = makeHit(monster, hero, fightCallback);
                } else {
                    isFightEnded = makeHit(hero, monster, fightCallback);
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private Boolean makeHit(FantasyCharacter defender, FantasyCharacter attacker, Realm.FightCallback fightCallback) {
        int hit = attacker.attack();
        int defenderHealth = defender.getHealthPoints() - hit;
        if (hit != 0) {
            System.out.println(String.format("%s нанес урон %d!", attacker.getName(), hit));
            System.out.println(String.format("У %s осталось %d здоровья..", defender.getName(), defenderHealth));
        } else {
            System.out.println(String.format("%s промазал!", attacker.getName()));
        }
        if (defenderHealth <= 0 && defender instanceof Hero) {
            System.out.println("Вы проиграли бой! Game Over");
            fightCallback.fightLost();
            return true;
        } else if(defenderHealth <= 0) {
            System.out.println(String.format("Вы победили! Вам достается +%d опыта и +%d золота", defender.getXp(), defender.getGold()));
            attacker.setXp(attacker.getXp() + defender.getXp());
            attacker.setGold(attacker.getGold() + defender.getGold());
            fightCallback.fightWin();
            return true;
        } else {
            defender.setHealthPoints(defenderHealth);
            return false;
        }
    }
}