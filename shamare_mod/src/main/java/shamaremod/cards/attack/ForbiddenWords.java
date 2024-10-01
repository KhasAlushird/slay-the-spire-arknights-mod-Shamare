package shamaremod.cards.attack;

import java.util.Random;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.SorceryPoison;
import shamaremod.powers.SorceryVulnerable;
import shamaremod.powers.SorceryWeak;

public class ForbiddenWords extends CustomCard {

    public static final String ID = IdHelper.makePath("ForbiddenWords");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "ForbiddenWords");
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ForbiddenWords() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        
        // 随机选择一个 debuff
        Random rand = new Random();
        int debuff = rand.nextInt(6);
        AbstractPower power;
        switch (debuff) {
            case 0:
                power = new WeakPower(m, 1, false);
                break;
            case 1:
                power = new VulnerablePower(m, 1, false);
                break;
            case 2:
                power = new PoisonPower(m, p, 3);
                break;
            case 3:
                power = new SorceryWeak(m, 1);
                break;
            case 4:
                power = new SorceryVulnerable(m, 1);
                break;
            case 5:
                power = new SorceryPoison(m, 2);
                break;
            default:
                power = new WeakPower(m, 1, false);
                break;
        }
        this.addToBot(new ApplyPowerAction(m, p, power, power.amount));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ForbiddenWords();
    }
}