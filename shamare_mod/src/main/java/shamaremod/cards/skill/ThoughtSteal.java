package shamaremod.cards.skill;

import java.util.ArrayList;
import java.util.Random;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.CustomCard;
import shamaremod.cards.power.Aruspicy;
import shamaremod.cards.power.CalamityGrasp;
import shamaremod.cards.power.Deal;
import shamaremod.cards.power.Defile;
import shamaremod.cards.power.DimBud;
import shamaremod.cards.power.EdgeRunner;
import shamaremod.cards.power.HirudoTherapy;
import shamaremod.cards.power.PeaceAltar;
import shamaremod.cards.power.PlagueScholar;
import shamaremod.cards.power.Salvation;
import shamaremod.cards.power.TheFool;
import shamaremod.character.Shamare;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.Namesis;
import shamaremod.powers.SorceryVulnerable;
import shamaremod.powers.SorceryWeak;

public class ThoughtSteal extends CustomCard {

    public static final String ID = IdHelper.makePath("ThoughtSteal");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = ImageHelper.getCardImgPath(CardType.SKILL, "ThoughtSteal");
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Shamare.PlayerColorEnum.SHAMARE_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ThoughtSteal() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = 16;
        this.baseDamage = 17;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (m.intent) {
            case ATTACK:{
                this.addToBot(new GainBlockAction(p, p, this.block));
                this.addToBot(new GainBlockAction(p, p, this.block));
                break;
            }
            case ATTACK_BUFF:{
                this.addToBot(new GainBlockAction(p, p, this.block));
                this.addToBot(new ApplyPowerAction(m, p, new SorceryWeak(m, this.magicNumber), this.magicNumber));
            }
            case ATTACK_DEBUFF:{
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
                this.addToBot(new GainBlockAction(p, p, this.block));
                this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
                break;
            }
            case ATTACK_DEFEND:{
                this.addToBot(new GainBlockAction(p, p, this.block));
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                break;
            }
                
            case DEFEND:{
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                break;
            }
            case DEFEND_BUFF:{
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                this.addToBot(new ApplyPowerAction(m, p, new SorceryVulnerable(m, this.magicNumber), this.magicNumber));
                break;
            }
            case DEFEND_DEBUFF:
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
                break;
            case DEBUFF:{
                this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, this.magicNumber), this.magicNumber));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
                break;
            }
            case STRONG_DEBUFF:{
                this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 3), 3));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 2), 2));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 2), 2));
                break;
            }
            case BUFF:{
                this.addToBot(new ApplyPowerAction(m, p, new SorceryVulnerable(m, 3), 3));
                this.addToBot(new ApplyPowerAction(m, p, new SorceryWeak(m, 3), 3));
                break;
            }
            case ESCAPE:{
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                this.addToBot(new ApplyPowerAction(p, p, new Namesis(p, 10),10));
                break;

            }
            case SLEEP:
            case STUN:{
                this.addToBot(new ApplyPowerAction(m, p, new SorceryVulnerable(m, this.magicNumber), this.magicNumber));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 3), 3));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 3), 3));
            }
            default:
                for (int i = 0; i < 2; i++) {
                    AbstractCard powerCard = getRandomShamarePowerCard();
                    powerCard.setCostForTurn(0); // 将卡牌的能量消耗设置为 0
                    this.addToBot(new MakeTempCardInHandAction(powerCard, 1));
                }
                break;
        }
    }

    private AbstractCard getRandomShamarePowerCard() {
        ArrayList<AbstractCard> powerCards = new ArrayList<>();
        powerCards.add(new Salvation());
        powerCards.add(new PeaceAltar());
        powerCards.add(new CalamityGrasp());
        powerCards.add(new HirudoTherapy());
        powerCards.add(new PlagueScholar());
        powerCards.add(new EdgeRunner());
        powerCards.add(new DimBud());
        powerCards.add(new Defile());
        powerCards.add(new Aruspicy());
        powerCards.add(new TheFool());
        powerCards.add(new Deal());

        Random rand = new Random();
        return powerCards.get(rand.nextInt(powerCards.size()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThoughtSteal();
    }
}