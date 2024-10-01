package shamaremod.cards.status;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;

import basemod.abstracts.CustomCard;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class HolyDisaster extends CustomCard {

    public static final String ID = IdHelper.makePath("HolyDisaster");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.STATUS, "HolyDisaster");
    private static final int COST = -2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = AbstractCard.CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private boolean has_triggered_this_turn = false;

    // 静态变量来跟踪当前的 magicNumber 加成
    private static int currentMagicNumberBonus = 0;

    public HolyDisaster() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 1 + currentMagicNumberBonus;
        this.magicNumber = this.baseMagicNumber;
        this.isEthereal = true;
    }

    public static int get_currentMagicNumberBonus(){
        return currentMagicNumberBonus;
    }

    public void set_magicnumber_by_hand(int num){
        this.baseMagicNumber=num;
        this.magicNumber=num;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void triggerWhenDrawn() {
        AbstractPlayer player = AbstractDungeon.player;
        if(!this.has_triggered_this_turn){
            // 抽取 magicNumber 张牌
            addToBot(new DrawCardAction(player, this.magicNumber));

            // 获得 magicNumber 层再生
            addToBot(new ApplyPowerAction(player, player, new RegenPower(player, this.magicNumber), this.magicNumber));

            // 增加 magicNumber
            this.incrementMagicNumberForAllHolyDisaster();

            // 将所有种类的 Disaster 牌置入弃牌堆中
            addToBot(new MakeTempCardInDiscardAction(new FireDisaster(), 1));
            addToBot(new MakeTempCardInDiscardAction(new PoisonDisaster(), 1));
            addToBot(new MakeTempCardInDiscardAction(new ShadowDisaster(), 1));
            addToBot(new MakeTempCardInDiscardAction(new HolyDisaster(), 1));

            this.has_triggered_this_turn=true;
        }

        
    }

    
    @Override
    public void triggerOnManualDiscard() {
        this.has_triggered_this_turn=false;
  }
    @Override
    public void onMoveToDiscard() {
        this.has_triggered_this_turn=false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new HolyDisaster();
    }

    private void incrementMagicNumberForAllHolyDisaster() {
        currentMagicNumberBonus += 1;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof HolyDisaster) {
                card.baseMagicNumber += 1;
                card.magicNumber = card.baseMagicNumber;
                card.applyPowers();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof HolyDisaster) {
                card.baseMagicNumber += 1;
                card.magicNumber = card.baseMagicNumber;
                card.applyPowers();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof HolyDisaster) {
                card.baseMagicNumber += 1;
                card.magicNumber = card.baseMagicNumber;
                card.applyPowers();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof HolyDisaster) {
                card.baseMagicNumber += 1;
                card.magicNumber = card.baseMagicNumber;
                card.applyPowers();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof HolyDisaster) {
                card.baseMagicNumber += 1;
                card.magicNumber = card.baseMagicNumber;
                card.applyPowers();
            }
        }
    }

    // 新增方法：重置 currentMagicNumberBonus
    public static void resetMagicNumberBonus() {
        currentMagicNumberBonus = 0;
    }
}