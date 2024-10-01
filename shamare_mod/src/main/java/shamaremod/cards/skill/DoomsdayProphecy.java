package shamaremod.cards.skill;

import java.util.Random;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.cards.status.FireDisaster;
import shamaremod.cards.status.PoisonDisaster;
import shamaremod.cards.status.ShadowDisaster;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class DoomsdayProphecy extends CustomCard {

    public static final String ID = IdHelper.makePath("DoomsdayProphecy");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "DoomsdayProphecy");
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public DoomsdayProphecy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
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
        // 获得 1 点能量
        this.addToBot(new GainEnergyAction(1));
        if(this.upgraded){
            //draw a card
            addToBot(new DrawCardAction(p, 1));
        }

        // 如果玩家当前生命值低于最大生命值的 50%，则随机将 3 张灾祸置入抽牌堆
        if (p.currentHealth <= p.maxHealth * 0.5) {
            for (int i = 0; i < 3; i++) {
                this.addToBot(new MakeTempCardInDrawPileAction(randomDisaster(), 1, true, true));
            }
        }
    }

    private AbstractCard randomDisaster() {
        Random rand = new Random();
        int debuff = rand.nextInt(3);
        switch (debuff) {
            case 0:
                return new PoisonDisaster();
            case 1:
                return new FireDisaster();
            case 2:
                return new ShadowDisaster();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.currentHealth <= AbstractDungeon.player.maxHealth * 0.5) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoomsdayProphecy();
    }
}