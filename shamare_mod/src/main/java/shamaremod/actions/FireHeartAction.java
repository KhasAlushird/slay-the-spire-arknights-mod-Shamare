package shamaremod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import shamaremod.cards.status.FireDisaster;

public class FireHeartAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean if_upgraded=false;

    public FireHeartAction(AbstractPlayer p,  boolean freeToPlayOnce, int energyOnUse,boolean if_upgraded) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.if_upgraded=if_upgraded;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }
        if(this.if_upgraded){
            effect+=1;
        }

        if (effect > 0) {
            int handSize = p.hand.size();
            int exhaustCount = Math.min(handSize, effect); 
           
            //exhaust
            addToBot(new ExhaustAction(exhaustCount, false, true, true));
            //take effect  
            addToBot(new MakeTempCardInDrawPileAction(new FireDisaster(), exhaustCount ,true, true));
             // 回复生命值
            addToBot(new com.megacrit.cardcrawl.actions.common.HealAction(p, p, exhaustCount));
            

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}