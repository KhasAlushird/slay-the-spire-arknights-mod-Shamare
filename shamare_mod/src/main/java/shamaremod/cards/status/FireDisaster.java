package shamaremod.cards.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class FireDisaster extends CustomCard {

    public static final String ID = IdHelper.makePath("FireDisaster");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.STATUS, "FireDisaster");
    private static final int COST = -2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR =  AbstractCard.CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private boolean has_triggered_this_turn=false;

    // 静态变量来跟踪当前的 magicNumber 加成
    private static int currentMagicNumberBonus = 0;

    public FireDisaster() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 12 + currentMagicNumberBonus;
        this.magicNumber = this.baseMagicNumber;
        this.isEthereal = true;
    }

    public static  int get_currentMagicNumberBonus(){
        return currentMagicNumberBonus;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void set_magicnumber_by_hand(int num){
        this.baseMagicNumber=num;
        this.magicNumber=num;
    }

    public void triggerWhenDrawn() {
        if(!this.has_triggered_this_turn){
            // 遍历房间中的所有怪物，依次对它们造成伤害
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDeadOrEscaped()) {
                    addToBot(new DamageAction(monster, new DamageInfo(null, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
                }
            }
            // 增加 magicNumber
            this.incrementMagicNumberForAllFireDisaster();
            this.has_triggered_this_turn=true;
        }

    }

    @Override
    public AbstractCard makeCopy() {
        return new FireDisaster();
    }

    @Override
    public void triggerOnManualDiscard() {
        this.has_triggered_this_turn=false;
  }
    @Override
    public void onMoveToDiscard() {
        this.has_triggered_this_turn=false;
    }

    private void incrementMagicNumberForAllFireDisaster() {
        currentMagicNumberBonus += 6;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof FireDisaster) {
                card.baseMagicNumber += 6;
                card.magicNumber = card.baseMagicNumber;
                
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof FireDisaster) {
                card.baseMagicNumber += 6;
                card.magicNumber = card.baseMagicNumber;
                
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof FireDisaster) {
                card.baseMagicNumber += 6;
                card.magicNumber = card.baseMagicNumber;
                
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof FireDisaster) {
                card.baseMagicNumber += 6;
                card.magicNumber = card.baseMagicNumber;
                
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof FireDisaster) {
                card.baseMagicNumber += 6;
                card.magicNumber = card.baseMagicNumber;
                
            }
        }
    }

    // 新增方法：重置 currentMagicNumberBonus
    public static void resetMagicNumberBonus() {
        currentMagicNumberBonus = 0;
    }
}