package SuperRobot.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenCore;


public class RelicPatch {
    //修改冰冻核心为初始遗物
    @SpirePatch(
            clz = FrozenCore.class,
            method = "<ctor>"
    )
    public static class ModifyFrozenCoreRelicTier
    {
        @SpireInsertPatch(
                rloc = 1
        )
        public static void modifyRelicTier(AbstractRelic __instance)
        {
            __instance.tier = AbstractRelic.RelicTier.STARTER;
        }
    }
}
