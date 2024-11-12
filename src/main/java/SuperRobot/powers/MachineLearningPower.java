package SuperRobot.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MachineLearningPower extends AbstractPower {
    public static final String POWER_ID = "Machine";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private double[] weights=new double[4];

    private int lastHealth = 0;
    private  int lastDamage=0;
    public MachineLearningPower(AbstractPlayer owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        /*String path128 = "resources/img/powers/Machine84.png";
        String path48 = "resources/img/powers/Machine32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);*/

        this.loadRegion("ai");

        this.updateDescription();

        //记录当前玩家血量
        lastHealth = this.owner.currentHealth;

        //各充能球初始比重：闪、冰、黑、等
        weights[0]=10.0;
        weights[1]=10.0;
        weights[2]=10.0;
        weights[3]=8.0;
    }

    public void updateDescription(){
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void atEndOfTurn(boolean isPlayer){
        calculateWeight();
        switch (chooseBall()){
            case 0:{
                AbstractDungeon.player.channelOrb(new Lightning());
                weights[0]-=1.0;
                break;
            }
            case 1:{
                AbstractDungeon.player.channelOrb(new Frost());
                weights[0]-=1.0;
                break;
            }
            case 2:{
                AbstractDungeon.player.channelOrb(new Dark());
                weights[0]-=1.0;
                break;
            }
            case 3:{
                AbstractDungeon.player.channelOrb(new Plasma());
                weights[0]-=1.0;
                break;
            }
        }
    }

    private void  calculateWeight(){
        AbstractPlayer p= AbstractDungeon.player;
        if(p.damagedThisCombat-this.lastDamage>p.currentBlock){
            weights[0]+=0.5;
            weights[2]+=0.5;
        }
        if(p.currentHealth<lastHealth){
            weights[0]-=0.5;
            weights[1]+=0.5;
            weights[2]-=0.5;
        }else{
            weights[0]+=0.5;
            weights[1]-=0.5;
            weights[2]+=0.5;
        }
        if(p.currentBlock>10)
            weights[1]+=0.25;
        if(p.maxOrbs>=4)
            weights[2]-=0.5;
        if(p.hasPower("Focus")){
            weights[0]+=0.25;
            weights[1]+=0.5;
            weights[2]-=0.5;
            weights[3]-=0.25;
        }else{
            weights[0]-=0.5;
            weights[1]-=0.25;
            weights[2]+=0.5;
            weights[3]+=0.25;
        }
        if(p.hasEmptyOrb()){
            weights[3]+=0.5;
        }
        if(p.energy.energy==0){
            weights[3]+=0.5;
        }else{
            weights[3]-=0.25;
        }
        this.lastHealth=p.currentHealth;
        this.lastDamage+=p.damagedThisCombat;
    }

    private int chooseBall(){
        double maxWeight=weights[0];
        int maxIndex=0;
        for(int i=0;i<4;i++){
            if(weights[i]>maxWeight){
                maxWeight=weights[i];
                maxIndex=i;
            }
        }
        return maxIndex;
    }
}
