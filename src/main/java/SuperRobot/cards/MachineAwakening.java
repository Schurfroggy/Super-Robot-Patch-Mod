package SuperRobot.cards;

import SuperRobot.powers.MachineAwakeningPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

public class MachineAwakening extends CustomCard {

    public static final String ID = "SuperRobot:Machine Awakening";
    private static final String IMG_PATH="img/cards/MachineAwakening.png";
    private static final CardStrings CARD_STRINGS= CardCrawlGame.languagePack.getCardStrings(ID);
    public MachineAwakening() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 3, CARD_STRINGS.DESCRIPTION, CardType.POWER, CardColor.BLUE, CardRarity.RARE, CardTarget.SELF);
        this.isEthereal=true;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded){
            this.upgradeName();
            this.isEthereal=false;
            this.rawDescription=CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(p.hasPower("SuperRobot:Machine")){
            this.addToBot(new ApplyPowerAction(p,p,new FocusPower(p,2*p.getPower("SuperRobot:Machine").amount)));
            this.addToBot(new RemoveSpecificPowerAction(p,p,"SuperRobot:Machine"));
            //this.addToBot(new ApplyPowerAction(p,p,new MachineLearningPower(p,-p.getPower("SuperRobot:Machine").amount)));
        }
        this.addToBot(new ApplyPowerAction(p, p,new MachineAwakeningPower(p)));
    }
    @Override
    public AbstractCard makeCopy() {
        return new MachineAwakening();
    }
}
