package SuperRobot.cards;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Dualcast;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BasicCardPatch {
    //修改dualcast费用
    /*@SpirePatch(
            clz= Dualcast.class,
            method = "<ctor>"
    )
    public static class ReplaceDualCast{
        @SpirePostfixPatch
        public static void DualCast(AbstractCard __instance){
            __instance.assetUrl= "img/cards/NewDualcast.png";
            __instance.baseDamage=1;
            __instance.type= AbstractCard.CardType.ATTACK;
            __instance.target= AbstractCard.CardTarget.ENEMY;
        }
    }*/

    //修改dualcast升级方法
    /*@SpirePatch(
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
    }*/

    //修改dualcast使用方法
    /*@SpirePatch(
            clz=Dualcast.class,
            method="use"
    )
    public static class DualCastUse{
        @SpirePrefixPatch
        public static void ModifyEffect(AbstractCard __instance, AbstractPlayer p, AbstractMonster m){
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, __instance.damage, __instance.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }*/
}
