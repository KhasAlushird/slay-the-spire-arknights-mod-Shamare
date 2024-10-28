package shamaremod.cards.attack;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class SuperNatural extends CustomCard {

    public static final String ID = IdHelper.makePath("SuperNatural");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.ATTACK, "SuperNatural");
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public SuperNatural() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 10;
        this.magicNumber=this.baseMagicNumber=2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(true);
            this.addToBot(new DamageAction(randomMonster, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }

        boolean hasDemandedCard = false;
        Iterator<AbstractCard> var4 = p.hand.group.iterator();

        while (var4.hasNext()) {
            AbstractCard c = var4.next();
            if (c.type == CardType.CURSE||c.type==CardType.STATUS) {
                hasDemandedCard = true;
                break;
            }
        }
        if(hasDemandedCard){
            if(p.hasPower("ShamareKhas:Namesis")){
                int reduce_num=p.getPower("ShamareKhas:Namesis").amount/2;
                if(reduce_num>=1){
                    this.addToBot(new com.megacrit.cardcrawl.actions.common.ReducePowerAction(p, p,"ShamareKhas:Namesis", reduce_num));
                }
            }
        }
    }

     public AbstractCard makeCopy() {
      return new SuperNatural();
   }

   @Override
    public void triggerOnGlowCheck() {
        AbstractPlayer p = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
       boolean glow = false;
       boolean hasDemandedCard=false;
       boolean hasNamesis=false;

       Iterator<AbstractCard> var4 = p.hand.group.iterator();
       while (var4.hasNext()) {
           AbstractCard c = var4.next();
           if (c.type == CardType.CURSE||c.type==CardType.STATUS) {
               hasDemandedCard = true;
               break;
           }
       }
       if(p.hasPower("ShamareKhas:Namesis")){
            if(p.getPower("ShamareKhas:Namesis").amount>1){
                hasNamesis=true;
            }
       }
       if(hasNamesis&&hasDemandedCard){
        glow = true;
       }
       else{
        glow  = false;
       }
        if (glow) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    
}
