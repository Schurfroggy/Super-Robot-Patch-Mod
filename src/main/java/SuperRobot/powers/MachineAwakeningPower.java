package SuperRobot.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MachineAwakeningPower extends AbstractPower {
    public static final String POWER_ID = "SuperRobot:Machine Awakening Power";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final int times;
    private final double[] weights=new double[4];
    private final double[] flags=new double[4];
    private int lastHealth = 0;
    public MachineAwakeningPower(AbstractCreature owner)
    {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = -1;
        String path128 = "img/powers/MachineAwakeningPower84.png";
        String path48 = "img/powers/MachineAwakeningPower32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

        //记录当前玩家血量
        lastHealth = this.owner.currentHealth;

        //各充能球初始比重：闪、冰、黑、等
        weights[0]=10.0;
        weights[1]=10.0;
        weights[2]=10.0;
        weights[3]=8.0;

        flags[0]=1.0;
        flags[1]=1.0;
        flags[2]=1.0;
        flags[3]=1.0;

        times=AbstractDungeon.player.maxOrbs/2;
    }

    public void updateDescription(){
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfTurn(boolean isPlayer){
        for(int i=0;i<times;i++){
            calculateWeight();
            switch (chooseBall()){
                case 0:{
                    AbstractDungeon.player.channelOrb(new Lightning());
                    weights[0]-=flags[0];
                    flags[0]+=0.5;
                    flags[1]-=0.25;
                    flags[2]-=0.25;
                    flags[3]-=0.25;
                    break;
                }
                case 1:{
                    AbstractDungeon.player.channelOrb(new Frost());
                    weights[1]-=flags[1];
                    flags[0]-=0.25;
                    flags[1]+=0.5;
                    flags[2]-=0.25;
                    flags[3]-=0.25;
                    break;
                }
                case 2:{
                    AbstractDungeon.player.channelOrb(new Dark());
                    weights[2]-=flags[2];
                    flags[0]-=0.25;
                    flags[1]-=0.25;
                    flags[2]+=0.5;
                    flags[3]-=0.25;
                    break;
                }
                case 3:{
                    AbstractDungeon.player.channelOrb(new Plasma());
                    weights[3]-=flags[3];
                    flags[0]-=0.25;
                    flags[1]-=0.25;
                    flags[2]-=0.25;
                    flags[3]+=0.5;
                    break;
                }
            }
        }
    }

    private void  calculateWeight(){
        AbstractPlayer p= AbstractDungeon.player;
        if(p.currentHealth<lastHealth){
            weights[0]-=0.25;
            weights[1]+=0.25;
            weights[2]-=0.25;
        }
        if((double)p.currentHealth<0.3*(double)p.maxHealth)
            weights[1]+=0.25;
        if(p.currentBlock>10)
            weights[1]+=0.25;
        if(p.currentBlock>=30)
            weights[1]-=1.0;
        if(p.hasPower("Focus")&&p.getPower("Focus").amount>0){
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
        if(p.energy.energy==0){
            weights[3]+=0.5;
        }else{
            weights[3]-=0.25;
        }
        if(p.hasPower("Storm"))
            weights[0]+=1.0;
        if(p.hasPower("SuperRobot:SnowStorm"))
            weights[1]+=1.0;
        this.lastHealth=p.currentHealth;
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
