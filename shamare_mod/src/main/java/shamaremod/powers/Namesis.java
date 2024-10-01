package shamaremod.powers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.cards.skill.DarkAffine;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class Namesis extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("Namesis");
    
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private List<AbstractCard> dynamicCostCards = new ArrayList<>();
    private boolean has_triggered_this_turn1 = false;
    private boolean has_triggered_this_turn2 = false;
    
    public Namesis(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.DEBUFF;
        this.isTurnBased = true;


        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "Namesis_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "Namesis_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);

        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals("ShamareKhas:DarkAffine") && !dynamicCostCards.contains(card)) {
                if (!dynamicCostCards.contains(card)) {
                    dynamicCostCards.add(card);
                }
                ((DarkAffine) card).adjustCostBasedOnNamesis_by_amount(this.amount);
            }
        }

        
    }


     public void addDynamicCostCard(AbstractCard card) {
        if (!dynamicCostCards.contains(card)) {
            dynamicCostCards.add(card);
        }
    }

    public void removeDynamicCostCard(AbstractCard card) {
        dynamicCostCards.remove(card);
    }

    private void notifyDynamicCostCards() {
        for (AbstractCard card : dynamicCostCards) {
            if (card instanceof DarkAffine) {
                ((DarkAffine)card).adjustCostBasedOnNamesis();
            }
        }
    }

    public  void notifyDynamicCostCards_whenRemoved() {
        for (AbstractCard card : dynamicCostCards) {
            if (card instanceof DarkAffine) {
                ((DarkAffine)card).adjustCostBasedOnNamesis_whenRemoved();
            }
        }
    }

    private  void register_to_namesis_by_hand(){
          for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals("ShamareKhas:DarkAffine") && !dynamicCostCards.contains(card)) {
                addDynamicCostCard(card);
                ((DarkAffine) card).adjustCostBasedOnNamesis();
            }
        }
    }
    
    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
        notifyDynamicCostCards();
    }
    @Override
    public void atStartOfTurnPostDraw() {
        this.has_triggered_this_turn1 = false;
        this.has_triggered_this_turn2 = false;
  }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if(!this.owner.hasPower("ShamareKhas:Pardon")){

            this.notifyDynamicCostCards_whenRemoved();
            //if has pardon , won't be triggered
            if (info.owner != null && info.owner != this.owner && damageAmount> 0 &&info.type!=DamageType.THORNS) {
                if(!this.has_triggered_this_turn1){
                    addToBot(new LoseHPAction(this.owner, this.owner, this.amount));
                    this.has_triggered_this_turn1 = true;
                    addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
                }

            }

             // 检查是否有PeacefulPsyche
             if (this.owner.hasPower("ShamareKhas:PeacefulPsyche")) {
                AbstractPower peacefulPsyche = this.owner.getPower("ShamareKhas:PeacefulPsyche");
                if (peacefulPsyche instanceof PeacefulPsyche) {
                    if(!this.has_triggered_this_turn2){
                        ((PeacefulPsyche) peacefulPsyche).onNamesisTriggered();
                        this.has_triggered_this_turn2 = true;
                    }
                  
                }
            }

        }
        
    }

    public void trigger_by_hand(){
         //if has pardon , won't be triggered
        if(!this.owner.hasPower("ShamareKhas:Pardon")){
            this.notifyDynamicCostCards_whenRemoved();
           
            addToBot(new LoseHPAction(this.owner, this.owner, this.amount));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
                
             // 检查是否有PeacefulPsyche
             if (this.owner.hasPower("ShamareKhas:PeacefulPsyche")) {
                AbstractPower peacefulPsyche = this.owner.getPower("ShamareKhas:PeacefulPsyche");
                if (peacefulPsyche instanceof PeacefulPsyche) {
                    ((PeacefulPsyche) peacefulPsyche).onNamesisTriggered();
                }
            }

        }
    }

    



    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.register_to_namesis_by_hand();
        notifyDynamicCostCards();
    }
}