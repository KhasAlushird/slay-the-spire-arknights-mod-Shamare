package shamaremod.actions;

import java.util.UUID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
//abondoned！！
public class ModifyCostAction extends AbstractGameAction {
  private UUID uuid;
  
  public ModifyCostAction(UUID targetUUID, int amount) {
    setValues(this.target, this.source, amount);
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.uuid = targetUUID;
  }
  
  public void update() {
    for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
      c.modifyCostForCombat(this.amount);
    } 
    this.isDone = true;
  }
}