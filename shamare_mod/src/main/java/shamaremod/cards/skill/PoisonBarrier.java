package shamaremod.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.PoisonBarrierPower;

public class PoisonBarrier extends CustomCard {

    public static final String ID = IdHelper.makePath("PoisonBarrier");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "PoisonBarrier");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public PoisonBarrier() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock=20;
        this.magicNumber=this.baseMagicNumber=4;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(5);
            this.upgradeMagicNumber(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();
        this.addToBot(new GainBlockAction(p, p, this.block));  
        this.addToBot(new ApplyPowerAction(p, p, new PoisonBarrierPower(p, this.magicNumber), this.magicNumber)); 

    }


     public AbstractCard makeCopy() {
      return new PoisonBarrier();
   }

    
}
