package SuperRobot.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BiasPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.HelloPower;

public class PowerPatch {
    //修改你好世界效果为固定增加零费手牌
    @SpirePatch(
            clz= HelloPower.class,
            method="atStartOfTurn"
    )
    public static class ModifyHelloPowerAtStartOfTurn {
        public static void Replace(AbstractPower __instance)
        {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                __instance.flash();

                for(int i = 0; i < __instance.amount; ++i) {
                    //this.addToBot(new MakeTempCardInHandAction(AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON, AbstractDungeon.cardRandomRng).makeCopy(), 1, false));
                    AbstractCard c = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON, AbstractDungeon.cardRandomRng).makeCopy();
                    c.setCostForTurn(0);
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, true));
                }
            }
        }
    }

    //修改偏差认知效果，当集中为零时停止扣除
    @SpirePatch(
            clz= BiasPower.class ,
            method="atStartOfTurn"
    )
    public static class ModifyBiasPowerAtStartOfTurn {
        public static void Replace(AbstractPower __instance)
        {
            __instance.flash();
            if(__instance.owner.hasPower("Focus")) {
                int __instance_focus_amount = __instance.owner.getPower("Focus").amount;
                if (__instance_focus_amount - __instance.amount >= 0)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance.owner, __instance.owner, new FocusPower(__instance.owner, -__instance.amount), -__instance.amount));
                else
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance.owner, __instance.owner, new FocusPower(__instance.owner, -__instance_focus_amount), -__instance_focus_amount));
            }
        }
    }

}
