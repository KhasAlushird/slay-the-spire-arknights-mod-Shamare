package shamaremod.cards.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

import basemod.abstracts.CustomCard;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.Namesis;
import shamaremod.powers.NamesisToEnemy;

public class FallenBlood extends CustomCard {

    public static final String ID = IdHelper.makePath("FallenBlood");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.CURSE, "FallenBlood");
    private static final int COST = -2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.CURSE;
    private static final CardColor COLOR = AbstractCard.CardColor.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public FallenBlood() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerWhenDrawn() {

        //VFXAction
        if (Settings.FAST_MODE) {
            addToBot((AbstractGameAction) new VFXAction((AbstractGameEffect) new OfferingEffect(), 0.1F));
        } else {
            addToBot((AbstractGameAction) new VFXAction((AbstractGameEffect) new OfferingEffect(), 0.5F));
        }
        AbstractPlayer p = AbstractDungeon.player;
        // 抽一张牌
        addToBot(new DrawCardAction(p, 1));
        // 对自己施加 4 层 Namesis
        addToBot(new ApplyPowerAction(p, p, new Namesis(p, 3), 3));
        // 对一个随机敌人施加 12 点namesis
        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (randomMonster != null) {
            addToBot(new ApplyPowerAction(randomMonster, p, new NamesisToEnemy(randomMonster, 12), 12));
        }
        // 将 2 张 FallenBlood 置入弃牌堆
        addToBot(new MakeTempCardInDiscardAction(new FallenBlood(), 2));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FallenBlood();
    }

}
