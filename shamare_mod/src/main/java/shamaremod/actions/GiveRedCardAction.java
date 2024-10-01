package shamaremod.actions;

import java.util.ArrayList;
import java.util.Random;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class GiveRedCardAction extends AbstractGameAction {
    private boolean retrieveCard = false;

    public GiveRedCardAction() {
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
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                disCard.setCostForTurn(0);
                disCard.current_x = -1000.0F * Settings.xScale;
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

        
    private AbstractCard random_red_card_getter() {
        Random rand = new Random();
        int roll = rand.nextInt(100);
        AbstractCard.CardRarity rarity;
        if (roll < 40) {
            rarity = AbstractCard.CardRarity.COMMON;
        } else if (roll < 80) {
            rarity = AbstractCard.CardRarity.UNCOMMON;
        } else {
            rarity = AbstractCard.CardRarity.RARE;
        }
        AbstractCard tmp = CardLibrary.getAnyColorCard(rarity);
        while (tmp.color != AbstractCard.CardColor.RED||tmp.type==AbstractCard.CardType.ATTACK) {
            tmp = CardLibrary.getAnyColorCard(rarity);
        }
        return tmp.makeCopy();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        while (choices.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp = this.random_red_card_getter();
            for (AbstractCard c : choices) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe) {
                choices.add(tmp.makeCopy());
            }
        }
        return choices;
    }
}