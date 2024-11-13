package SuperRobot.powers;

import SuperRobot.actions.ChooseOnePowerAction;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

public class PowerPatch {
    //增强你好世界效果为固定增加零费手牌
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

    //增强偏差认知效果，当集中为零时停止扣除
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

    //todo 增强弹回效果，将下一张牌弹回至手牌中
    /*@SpirePatch2(
            clz= ReboundPower.class,
            method="onAfterUseCard"
    )
    public static class ModifyReboundPowerOnAfterUseCard {
        @SpireInsertPatch(
                rloc = 6
        )
        public static void ModifyEffect(UseCardAction action)
        {
            action.reboundCard=false;
            action.returnToHand=true;
        }
    }*/

    //增强创造性AI效果，随机一张牌改为三选一，升级后不减费用而是变为固有
    @SpirePatch(
            clz=CreativeAIPower.class,
            method="atStartOfTurn"
    )
    public static class ModifyCreativeAIPowerAtStartOfTurn {
        public static void Replace(AbstractPower __instance)
        {
            for(int i = 0; i < __instance.amount; ++i) {
                AbstractDungeon.actionManager.addToBottom(new ChooseOnePowerAction());
            }
        }
    }

    @SpirePatch(
            clz=CreativeAIPower.class,
            method="updateDescription"
    )
    public static class ModifyCreativeAIPowerAtStartOfTurn2 {
        public static void Replace(AbstractPower __instance)
        {
            String[] DESCRIPTIONS = ((PowerStrings)ReflectionHacks.getPrivateStatic(CreativeAIPower.class, "powerStrings")).DESCRIPTIONS;
            if (__instance.amount > 1) {
                __instance.description = DESCRIPTIONS[0] + __instance.amount + DESCRIPTIONS[2];
            } else {
                __instance.description = DESCRIPTIONS[0] + __instance.amount + DESCRIPTIONS[1];
            }
        }
    }

    //增强增幅效果，跨回合依旧生效
    @SpirePatch2(
            clz=AmplifyPower.class,
            method="atEndOfTurn"
    )
    public static class ModifyAmplifyPowerAtEndOfTurn {
        @SpirePrefixPatch
        public static SpireReturn<?> Replace(boolean isPlayer)
        {
            return SpireReturn.Return();
        }
    }
}
