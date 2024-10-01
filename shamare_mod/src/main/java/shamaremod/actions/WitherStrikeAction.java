package shamaremod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import shamaremod.powers.Namesis;

public class WitherStrikeAction extends AbstractGameAction {
  private int namesisGainAmt;
  
  private DamageInfo info;
  
  public WitherStrikeAction(AbstractCreature target, DamageInfo info, int namesisAmt) {
    this.info = info;
    setValues(target, info);
    this.namesisGainAmt = namesisAmt;
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.duration = Settings.ACTION_DUR_FASTER;
  }
  
  public void update() {
    AbstractCreature p = AbstractDungeon.player;
    if (this.duration == Settings.ACTION_DUR_FASTER && 
      this.target != null) {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SLASH_HEAVY));
      this.target.damage(this.info);
      if (!((this.target).isDying || this.target.currentHealth <= 0)){
        addToBot(new ApplyPowerAction(p, p, new Namesis(p, this.namesisGainAmt), this.namesisGainAmt, true, AbstractGameAction.AttackEffect.NONE));
      }
        
      if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
        AbstractDungeon.actionManager.clearPostCombatActions(); 
    } 
    tickDuration();
  }
}
