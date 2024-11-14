package SuperRobot.cards;

import SuperRobot.actions.RemoveWeatherAction;
import SuperRobot.effect.WeatherEffect;
import SuperRobot.powers.SnowStormPower;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class SnowStorm extends CustomCard {
    public static final String ID = "SuperRobot:Snow Storm";
    private static final String IMG_PATH="img/cards/SnowStorm.png";
    private static final CardStrings CARD_STRINGS= CardCrawlGame.languagePack.getCardStrings(ID);
    public SnowStorm(){
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION, CardType.POWER, CardColor.BLUE, CardRarity.RARE, CardTarget.SELF);
        this.cardsToPreview=new WeatherTransform();
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            AbstractCard c=new WeatherTransform();
            c.upgrade();
            this.cardsToPreview=c;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.hasPower("Storm"))
            this.addToBot(new RemoveWeatherAction(p,p,"Storm"));
        this.addToBot(new ApplyPowerAction(p, p, new SnowStormPower(p, this.magicNumber), this.magicNumber));

        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
        AbstractDungeon.effectsQueue.add(new WeatherEffect("SuperRobot:SnowStorm"));

        if(!this.upgraded){
            this.addToBot(new MakeTempCardInDrawPileAction(new WeatherTransform(), 1,true,true,false));
        }
        else{
            AbstractCard c=new WeatherTransform();
            c.upgrade();
            this.addToBot(new MakeTempCardInDrawPileAction(c, 1,true,true,false));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SnowStorm();
    }
}
