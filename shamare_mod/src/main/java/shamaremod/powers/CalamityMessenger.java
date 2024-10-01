package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class CalamityMessenger extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("CalamityMessenger");
    
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public CalamityMessenger(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;

         // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "CalamityMessenger_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "CalamityMessenger_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }
    
    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }

    public void onCardDraw(AbstractCard card) {
        boolean is_disaster = false;
        is_disaster=(card.cardID.equals("ShamareKhas:FireDisaster")||
                    card.cardID.equals("ShamareKhas:HolyDisaster")||
                    card.cardID.equals("ShamareKhas:PoisonDisaster")||
                    card.cardID.equals("ShamareKhas:ShadowDisaster"));
        if (is_disaster && !this.owner.hasPower("No Draw")) {
        flash();
        addToBot((AbstractGameAction)new DrawCardAction(this.owner, this.amount));
        } 
  }

    

    
}