package shamaremod.relics;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import basemod.abstracts.CustomRelic;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

// 继承CustomRelic
public class GreedCoin extends CustomRelic {
    // 遗物ID
    public static final String ID = IdHelper.makePath("GreedCoin");
    // 图片路径
    private static final String IMG_PATH =ImageHelper.getOtherImgPath("relics", "GreedCoin");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public GreedCoin() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onExhaust(AbstractCard card) {
        flash();
        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(1));
  }
  

    public AbstractRelic makeCopy() {
        return new GreedCoin();
    }
}