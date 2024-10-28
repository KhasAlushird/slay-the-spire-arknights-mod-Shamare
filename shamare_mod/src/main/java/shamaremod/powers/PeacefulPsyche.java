package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class PeacefulPsyche extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("PeacefulPsyche");
    
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public PeacefulPsyche(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;

         // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "PeacefulPsyche_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "PeacefulPsyche_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }
    
    @Override
    public void updateDescription() {
        StringBuilder energySymbols = new StringBuilder();
        for (int i = 0; i < this.amount * 2; i++) {
            energySymbols.append("[E] ");
        }
        this.description = String.format(DESCRIPTIONS[0], this.amount*2,energySymbols.toString().trim());
    }

    public void onNamesisTriggered() {
        // 抽2张牌
        AbstractCreature p = this.owner;

         addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.amount*2), this.amount*2));
        // 获得2点能量
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, this.amount*2), this.amount*2));
    }

    public void onNamesisTriggered_by_hand() {
        AbstractCreature p = this.owner;

        this.addToBot(new GainEnergyAction(this.amount*2));
        addToBot(new DrawCardAction(p,  this.amount*2));

    }
}