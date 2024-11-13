package SuperRobot.cards;

import SuperRobot.actions.MultiProgramAction;
import SuperRobot.powers.MachineLearningPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
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

    //增强beam cell，同时给予一层虚弱，升级后变为两层虚弱
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

    //增强go for the eyes，同时提升自身1层敏捷
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

    //增强hello world，升级后费用变为零,你好世界发出的牌首轮为零费
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

    //修改暴雪，初始改为造成冰球数量的4倍伤害，升级后造成五倍，并生成1颗冰球，但是变为金卡
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
            __instance.rarity=AbstractCard.CardRarity.RARE;
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
            if(__instance.upgraded)
                c.upgrade();
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

    //修改彩虹，同时增加一个球位，升级后减一点费用，仍然为消耗
    @SpirePatch(
            clz= Rainbow.class,
            method="use"
    )
    public static class ModifyRainbow {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(1));
        }
    }

    @SpirePatch(
            clz= Rainbow.class,
            method="upgrade"
    )
    public static class UpgradeRainbow {
        @SpireInsertPatch(
                rloc=5
        )
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.exhaust=true;
            __instance.cost = __instance.costForTurn = 1;
            __instance.upgradedCost = true;
        }
    }

    //增强核心电涌，改为蓝卡
    @SpirePatch(
            clz= CoreSurge.class,
            method="<ctor>"
    )
    public static class ModifyCoreSurge {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.rarity = AbstractCard.CardRarity.UNCOMMON;
        }
    }

    //修改狂乱撕扯，改为白卡
    @SpirePatch(
            clz= RipAndTear.class,
            method="<ctor>"
    )
    public static class ModifyMadRiposte {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.rarity = AbstractCard.CardRarity.COMMON;
        }
    }

    //增强陨石打击，初始费用为4费
    @SpirePatch(
            clz= MeteorStrike.class,
            method="<ctor>"
    )
    public static class ModifyMeteorStrike {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.cost = __instance.costForTurn = 4;
        }
    }

    //todo 增强Rebound，改为弹回到手牌中
    /*@SpirePatch(
            clz= Rebound.class,
            method="use"
    )*/

    //修改重编程，改为多重编程，消耗，耗X费，降低X点集中，提升X点力量和敏捷，升级后全部变为2X
    @SpirePatch(
            clz= Reprogram.class,
            method="<ctor>"
    )
    public static class ModifyReprogram {
        @SpirePostfixPatch
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.cost = -1;
            __instance.exhaust=true;
        }
    }

    @SpirePatch2(
            clz= Reprogram.class,
            method="use"
    )
    public static class ModifyReprogram2 {
        @SpirePrefixPatch
        public static SpireReturn<?> ModifyEffect(AbstractPlayer p,AbstractMonster m, AbstractCard __instance)
        {
            AbstractDungeon.actionManager.addToBottom(new MultiProgramAction(p,__instance.energyOnUse,__instance.upgraded,__instance.freeToPlayOnce));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz= Reprogram.class,
            method="upgrade"
    )
    public static class ModifyReprogram3 {
        @SpireInsertPatch(
                rloc=3
        )
        public static void modifyEffect(AbstractCard __instance)
        {
            __instance.rawDescription = ((CardStrings) ReflectionHacks.getPrivateStatic(Reprogram.class, "cardStrings")).UPGRADE_DESCRIPTION;
            __instance.initializeDescription();
        }
    }

    //增强创造性AI，三选一能力牌而不是随机，升级后不减费用而是变为固有
    @SpirePatch(
            clz= CreativeAI.class,
            method="upgrade"
    )
    public static class ModifyCreativeAI {
        @SpireInsertPatch(
                rloc=2
        )
        public static SpireReturn<?> modifyEffect(AbstractCard __instance)
        {
            __instance.isInnate=true;
            __instance.rawDescription = ((CardStrings) ReflectionHacks.getPrivateStatic(CreativeAI.class, "cardStrings")).UPGRADE_DESCRIPTION;
            __instance.initializeDescription();
            return SpireReturn.Return();
        }
    }

    //修改机器学习，回合结束时，根据AI结果生成一个充能球。升级后无固有，费用减一，改为蓝卡
    @SpirePatch2(
            clz= MachineLearning.class,
            method="<ctor>"
    )
    public static class ModifyMachineLearning {
        @SpirePostfixPatch
        public static void ModifyEffect(AbstractCard __instance)
        {
            __instance.rarity=AbstractCard.CardRarity.UNCOMMON;
        }
    }

    @SpirePatch2(
            clz= MachineLearning.class,
            method="use"
    )
    public static class ModifyMachineLearning2 {
        @SpirePrefixPatch
        public static SpireReturn<?> ModifyEffect(AbstractPlayer p,AbstractMonster m,AbstractCard __instance)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,new MachineLearningPower(p,__instance.magicNumber)));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz=MachineLearning.class,
            method="upgrade"
    )
    public static class ModifyMachineLearning3 {
        @SpireInsertPatch(
                rloc=2
        )
        public static SpireReturn<?> ModifyEffect(AbstractCard __instance)
        {
            __instance.cost=__instance.costForTurn=0;
            __instance.upgradedCost=true;
            return SpireReturn.Return();
        }
    }









}
