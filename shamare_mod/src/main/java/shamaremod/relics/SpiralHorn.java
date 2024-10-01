package shamaremod.relics;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.Namesis;

// 继承CustomRelic
public class SpiralHorn extends CustomRelic {
    // 遗物ID
    public static final String ID = IdHelper.makePath("SpiralHorn");
    // 图片路径
    private static final String IMG_PATH =ImageHelper.getOtherImgPath("relics", "SpiralHorn");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public SpiralHorn() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        flash();
        addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
        //add Namesis
         addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Namesis(AbstractDungeon.player, 6),6, true, AbstractGameAction.AttackEffect.NONE));
  }
  
  public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
  }
  
  public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
  }

    public AbstractRelic makeCopy() {
        return new SpiralHorn();
    }
}
