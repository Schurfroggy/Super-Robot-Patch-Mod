package SuperRobot.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
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



}
