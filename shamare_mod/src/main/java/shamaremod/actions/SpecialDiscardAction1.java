package shamaremod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import shamaremod.cards.status.ShadowDisaster;

public class SpecialDiscardAction1 extends AbstractGameAction {
    private AbstractPlayer player;
    private boolean isRandom;
    private boolean endTurn;
    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    private static final String[] TEXT = uiStrings.TEXT;
    int action_mode = 0;

    public SpecialDiscardAction1(AbstractPlayer player, int amount, boolean isRandom, boolean endTurn,int mode) {
        this.player = player;
        this.isRandom = isRandom;
        this.endTurn = endTurn;
        this.amount = amount;
        this.duration = DURATION;
        this.actionType = ActionType.DISCARD;
        this.action_mode = mode;
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            if (this.player.hand.size() <= this.amount) {
                this.amount = this.player.hand.size();
                int tmp = this.player.hand.size();
                for (int i = 0; i < tmp; i++) {
                    AbstractCard c = this.player.hand.getTopCard();
                    this.player.hand.moveToDiscardPile(c);
                    if (!this.endTurn) c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(this.endTurn);
                    // 检查弃掉的牌是否是诅咒或状态牌
                    if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                        if(this.action_mode == 0){
                            addToBot(new GainEnergyAction(1));
                            addToBot(new DrawCardAction(player, 1));
                        }
                        else if(this.action_mode == 1){
                            addToBot(new DrawCardAction(player, 1));
                            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new ShadowDisaster(), 1, true, true));
                        }
                    }
                }
                AbstractDungeon.player.hand.applyPowers();
                tickDuration();
                return;
            }

            if (this.isRandom) {
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard c = this.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                    this.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(this.endTurn);
                    // 检查弃掉的牌是否是诅咒或状态牌
                    if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                        if(this.action_mode == 0){
                            addToBot(new GainEnergyAction(1));
                            addToBot(new DrawCardAction(player, 1));
                        }
                        else if(this.action_mode == 1){
                            addToBot(new DrawCardAction(player, 1));
                            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new ShadowDisaster(), 1, true, true));
                        }
                    }
                }
            } else {
                if (this.amount < 0) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
                    AbstractDungeon.player.hand.applyPowers();
                    tickDuration();
                    return;
                }
                if (this.player.hand.size() > this.amount)
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false);
                AbstractDungeon.player.hand.applyPowers();
                tickDuration();
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(this.endTurn);
                // 检查弃掉的牌是否是诅咒或状态牌
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                    if(this.action_mode == 0){
                        addToBot(new GainEnergyAction(1));
                        addToBot(new DrawCardAction(player, 1));
                    }
                    else if(this.action_mode == 1){
                        addToBot(new DrawCardAction(player, 1));
                        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new ShadowDisaster(), 1, true, true));
                    }
                }
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        tickDuration();
    }
}