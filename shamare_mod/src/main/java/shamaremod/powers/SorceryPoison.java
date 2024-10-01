package shamaremod.powers;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;





public class SorceryPoison extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = IdHelper.makePath("SorceryPoison");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SorceryPoison(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;

        // 如果需要不能叠加的能力，只需将上面的amount参数删掉，并把下面的amount改成-1就行
        this.amount = amount;
        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "SorceryPoison_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "SorceryPoison_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount * 2); // 这样，%d就被替换成能力的层数
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
            flash();
            // 对 Buff 拥有者造成相当于 Buff 层数 2 倍的伤害
            DamageInfo poisonDamage = new DamageInfo(this.owner, this.amount * 2, DamageInfo.DamageType.THORNS);
            poisonDamage.applyPowers(this.owner, this.owner);
            addToBot(new DamageAction(this.owner, poisonDamage, AbstractGameAction.AttackEffect.POISON));
            // 减少一层 Buff
            if(this.amount==1){
                 addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
            else{
                addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            }
            
        }
}

}



