package shamaremod.cards.attack;

import java.util.Random;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

public class SerialDisaster extends CustomCard {

    public static final String ID = IdHelper.makePath("SerialDisaster");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "SerialDisaster");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public SerialDisaster() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 5;
        this.isMultiDamage = true;
        this.exhaust = true;
        
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
        int disaster_to_add = 0;
        for (int i = 0; i < 2; i++) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDeadOrEscaped()) {
                    this.addToBot(new DamageAction(monster, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    if (monster.currentHealth > 0 && monster.currentBlock < this.damage) {
                        disaster_to_add++;
                    }
                }
            }
        }

        for(int i = 0;i<disaster_to_add;i++){
            random_disaster_getter();
        }

        
    }

    private void random_disaster_getter() {
        Random rand = new Random();
        int debuff = rand.nextInt(3);
        switch (debuff) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new PoisonDisaster(), 1));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new FireDisaster(), 1));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new ShadowDisaster(), 1));
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SerialDisaster();
    }
}