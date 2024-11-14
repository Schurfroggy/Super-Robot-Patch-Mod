package SuperRobot.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenCore;


public class RelicPatch {
    //修改冰冻核心为初始遗物，效果变为在有剩余球位且无集中时生成一个冰霜充能球
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

    @SpirePatch(
            clz=FrozenCore.class,
            method="onPlayerEndTurn"
    )
    public static class ModifyFrozenCoreOnPlayerEndTurn
    {
        @SpirePrefixPatch
        public static SpireReturn<?> ModifyEffecct(AbstractRelic __instance)
        {
            if (AbstractDungeon.player.hasEmptyOrb()&&!AbstractDungeon.player.hasPower("Focus")) {
                __instance.flash();
                AbstractDungeon.player.channelOrb(new Frost());
            }
            return SpireReturn.Return();
        }
    }


}
