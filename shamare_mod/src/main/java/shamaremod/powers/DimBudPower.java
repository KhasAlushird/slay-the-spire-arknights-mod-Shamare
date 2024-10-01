package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.cards.status.ShadowDisaster;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class DimBudPower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("DimBudPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DimBudPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;

        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "DimBudPower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "DimBudPower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner != null  && damageAmount > 0) {
            if (info.type == DamageInfo.DamageType.HP_LOSS || info.type == DamageInfo.DamageType.NORMAL|| info.type == DamageInfo.DamageType.THORNS){

            
            flash();
            for (int i = 0; i < this.amount; i++) {
                AbstractCard card = new ShadowDisaster();
                addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
            }
          }
        
        }
    }
}