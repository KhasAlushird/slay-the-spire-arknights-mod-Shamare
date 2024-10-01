package shamaremod.potions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.ReflectionHacks;
import shamaremod.helpers.IdHelper;
import shamaremod.powers.Namesis;

public class ViciousPotion extends AbstractPotion {
  public static final String POTION_ID = IdHelper.makePath("ViciousPotion");
  
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString( IdHelper.makePath("ViciousPotion"));
  
  public ViciousPotion() {
    super(potionStrings.NAME,  IdHelper.makePath("ViciousPotion") , AbstractPotion.PotionRarity.COMMON, AbstractPotion.PotionSize.SPHERE, AbstractPotion.PotionColor.NONE);
    ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", new Texture("shamaremod/images/potions/ViciousPotion.png"));
    this.labOutlineColor = Color.PURPLE;
    this.isThrown = true;    
    this.targetRequired = true;
  }
  
  public void initializeData() {
    this.potency = getPotency();
    this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
  }
  
  public void use(AbstractCreature target) {
     this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Namesis(AbstractDungeon.player, 4), 4));
     this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new Namesis(target, potency), potency));
  }
  
  public int getPotency(int ascensionLevel) {
    return 25;
  }
  
  public AbstractPotion makeCopy() {
    return new ViciousPotion();
  }
}
