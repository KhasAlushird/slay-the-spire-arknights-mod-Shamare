package shamaremod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import shamaremod.powers.SorceryVulnerable;


public class DepartedEchoAction extends AbstractGameAction {
    private AbstractPlayer p;
    private AbstractMonster m;
    private int damage;
    private int magicNumber;
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean if_upgraded;

    public DepartedEchoAction(AbstractPlayer p, AbstractMonster m, int damage, int magicNumber, boolean freeToPlayOnce, int energyOnUse, boolean if_upgraded) {
        this.p = p;
        this.m = m;
        this.damage = damage;
        this.magicNumber = magicNumber;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.energyOnUse = energyOnUse;
        this.if_upgraded = if_upgraded;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }
        if (this.if_upgraded) {
            effect += 1;
        }

        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
            }
            for (int i = 0; i < effect; i++) {
                addToBot(new ApplyPowerAction(m, p, new SorceryVulnerable(m, this.magicNumber), this.magicNumber));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}