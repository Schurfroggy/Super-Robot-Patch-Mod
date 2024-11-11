package SuperRobot.cards;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Dualcast;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BasicCardPatch {
    //修改dualcast费用
    @SpirePatch(
            clz= Dualcast.class,
            method = "<ctor>"
    )
    public static class ReplaceDualCast{
        @SpirePostfixPatch
        public static void DualCast(AbstractCard __instance){
            __instance.baseMagicNumber=__instance.magicNumber = 2;
            __instance.cost=__instance.costForTurn=0;
            __instance.isCostModified=false;
        }
    }

    //修改dualcast升级方法
    @SpirePatch(
            clz = Dualcast.class,
            method = "upgrade"
    )
    public static class DualcastRemoveCostUpgrade {
        @SpireInsertPatch(
                rloc = 3
        )
        public static void patch(AbstractCard __instance) {
            __instance.upgradedCost=false;
            __instance.isCostModified=false;
            ++__instance.baseMagicNumber;
            __instance.magicNumber = __instance.baseMagicNumber;
            __instance.upgradedMagicNumber=true;
        }
    }

    //修改dualcast升级后使用方法
    @SpirePatch(
            clz=Dualcast.class,
            method="use"
    )
    public static class DualCastUse{
        @SpireInsertPatch(
                rloc = 3
        )
        public static void Replace(AbstractCard __instance){
            if(__instance.upgraded){
                AbstractDungeon.actionManager.addToBottom(new AnimateOrbAction(1));
                AbstractDungeon.actionManager.addToBottom(new EvokeWithoutRemovingOrbAction(1));
            }

        }
    }
}
