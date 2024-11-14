package SuperRobot.orbs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class OrbPatch {
    //修改闪电充能球，在拥有雷暴能力时基础和被动伤害+雷暴层数
    //todo 修改冰霜充能球，在拥有雪暴能力时基础和被动格挡+雪暴层数
    @SpirePatch(
            clz= AbstractOrb.class,
            method="applyFocus"
    )
    public static class ModifyAbstractOrb{
        @SpirePostfixPatch
        public static void ModifyEffect(AbstractOrb __instance)
        {
            AbstractPower power2 = AbstractDungeon.player.getPower("Storm");
            AbstractPower power3 = AbstractDungeon.player.getPower("SuperRobot:SnowStorm");
            if(power2 != null && __instance.ID.equals("Lightning")){
                __instance.passiveAmount+=power2.amount;
                __instance.evokeAmount+=power2.amount;
            }
            if(power2 != null && __instance.ID.equals("Frost")){
                __instance.passiveAmount=Math.max(0,__instance.passiveAmount-1);
                __instance.evokeAmount=Math.max(0,__instance.evokeAmount-1);
            }
            if(power3 != null && __instance.ID.equals("Frost")){
                __instance.passiveAmount+=power3.amount;
                __instance.evokeAmount+=power3.amount;
            }
            if(power3 != null && __instance.ID.equals("Lightning")){
                __instance.passiveAmount=Math.max(0,__instance.passiveAmount-1);
                __instance.evokeAmount=Math.max(0,__instance.evokeAmount-1);
            }
        }
    }
}
