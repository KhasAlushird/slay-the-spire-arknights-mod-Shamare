package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class NamesisToEnemy extends AbstractPower {

    public static final String POWER_ID = IdHelper.makePath("NamesisToEnemy");

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean has_triggered_this_turn1 = false;

    public NamesisToEnemy(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = AbstractPower.PowerType.DEBUFF;
        this.isTurnBased = true;

        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "Namesis_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "Namesis_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }

    @Override
    public void atStartOfTurn() {
        this.has_triggered_this_turn1 = false;
    }

    /*
        当通过“堕落之血”或“手术”赋予/增加报应层数时，会调用 settings_when_applyed() 方法。
        这个方法会将 has_triggered_this_turn1 设为 true。
        由于这个开关被手动打开了，本回合内怪物的下一次受到伤害将无法触发报应扣血逻辑。
     */
    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != this.owner && damageAmount > 0 && info.type != DamageType.THORNS && info.type != DamageType.HP_LOSS) {
            if (!this.has_triggered_this_turn1) {
                addToBot(new LoseHPAction(this.owner, this.owner, this.amount));
                this.has_triggered_this_turn1 = true;
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }
    }

    public void trigger_by_hand() {
        addToBot(new LoseHPAction(this.owner, this.owner, this.amount));
        this.has_triggered_this_turn1 = true;
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
    }
}
