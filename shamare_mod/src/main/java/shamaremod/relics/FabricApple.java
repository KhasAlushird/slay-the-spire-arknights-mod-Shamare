package shamaremod.relics;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import shamaremod.cards.skill.SuspiciousApple;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

// 继承CustomRelic
public class FabricApple extends CustomRelic {
    // 遗物ID
    public static final String ID = IdHelper.makePath("FabricApple");
    // 图片路径
    private static final String IMG_PATH =ImageHelper.getOtherImgPath("relics", "FabricApple");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public FabricApple() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        AbstractCreature p = AbstractDungeon.player;
        super.atBattleStart();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));


        //draw card 
        this.addToBot(new DrawCardAction(p,1));
        //add card to hand
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new SuspiciousApple(), 1));




    }

    public AbstractRelic makeCopy() {
        return new FabricApple();
    }
}
