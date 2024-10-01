package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;


public class NamesisExplosionPower extends AbstractPower {
  public static final String POWER_ID = IdHelper.makePath("NamesisExplosionPower");
  
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  
  public static final String NAME = powerStrings.NAME;
  
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  
  private int namesis_amt = 0;
  
  private static int CambrianIdOffset;
  
  public NamesisExplosionPower(AbstractCreature owner, int turns, int namesis_amt) {
    this.name = NAME;
    this.ID = POWER_ID + CambrianIdOffset;
    CambrianIdOffset++;
    this.owner = owner;
    this.amount = turns;
    this.namesis_amt = namesis_amt;
    
    // 添加一大一小两张能力图
    String path128 = ImageHelper.getOtherImgPath("powers","NamesisExplosionPower_96");
    String path48 = ImageHelper.getOtherImgPath("powers","NamesisExplosionPower_35");
    this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
    this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);


    updateDescription();
  }
  
  public void atEndOfTurn(boolean isPlayer) {
    if(this.amount > 1){
        addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
    }
    else if(this.amount == 1){
        addToBot(new RemoveSpecificPowerAction(this.owner,this.owner, this));
        if(this.namesis_amt > 0){
            //apply namesis
            addToBot(new ApplyPowerAction(this.owner, this.owner, new Namesis(this.owner, this.namesis_amt),this.namesis_amt, true, AbstractGameAction.AttackEffect.NONE));

        }
    }
  }
  
  public void updateDescription() {
    this.description = String.format(DESCRIPTIONS[0], this.amount,this.namesis_amt); // 这样，%d就被替换成能力的层数
  }
}
