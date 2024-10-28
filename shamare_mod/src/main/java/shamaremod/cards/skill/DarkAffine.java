package shamaremod.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.Namesis;

public class DarkAffine extends CustomCard {

    public static final String ID = IdHelper.makePath("DarkAffine");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "DarkAffine");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public DarkAffine() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = 16;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
        unregisterFromNamesis();
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        adjustCostBasedOnNamesis();
        registerToNamesis();
    }

    @Override
    public void atTurnStart() {
        resetAttributes();
        adjustCostBasedOnNamesis();
        applyPowers();
    }

    public void adjustCostBasedOnNamesis() {
        if (AbstractDungeon.player != null) {
            AbstractPower namesisPower = AbstractDungeon.player.getPower("ShamareKhas:Namesis");
            if (namesisPower != null) {
                int namesisAmount = namesisPower.amount;
                setCostForTurn(Math.max(0, this.cost - namesisAmount / 4));
            }
        }
    }

    public void adjustCostBasedOnNamesis_by_amount(int namesisAmount) {
        setCostForTurn(Math.max(0, this.cost - namesisAmount / 4));
    }

    public void adjustCostBasedOnNamesis_whenRemoved() {
        setCostForTurn(Math.max(0, this.cost));
    }

    @Override
    public AbstractCard makeCopy() {
        DarkAffine copy = new DarkAffine();
        copy.adjustCostBasedOnNamesis();
        copy.registerToNamesis();
        return copy;
    }

    private void registerToNamesis() {
        if (AbstractDungeon.player != null) {
            AbstractPower namesisPower = AbstractDungeon.player.getPower("ShamareKhas:Namesis");
            if (namesisPower instanceof Namesis) {
                ((Namesis) namesisPower).addDynamicCostCard(this);
            }
        }
    }

    private void unregisterFromNamesis() {
        if (AbstractDungeon.player != null) {
            AbstractPower namesisPower = AbstractDungeon.player.getPower("ShamareKhas:Namesis");
            if (namesisPower instanceof Namesis) {
                ((Namesis) namesisPower).removeDynamicCostCard(this);
            }
        }
    }
}