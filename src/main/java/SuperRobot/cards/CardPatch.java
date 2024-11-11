package SuperRobot.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.*;
import javassist.*;

public class CardPatch {
    //增强barrage，初始伤害变为6点，升级后变为8点
    @SpirePatch(
            clz= Barrage.class,
            method="<ctor>"
    )
    public static class ReplaceBarrage {
        @SpirePostfixPatch
        public static void modifyDamage(AbstractCard __instance)
        {
            __instance.baseDamage = 6;
        }
    }

    //增强claw，升级后所有claw牌增加3点伤害（而不是两点）
    @SpirePatch(
            clz= Claw.class,
            method="upgrade"
    )
    public static class UpgradeClaw {
        @SpireInsertPatch(
                rloc=3
        )
        public static void modifyDamage(AbstractCard __instance)
        {
            __instance.baseMagicNumber = 3;
            __instance.magicNumber = __instance.baseMagicNumber;
            __instance.upgradedMagicNumber = true;
        }
    }

    //增强beam cell，同时给予一层虚弱，升级后变为两层虚弱，并修改描述
    @SpirePatch(
            clz= BeamCell.class,
            method="use",
            paramtypez={AbstractPlayer.class, AbstractMonster.class}
    )
    public static class UseBeamCell {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance,AbstractPlayer p, AbstractMonster m)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, __instance.magicNumber, false), __instance.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    //增强go for the eyes，同时高提升自身1层灵巧
    @SpirePatch(
            clz= GoForTheEyes.class,
            method="use"
    )
    public static class UseGoForTheEyes {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance,AbstractPlayer p, AbstractMonster m)
        {
            if(m != null && m.getIntentBaseDmg() >= 0)
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    //增强steam barrier，修改基础格挡为8点
    @SpirePatch(
            clz= SteamBarrier.class,
            method="<ctor>"
    )
    public static class ReplaceSteam {
        @SpirePostfixPatch
        public static void modifyBlock(AbstractCard __instance)
        {
            __instance.baseBlock = 8;
        }
    }

    //增强skim，修改其为普通卡
    @SpirePatch(
            clz= Skim.class,
            method="<ctor>"
    )
    public static class ReplaceSkim {
        @SpirePostfixPatch
        public static void modifyCard(AbstractCard __instance)
        {
            __instance.rarity = AbstractCard.CardRarity.COMMON;
        }
    }

    //增强conserve battery，下回合获得两费
    @SpirePatch2(
            clz= ConserveBattery.class,
            method="use"
    )
    public static class UseConserveBattery {
        @SpireInsertPatch(
                rloc=1
        )
        public static SpireReturn<?> modifyEffect(AbstractPlayer p)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedBluePower(p, 2), 1));
            return SpireReturn.Return();
        }
    }

    //增强hello world，升级后费用变为零
    //你好世界发出的牌首轮为零费(在power中修改)
    @SpirePatch(
            clz= HelloWorld.class,
            method="upgrade"
    )
    public static class UpgradeHelloWorld {
        @SpirePostfixPatch
        public static void modifyCost(AbstractCard __instance)
        {
            __instance.cost = 0;
            __instance.costForTurn = 0;
            __instance.isCostModified = true;
        }
    }

    //增强冰川，升级后改为生成三个冰球
    @SpirePatch(
            clz= Glacier.class,
            method="upgrade"
    )
    public static class UpgradeGlacier {
        @SpireInsertPatch(
                rloc=5
        )
        public static void modifyDamage(AbstractCard __instance)
        {
            __instance.baseMagicNumber = 3;
            __instance.magicNumber = __instance.baseMagicNumber;
            __instance.isMagicNumberModified=true;
        }
    }

    //增强暴雪，初始改为造成冰球数量的4倍伤害，升级后造成五倍，并生成1颗冰球
    @SpirePatch(
            clz= Blizzard.class,
            method="<ctor>"
    )
    public static class ModifyBlizzard {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.baseMagicNumber = 4;
            __instance.magicNumber = __instance.baseMagicNumber;
        }
    }

    @SpirePatch(
            clz= Blizzard.class,
            method="use"
    )
    public static class UseBlizzard2 {
        @SpirePrefixPatch
        public static void modifyBlizzardEffect(AbstractCard __instance)
        {
            if(__instance.upgraded)
                AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Frost()));
        }
    }

    @SpirePatch(
            clz= Blizzard.class,
            method="upgrade"
    )
    public static class UpgradeBlizzard {
        @SpirePostfixPatch
        public static void modifyBlizzardDescription(AbstractCard __instance)
        {
            __instance.rawDescription = ((CardStrings) ReflectionHacks.getPrivateStatic(Blizzard.class, "cardStrings")).UPGRADE_DESCRIPTION;
            //__instance.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            __instance.initializeDescription();
        }
    }

    //增强超频，改为添加一张晕眩而不是灼伤
    @SpirePatch(
            clz= Overclock.class,
            method="<ctor>"
    )
    public static class ModifyOverclockDescription {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.cardsToPreview = new Dazed();
        }
    }

    @SpirePatch2(
            clz= Overclock.class,
            method="use"
    )
    public static class ModifyOverclock {
        @SpireInsertPatch(
                rloc=1
        )
        public static SpireReturn<?> modifyEffect(AbstractCard __instance)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), 1));
            return SpireReturn.Return();
        }
    }

    //修改搜寻，升级后不增加抽牌数，但是拥有固有属性
    @SpirePatch(
            clz= Seek.class,
            method="upgrade"
    )
    public static class ModifySearch {
        @SpireInsertPatch(
                rloc=4
        )
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.magicNumber = __instance.baseMagicNumber = 1;
            __instance.upgradedMagicNumber=false;
            __instance.isInnate = true;
            __instance.rawDescription = ((CardStrings) ReflectionHacks.getPrivateStatic(Seek.class, "cardStrings")).UPGRADE_DESCRIPTION;
        }
    }

    //增强混沌，基础生成两个，升级后生成三个
    @SpirePatch2(
            clz=Chaos.class,
            method="<ctor>"
    )
    public static class ModifyChaos {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.showEvokeOrbCount = 2;
            __instance.baseMagicNumber = 2;
            __instance.magicNumber = __instance.baseMagicNumber;
        }
    }

    @SpirePatch2(
            clz=Chaos.class,
            method="use"
    )
    public static class ModifyUseChaos {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(AbstractOrb.getRandomOrb(true)));
        }
    }

    //增强聚变，增加抽二效果
    @SpirePatch2(
            clz= Fusion.class,
            method="use"
    )
    public static class ModifyFusion {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractPlayer p, AbstractMonster m)
        {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 2));
        }
    }

    //增强auto shield，球未满时额外提供5格挡，升级后额外提供7格挡
    @SpirePatch(
            clz= AutoShields.class,
            method="<ctor>"
    )
    public static class ModifyAutoShields {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.baseMagicNumber = 5;
            __instance.magicNumber=__instance.baseMagicNumber;
        }
    }

    @SpirePatch2(
            clz= AutoShields.class,
            method="use"
    )
    public static class ModifyAutoShields2 {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractPlayer p,AbstractCard __instance)
        {
            if(AbstractDungeon.player.hasEmptyOrb())
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, __instance.magicNumber));
        }
    }

    @SpirePatch(
            clz= AutoShields.class,
            method="upgrade"
    )
    public static class UpgradeAutoShields {
        @SpireInsertPatch(
                rloc=3
        )
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.baseMagicNumber = 7;
            __instance.magicNumber=__instance.baseMagicNumber;
            __instance.upgradedMagicNumber = true;
        }
    }

    //增强遗传算法，基础格挡为5点
    @SpirePatch(
            clz= GeneticAlgorithm.class,
            method="<ctor>"
    )
    public static class ModifyGeneticAlgorithm {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.misc = 5;
            __instance.baseBlock = __instance.misc;
        }
    }

    //修改回响形态，这张牌改为无法被逃脱，每被打出或消耗一次增加一费，初始为一费，升级后取消虚无
    @SpirePatch(
            clz= EchoForm.class,
            method="<ctor>"
    )
    public static class ModifyEchoForm {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.cost=__instance.costForTurn=1;
        }
    }

    @SpirePatch(
            clz= EchoForm.class,
            method="makeCopy"
    )
    public static class ModifyEchoForm0 {
        public static AbstractCard Replace(AbstractCard __instance)
        {
            AbstractCard c = new EchoForm();
            c.cost =__instance.cost;
            return c;
        }
    }

    @SpirePatch(
            clz= EchoForm.class,
            method="use"
    )
    public static class ModifyEchoForm1 {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            AbstractCard c = __instance.makeCopy();
            c.cost = c.costForTurn = __instance.cost+1;
            c.isCostModified = true;
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
        }
    }

    //由于EchoForm中没有显示地复写triggerOnExhaust方法，无法直接Override，需要对原EchoForm类的该方法进行补充
    @SpirePatch(
            clz = EchoForm.class,
            method = "<ctor>"
    )
    public static class ModifyEchoForm2{
        @SpireRawPatch
        public static void Raw(CtBehavior ctMethodToPatch) throws CannotCompileException,NotFoundException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            CtMethod method = CtNewMethod.make(CtClass.voidType, "triggerOnExhaust", new CtClass[0], (CtClass[])null, "{"+ModifyEchoForm2.class.getName()+".Do(this);}", ctClass);
            ctClass.addMethod(method);
        }
        public static void Do(AbstractCard __instance){
            AbstractCard c = __instance.makeCopy();
            c.cost = c.costForTurn = __instance.cost+1;
            c.isCostModified = true;
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
        }
    }

    


}