package shamaremod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.NamesisToEnemy;

public class EvilBoding extends CustomCard {

    public static final String ID = IdHelper.makePath("EvilBoding");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "EvilBoding");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public EvilBoding() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust=true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();
        int namesis_to_add = 0;
        if(p.hasPower("ShamareKhas:Namesis")){
            namesis_to_add = p.getPower("ShamareKhas:Namesis").amount;

              //add Namesis
            addToBot(new ApplyPowerAction(m, m, new NamesisToEnemy(m, namesis_to_add)));
        }
    }


    @Override
    public void triggerOnGlowCheck() {
        AbstractPlayer p = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
        if (p.hasPower("ShamareKhas:Namesis")) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EvilBoding();
    }


}
