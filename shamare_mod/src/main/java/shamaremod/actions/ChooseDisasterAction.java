package shamaremod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import shamaremod.cards.status.FireDisaster;
import shamaremod.cards.status.PoisonDisaster;
import shamaremod.cards.status.ShadowDisaster;

public class ChooseDisasterAction extends AbstractGameAction {
    private boolean retrieveCard = false;

    public ChooseDisasterAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], true);
            tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                int get_related_magic_number = get_related_magic_number(AbstractDungeon.cardRewardScreen.discoveryCard);
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                disCard.setCostForTurn(0);
                disCard.current_x = -1000.0F * Settings.xScale;
                // 将选中的牌放两张到抽牌堆顶部
                AbstractCard card1=disCard;
                AbstractCard card2=disCard.makeStatEquivalentCopy();
                if(card1 instanceof FireDisaster)
                    ((FireDisaster) card1).set_magicnumber_by_hand(get_related_magic_number);
            
                else if(card1 instanceof ShadowDisaster)
                    ((ShadowDisaster) card1).set_magicnumber_by_hand(get_related_magic_number);

                else if(card1 instanceof PoisonDisaster)
                    ((PoisonDisaster) card1).set_magicnumber_by_hand(get_related_magic_number);

                //the same to card2
                if(card2 instanceof FireDisaster)
                    ((FireDisaster) card2).set_magicnumber_by_hand(get_related_magic_number);
            
                else if(card2 instanceof ShadowDisaster)
                    ((ShadowDisaster) card2).set_magicnumber_by_hand(get_related_magic_number);

                else if(card2 instanceof PoisonDisaster)
                    ((PoisonDisaster) card2).set_magicnumber_by_hand(get_related_magic_number);

                AbstractDungeon.player.drawPile.addToTop(card1);
                AbstractDungeon.player.drawPile.addToTop(card2);
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new FireDisaster());
        choices.add(new PoisonDisaster());
        choices.add(new ShadowDisaster());
        return choices;
    }

    private int get_related_magic_number(AbstractCard card){
        if(card instanceof FireDisaster){
            return FireDisaster.get_currentMagicNumberBonus()+12;
        }else if(card instanceof PoisonDisaster){
            return PoisonDisaster.get_currentMagicNumberBonus()+5;
        }else if(card instanceof ShadowDisaster){
            return ShadowDisaster.get_currentMagicNumberBonus()+9;
        }
        return 0;
    }
}