package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.cards.status.PoisonDisaster;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class Plague extends AbstractPower{

     public static final String POWER_ID = IdHelper.makePath("Plague");
    
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public Plague(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = true;

        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "Plague_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "Plague_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }
    
    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }

    public void atEndOfRound() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new PoisonDisaster(), this.amount));
        
      }

    
    
}
