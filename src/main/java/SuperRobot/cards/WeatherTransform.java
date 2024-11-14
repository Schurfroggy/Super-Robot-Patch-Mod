package SuperRobot.cards;

import SuperRobot.actions.RemoveWeatherAction;
import SuperRobot.effect.WeatherEffect;
import SuperRobot.powers.SnowStormPower;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StormPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class WeatherTransform extends CustomCard {

    public static final String ID = "SuperRobot:Weather Transform";
    private static final String IMG_PATH="img/cards/WeatherTransform.png";
    private static final CardStrings CARD_STRINGS= CardCrawlGame.languagePack.getCardStrings(ID);
    public WeatherTransform(){
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 2, CARD_STRINGS.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);
    }
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.hasPower("Storm")){
            int _amount=AbstractDungeon.player.getPower("Storm").amount;
            this.addToBot(new ApplyPowerAction(p, p, new SnowStormPower(p, _amount), _amount));
            this.addToBot(new RemoveWeatherAction(p,p,"Storm"));

            CardCrawlGame.sound.play("STANCE_ENTER_CALM");
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
            AbstractDungeon.effectsQueue.add(new WeatherEffect("SuperRobot:SnowStorm"));
        }
        else if(AbstractDungeon.player.hasPower("SuperRobot:SnowStorm")){
            int _amount=AbstractDungeon.player.getPower("SuperRobot:SnowStorm").amount;
            this.addToBot(new ApplyPowerAction(p, p, new StormPower(p, _amount), _amount));
            this.addToBot(new RemoveSpecificPowerAction(p,p,"SuperRobot:SnowStorm"));

            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE");
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.YELLOW, true));
            AbstractDungeon.effectsQueue.add(new WeatherEffect("Storm"));
        }
    }

    @Override
    public void triggerOnExhaust(){
        AbstractPlayer p= AbstractDungeon.player;
        if(p.hasPower("Storm"))
            this.addToBot(new RemoveWeatherAction(p,p,"Storm"));
        if(p.hasPower("SuperRobot:SnowStorm"))
            this.addToBot(new RemoveWeatherAction(p,p,"SuperRobot:SnowStorm"));

        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
        AbstractDungeon.effectsQueue.add(new WeatherEffect("Clear Up"));
    }

    @Override
    public AbstractCard makeCopy() {
        return new WeatherTransform();
    }
}
