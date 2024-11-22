package SuperRobot.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SleepPower extends AbstractPower {
    public static final String POWER_ID = "SuperRobot:Sleep Power";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public SleepPower(AbstractCreature owner){
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount=-1;

        String path128 = "img/powers/SleepPower84.png";
        String path48 = "img/powers/SleepPower32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    public float atDamageFinalGive(float damage, DamageInfo.DamageType type){
        if(damage>1.0F)
            damage=1.0F;
        return damage;
    }

    public void atEndOfRound() {
        this.flash();
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "SuperRobot:Sleep Power"));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
