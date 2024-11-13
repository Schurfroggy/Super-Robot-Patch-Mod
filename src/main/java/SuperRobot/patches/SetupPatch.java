package SuperRobot.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.FrozenCore;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;


public class SetupPatch {
    public static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("SuperRobot:Defect");

    //增加冻核遗物
    @SpirePatch(
            clz= Defect.class,
            method="getStartingRelics"
    )
    public static class getStartingRelics
    {
        public static ArrayList<String> Replace()
        {
            ArrayList<String> retVal = new ArrayList<>();
            retVal.add("Cracked Core");
            retVal.add(FrozenCore.ID);
            return retVal;
        }
    }

    //todo 修改Defect的NAMES属性，覆盖Defect的静态代码块（不知道为什么静态代码块读取的NAMES名没有改变界面上的显示）
    /*@SpirePatch(
            clz= Defect.class,
            method="<ctor>"
    )
    public static class modifyNames
    {
        @SpirePostfixPatch
        public static void ModifyEffect(Defect __instance){
            __instance.NAMES=((CharacterStrings)ReflectionHacks.getPrivateStatic(Defect.class,"characterStrings")).NAMES;
        }
    }*/



}
