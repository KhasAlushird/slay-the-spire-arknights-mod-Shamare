package shamaremod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import shamaremod.helpers.IdHelper;
import shamaremod.powers.Namesis;

public class LivingEssence extends AbstractPotion {
  public static final String POTION_ID = IdHelper.makePath("LivingEssence");
  
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString( IdHelper.makePath("LivingEssence"));
  
  public LivingEssence() {
    super(potionStrings.NAME,  IdHelper.makePath("LivingEssence") , AbstractPotion.PotionRarity.RARE, AbstractPotion.PotionSize.EYE, AbstractPotion.PotionColor.FRUIT);
    //ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", new Texture("shamaremod/images/potions/LivingEssence.png"));
    this.labOutlineColor = Color.PURPLE;
    this.isThrown = true;    
  }
  
  public void initializeData() {
    this.potency = getPotency();
    this.description = potionStrings.DESCRIPTIONS[0];
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
  }
  
  public void use(AbstractCreature target) {
    int hp_to_add=0;
    if(AbstractDungeon.player != null){
        if(AbstractDungeon.player.hasPower(Namesis.POWER_ID)) {
            hp_to_add = AbstractDungeon.player.getPower(Namesis.POWER_ID).amount;
          }
        if(AbstractDungeon.player.hasRelic("SacredBark")){
            hp_to_add*=2;
        }
    }
    this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, hp_to_add));
  }
  
  public int getPotency(int ascensionLevel) {
    int the_potency = 0;
    return the_potency;
  }
  
  public AbstractPotion makeCopy() {
    return new LivingEssence();
  }
}
