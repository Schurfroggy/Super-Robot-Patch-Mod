package SuperRobot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class MultiProgramAction extends AbstractGameAction {
    private boolean freeToPlayOnce = false;
    private AbstractPlayer p;
    private int energyOnUse = -1;
    private boolean upgraded;
    public MultiProgramAction(AbstractPlayer p, int energyOnUse, boolean upgraded, boolean freeToPlayOnce) {
        this.p = p;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.freeToPlayOnce = freeToPlayOnce;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.upgraded) {
            effect*=2;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            for(int i = 0; i < effect - 1; ++i) {
                this.addToBot(new ApplyPowerAction(p, p, new FocusPower(p, -effect), -effect));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, effect), effect, true, AbstractGameAction.AttackEffect.NONE));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, effect), effect, true, AbstractGameAction.AttackEffect.NONE));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
