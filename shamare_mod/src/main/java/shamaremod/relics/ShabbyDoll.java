package shamaremod.relics;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

// 继承CustomRelic
public class ShabbyDoll extends CustomRelic {
    // 遗物ID
    public static final String ID = IdHelper.makePath("ShabbyDoll");
    // 图片路径
    private static final String IMG_PATH =ImageHelper.getOtherImgPath("relics", "ShabbyDoll");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public ShabbyDoll() {
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

        //add debuff to a random monster
        AbstractCreature m = AbstractDungeon.getMonsters().getRandomMonster();
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1,false), 1));
        m= AbstractDungeon.getMonsters().getRandomMonster();
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m,1,false), 1));

    }

    public AbstractRelic makeCopy() {
        return new ShabbyDoll();
    }
}
