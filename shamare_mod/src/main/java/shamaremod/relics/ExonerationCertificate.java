package shamaremod.relics;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

// 继承CustomRelic
public class ExonerationCertificate extends CustomRelic {
    // 遗物ID
    public static final String ID = IdHelper.makePath("ExonerationCertificate");
    // 图片路径
    private static final String IMG_PATH =ImageHelper.getOtherImgPath("relics", "ExonerationCertificate");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public ExonerationCertificate() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        boolean is_activate=false;
        is_activate=(drawnCard.cardID.equals("ShamareKhas:FireDisaster")
                    ||drawnCard.cardID.equals("ShamareKhas:HolyDisaster")
                    ||drawnCard.cardID.equals("ShamareKhas:PoisonDisaster")
                    ||drawnCard.cardID.equals("ShamareKhas:ShadowDisaster"));
        if (is_activate) {
            flash();
            AbstractCreature p = AbstractDungeon.player;
            if(p.hasPower("ShamareKhas:Namesis")){
                addToBot(new RelicAboveCreatureAction(p, this));
                if(p.getPower("ShamareKhas:Namesis").amount<=3){
                    addToBot(new RemoveSpecificPowerAction(p, p, "ShamareKhas:Namesis"));
                }
                else{
                    addToBot(new ReducePowerAction(p, p, "ShamareKhas:Namesis", 3));
                }
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new ExonerationCertificate();
    }
}
