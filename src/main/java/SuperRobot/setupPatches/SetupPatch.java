package SuperRobot.setupPatches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.FrozenCore;

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

        @SpireInsertPatch(
                rloc=2,
                localvars={"retVal"}
        )
        public static void ModifyRelics(ArrayList<String> retVal)
        {
            retVal.add(FrozenCore.ID);
        }
    }

    //替换双放卡
    @SpirePatch(
            clz= Defect.class,
            method="getStartingDeck"
    )
    public static class getStartingDeck
    {
        @SpireInsertPatch(
                rloc=11,
                localvars={"retVal"}
        )
        public static void ModifyDeck(ArrayList<String> retVal)
        {
            retVal.remove("Dualcast");
            retVal.add("SuperRobot:NewDualcast");
        }
    }

}
