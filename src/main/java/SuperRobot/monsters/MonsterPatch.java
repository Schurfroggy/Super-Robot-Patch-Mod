package SuperRobot.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;

public class MonsterPatch {
    //防止第一回合在有偏差的情况下被打成负集中
    @SpirePatch(
            clz= SpireShield.class,
            method="takeTurn"
    )
    public static class ModifyShieldTakeTurn {
        @SpireInsertPatch(
                rloc=7
        )
        public static SpireReturn<?> ModifyEffect(AbstractMonster __instance)
        {
            if(AbstractDungeon.player.hasPower("Bias")&&AbstractDungeon.player.hasPower("Focus")&&AbstractDungeon.player.getPower("Focus").amount<0){
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player,__instance,"Focus"));
                AbstractDungeon.player.getPower("Bias").flash();
            }
            return SpireReturn.Continue();
        }
}
}
