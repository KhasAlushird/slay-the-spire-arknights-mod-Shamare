package shamaremod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class WitchBrewAction extends AbstractGameAction  {
  
  private DamageInfo info;
  private boolean should_obtain_poison = false;
  
  public WitchBrewAction(AbstractCreature target, DamageInfo info) {
    this.info = info;
    setValues(target, info);
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.duration = Settings.ACTION_DUR_FASTER;
    //basemod.BaseMod.subscribe(this);
  }
  
  public void update() {
    if (this.duration == Settings.ACTION_DUR_FASTER && this.target != null) {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SLASH_HEAVY));
      this.target.damage(this.info);
      
      if (((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) {
        if (!this.target.halfDead && !this.target.hasPower("Minion")) {
          AbstractDungeon.getCurrRoom().rewards.add(new com.megacrit.cardcrawl.rewards.RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.UNCOMMON, true)));
           AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(7));
         
         
          //this.should_obtain_poison = true;
        }
      }
      
      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
    }
    
    tickDuration();
  }

  // @Override
  // public void receivePostBattle(AbstractRoom room) {
  //   if (this.should_obtain_poison) {
  //     // 在战斗结束时触发效果
  //     //addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.UNCOMMON, true)));
  //     AbstractDungeon.getCurrRoom().rewards.add(new com.megacrit.cardcrawl.rewards.RewardItem(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.UNCOMMON, true)));
  //     this.should_obtain_poison = false;
  //   }
  //   basemod.BaseMod.unsubscribe(this);
  // }
}