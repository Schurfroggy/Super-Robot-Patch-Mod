package SuperRobot.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FrostProtectionPower extends AbstractPower {
    public static final String POWER_ID = "SuperRobot:FrostProtection";
    private static final PowerStrings powerStrings;

    public FrostProtectionPower(AbstractCreature owner, int frostAmount) {
        this.name = powerStrings.NAME;
        this.ID = "SuperRobot:FrostProtection";
        this.owner = owner;
        this.amount = frostAmount;

        String path128 = "img/powers/FrostProtectionPower84.png";
        String path48 = "img/powers/FrostProtectionPower32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && damageAmount > 0) {
            this.flash();

            for(int i = 0; i < this.amount; ++i) {
                this.addToTop(new ChannelAction(new Frost()));
            }
        }
        return damageAmount;
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}
