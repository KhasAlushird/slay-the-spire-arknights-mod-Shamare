package shamaremod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

//ABONDONED!

public class AOEwithoutVulnerableAction extends AbstractGameAction {
    public int[] damage;
    private int baseDamage;
    private boolean firstFrame = true, utilizeBaseDamage = false;

    public AOEwithoutVulnerableAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
        this.source = source;
        this.damage = amount;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FAST;
        }
    }

    public AOEwithoutVulnerableAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        this(source, amount, type, effect, false);
    }

    public AOEwithoutVulnerableAction(AbstractCreature source, int baseDamage, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        this(source, (int[]) null, type, effect, false);
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
    }

    public void update() {
        if (this.firstFrame) {
            boolean playedMusic = false;
            int temp = (AbstractDungeon.getCurrRoom()).monsters.monsters.size();
            if (this.utilizeBaseDamage)
                this.damage = DamageInfo.createDamageMatrix(this.baseDamage);
            for (int i = 0; i < temp; i++) {
                if (!((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).isDying &&
                        ((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).currentHealth > 0 &&
                        !((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).isEscaping)
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                                ((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cX,
                                ((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                                ((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cX,
                                ((AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cY, this.attackEffect));
                    }
            }
            this.firstFrame = false;
        }
        tickDuration();
        if (this.isDone) {
            int temp = (AbstractDungeon.getCurrRoom()).monsters.monsters.size();
            for (int i = 0; i < temp; i++) {
                AbstractMonster monster = (AbstractMonster) (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i);
                if (!monster.isDeadOrEscaped()) {
                    if (this.attackEffect == AbstractGameAction.AttackEffect.POISON) {
                        monster.tint.color.set(Color.CHARTREUSE);
                        monster.tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AbstractGameAction.AttackEffect.FIRE) {
                        monster.tint.color.set(Color.RED);
                        monster.tint.changeColor(Color.WHITE.cpy());
                    }
                    // 直接造成伤害，不受 Vulnerable 或 SorceryVulnerable 的影响
                    int damageAmount = this.damage[i];
                    DamageInfo damageInfo = new DamageInfo(this.source, damageAmount, this.damageType);
                    if (shouldCancelAction() && damageInfo.type != DamageInfo.DamageType.THORNS) {
                        this.isDone = true;
                        return;
                    }
                    if (damageInfo.type != DamageInfo.DamageType.THORNS && (
                            damageInfo.owner.isDying || damageInfo.owner.halfDead)) {
                        this.isDone = true;
                        return;
                    }
                    monster.damage(damageInfo);
                    // 显示伤害数字特效
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY, this.attackEffect));
                }
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
            if (!Settings.FAST_MODE)
                addToTop((AbstractGameAction) new WaitAction(0.1F));
        }
    }
}