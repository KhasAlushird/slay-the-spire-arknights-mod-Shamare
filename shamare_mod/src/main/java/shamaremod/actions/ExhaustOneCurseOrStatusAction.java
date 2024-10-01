package shamaremod.actions;

import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExhaustOneCurseOrStatusAction extends AbstractGameAction {
  private float startingDuration = Settings.ACTION_DUR_FAST;

  public ExhaustOneCurseOrStatusAction() {
    this.duration = this.startingDuration;
  }

  public void update() {
    if (this.duration == this.startingDuration) {
      ArrayList<AbstractCard> curse_and_status_in_hand = new ArrayList<>();
      for (AbstractCard c : AbstractDungeon.player.hand.group) {
        if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
          curse_and_status_in_hand.add(c);
        }
      }
      if (!curse_and_status_in_hand.isEmpty()) {
        Collections.shuffle(curse_and_status_in_hand);
        AbstractCard randomCurseOrStatus = curse_and_status_in_hand.get(0);
        addToTop(new ExhaustSpecificCardAction(randomCurseOrStatus, AbstractDungeon.player.hand));
      }
      this.isDone = true;
    }
    tickDuration();
  }
}