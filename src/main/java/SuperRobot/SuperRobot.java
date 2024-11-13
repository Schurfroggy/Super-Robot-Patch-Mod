package SuperRobot;

import SuperRobot.cards.FrostProtection;
import SuperRobot.cards.MachineAwakening;
import basemod.BaseMod;

import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

@SpireInitializer
public class SuperRobot implements  EditStringsSubscriber,EditCardsSubscriber{
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
        BaseMod.loadCustomStringsFile(CardStrings.class, "localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization/" + lang + "/characters.json");
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new FrostProtection());
        BaseMod.addCard(new MachineAwakening());
    }


}
