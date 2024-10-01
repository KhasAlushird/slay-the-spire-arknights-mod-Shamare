package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class SorceryWeak extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("SorceryWeak");
    
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private boolean attackedThisTurn = false;
    
    private static final float DAMAGE_MULTIPLIER = 0.5F;
    
    public SorceryWeak(AbstractMonster owner, int amount) {
        //注意，该buff只能用在monster身上！！
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.DEBUFF;
        this.isTurnBased = true;

           // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "SorceryWeak_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "SorceryWeak_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }
    
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] ;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }
    
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * DAMAGE_MULTIPLIER;
        }
        return damage;
    }
    
    @Override
    public void atEndOfRound() {
        if (this.attackedThisTurn) {
            if (this.amount == 1) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            } else {
                addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            }
        }
        this.attackedThisTurn = false;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == this.owner && damageAmount > 0) {
            this.attackedThisTurn = true;
        }
    }

     @Override
    public void atStartOfTurn() {
        if (this.owner instanceof AbstractMonster) {
            AbstractMonster monster = (AbstractMonster) this.owner;
            if (monster.intent == AbstractMonster.Intent.ATTACK || 
                monster.intent == AbstractMonster.Intent.ATTACK_BUFF || 
                monster.intent == AbstractMonster.Intent.ATTACK_DEBUFF || 
                monster.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
                this.attackedThisTurn = true;
            }
        }
    }
}