package SuperRobot;

import SuperRobot.cards.*;
import basemod.BaseMod;

import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

@SpireInitializer
public class SuperRobot implements  EditStringsSubscriber,EditCardsSubscriber, EditKeywordsSubscriber{
    public SuperRobot(){
        BaseMod.subscribe(this);
    }

    public static void initialize(){
        new SuperRobot();
    }


    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "superrobot_localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "superrobot_localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "superrobot_localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "superrobot_localization/" + lang + "/characters.json");
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new NewDualcast());
        BaseMod.addCard(new FrostProtection());
        BaseMod.addCard(new MachineAwakening());
        BaseMod.addCard(new SnowStorm());
        BaseMod.addCard(new WeatherTransform());
        BaseMod.addCard(new RobotVault());
    }


    @Override
    public void receiveEditKeywords() {
        BaseMod.addKeyword("Machine Learning",new String[]{"machine","learning"},"AI will channel random Orb based on your situation at the end of turn.");
        BaseMod.addKeyword("机器学习",new String[]{"机器学习"},"在你的回合结束时，AI会根据你的状态调整参数生成充能球。");
        BaseMod.addKeyword("Weather",new String[]{"weather"},"You can only have one type of weather at the same time. Weather will increase or decrease the effectiveness of specific orb.");
        BaseMod.addKeyword("天气",new String[]{"天气"},"你只能同时拥有一种天气。天气会增加或削弱特定充能球的效果。");
        BaseMod.addKeyword("Clear Up",new String[]{"clear","up"},"Become sunny:)");
        BaseMod.addKeyword("放晴",new String[]{"放晴"},"退出所有天气。");
        BaseMod.addKeyword("Deep Sleep",new String[]{"deep","sleep"},"All the damage you cause is reduced to 1 when your fell asleep. Wake up in new round.");
        BaseMod.addKeyword("沉睡",new String[]{"沉睡"},"沉睡状态下，你所有的攻击伤害降低为1点。新的回合开始时苏醒。");
    }
}
