package shamaremod.cards.skill;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.actions.ExhaustOneCurseOrStatusAction;
import shamaremod.cards.curse.BlackBile;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class OmenOfDefeat extends CustomCard {

    public static final String ID = IdHelper.makePath("OmenOfDefeat");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "OmenOfDefeat");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public OmenOfDefeat() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=3;
        this.cardsToPreview = new BlackBile();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();

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
            this.addToBot(new ExhaustOneCurseOrStatusAction());
            addToBot(new DrawCardAction(p,this.magicNumber));
        }

        else{
            this.addToBot(new MakeTempCardInHandAction(new BlackBile(), 1));
        }
        
    }

     public AbstractCard makeCopy() {
      return new OmenOfDefeat();
   }


   @Override
   public void triggerOnGlowCheck() {
       AbstractPlayer p = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
      boolean glow = false;
      boolean hasDemandedCard=false;

      Iterator<AbstractCard> var4 = p.hand.group.iterator();
      while (var4.hasNext()) {
          AbstractCard c = var4.next();
          if (c.type == CardType.CURSE||c.type==CardType.STATUS) {
              hasDemandedCard = true;
              break;
          }
      }
      if(hasDemandedCard){
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
