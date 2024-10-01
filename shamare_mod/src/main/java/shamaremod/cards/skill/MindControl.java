package shamaremod.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
public class MindControl extends CustomCard {

    //abondoned!!
    public static final String ID = IdHelper.makePath("MindControl");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = null;//ImageHelper.getCardImgPath(CardType.SKILL, "MindControl");
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public MindControl() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=2;
        this.exhaust=true;
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isEthereal = false;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useFastAttackAnimation();
        m.rollMove();
        //intent_setter(m);

    }

    private void intent_setter(AbstractMonster m){
            byte curr_next_move=m.nextMove;
            AbstractMonster.Intent targetIntent = null;
            switch (m.intent) {
                case ATTACK:
                    targetIntent=AbstractMonster.Intent.DEFEND;
                    break;

                case ATTACK_BUFF:
                targetIntent=AbstractMonster.Intent.DEFEND_BUFF;
                break;

                case ATTACK_DEBUFF:
                targetIntent=AbstractMonster.Intent.DEFEND_DEBUFF;
                break;

                case DEFEND:
                targetIntent=AbstractMonster.Intent.ATTACK;
                break;

                case DEFEND_BUFF:
                targetIntent=AbstractMonster.Intent.ATTACK_BUFF;
                break;

                case DEFEND_DEBUFF:
                targetIntent=AbstractMonster.Intent.ATTACK_DEBUFF;
                break;

                default:
                    break;
            }
            if(targetIntent!=null){
                m.setMove(curr_next_move, targetIntent);
            }

            
    }

     public AbstractCard makeCopy() {
      return new MindControl();
   }

    
}
