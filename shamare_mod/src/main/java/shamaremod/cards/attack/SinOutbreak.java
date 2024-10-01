package shamaremod.cards.attack;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.Namesis;

public class SinOutbreak extends CustomCard {

    public static final String ID = IdHelper.makePath("SinOutbreak");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "SinOutbreak");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SinOutbreak() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = 5;
        this.baseDamage = 20;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();

        // 检查目标是否有DEBUFF
        int curr_debuff_num = 0;
        int monster_debuff_end_index = 0;
        int player_debuff_end_index = 0;
        // add monster DEBUFF
        ArrayList<String> debuff_to_move = new ArrayList<>();
        for (AbstractPower power : m.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuff_to_move.add(power.ID);
                curr_debuff_num++;
            }
        }
        monster_debuff_end_index = curr_debuff_num - 1;

        // remove debuff of player
        if (this.upgraded) {
            // add player DEBUFF
            for (AbstractPower power : p.powers) {
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    debuff_to_move.add(power.ID);
                    curr_debuff_num++;
                }
            }
            player_debuff_end_index = curr_debuff_num - 1;
        }

        for (int i = 0; i < curr_debuff_num; i++) {
            this.addToBot(
                new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn)
                )
            );
        }

        // remove monster debuff
        for (int i = 0; i <= monster_debuff_end_index; i++) {
            if (i < debuff_to_move.size()) {
                addToBot(new RemoveSpecificPowerAction(m, m, debuff_to_move.get(i)));
            }
        }

        // remove player debuff
        for (int i = monster_debuff_end_index + 1; i <= player_debuff_end_index; i++) {
            if (i < debuff_to_move.size()) {
                addToBot(new RemoveSpecificPowerAction(p, p, debuff_to_move.get(i)));
            }
        }

        // add Namesis
        addToBot(new ApplyPowerAction(p, p, new Namesis(p, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void triggerOnGlowCheck() {
        boolean glow = false;
        AbstractPlayer p = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
        boolean player_has_debuff = false;
        for (AbstractPower power : p.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                player_has_debuff = true;
                break;
            }
        }
        if (this.upgraded && player_has_debuff) {
            glow = true;
        } else {
            glow = false;
        }
        if (glow) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public AbstractCard makeCopy() {
        return new SinOutbreak();
    }
}