package shamaremod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TheFoolAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private final int discard_amt;
    //private boolean notchip;
    //public static boolean has_triggered_this_turn = false;

    public TheFoolAction(AbstractCreature source, int amount) {
        setValues((AbstractCreature)AbstractDungeon.player, source, -1);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.discard_amt = amount;
        this.duration = Settings.ACTION_DUR_FAST; // 确保正确设置 duration
        //this.notchip = notchip;
    }

    @Override
    public void update() {
            AbstractPlayer player = AbstractDungeon.player;
            if (this.duration == Settings.ACTION_DUR_FAST) {
                if (player.hand.size() > 0) {
                    // if (notchip) {
                    //     AbstractDungeon.handCardSelectScreen.open(TEXT[1], this.discard_amt, true, true);
                    //} else {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.discard_amt, true, true);
                    //}
                    addToBot(new WaitAction(0.25F));
                    tickDuration();
                    return;
                } 
            }
    
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                int disasterCount = 0;
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    player.hand.moveToDiscardPile(card);
                    GameActionManager.incrementDiscard(false);
                    card.triggerOnManualDiscard();
                    if (card.cardID.equals("ShamareKhas:FireDisaster") ||
                        card.cardID.equals("ShamareKhas:HolyDisaster") ||
                        card.cardID.equals("ShamareKhas:PoisonDisaster") ||
                        card.cardID.equals("ShamareKhas:ShadowDisaster")) {
                        disasterCount++;
                    }
                }
    
                for (int i = 0; i < disasterCount; i++) {
                    player.gainEnergy(1);
                    addToBot((AbstractGameAction)new DrawCardAction(player, 2));
                }
    
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                this.isDone = true;
            }
    
            tickDuration();
        }
        
    
}