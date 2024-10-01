package shamaremod.potions;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.ReflectionHacks;
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
import shamaremod.helpers.IdHelper;

public class WitchBlessing extends AbstractPotion {
    public static final String POTION_ID = IdHelper.makePath("WitchBlessing");

    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(IdHelper.makePath("WitchBlessing"));

    public WitchBlessing() {
        super(potionStrings.NAME, IdHelper.makePath("WitchBlessing"), AbstractPotion.PotionRarity.UNCOMMON, AbstractPotion.PotionSize.BOTTLE, AbstractPotion.PotionColor.NONE);
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", new Texture("shamaremod/images/potions/WitchBlessing.png"));
         this.labOutlineColor = Color.PURPLE;
        this.isThrown = true;
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target) {
        for (int i = 0; i < this.potency; i++) {
            AbstractCard powerCard = getRandomShamarePowerCard();
            powerCard.setCostForTurn(0); // 将卡牌的能量消耗设置为 0
            this.addToBot(new MakeTempCardInHandAction(powerCard, 1));
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

    public int getPotency(int ascensionLevel) {
        return 2;
    }

    public AbstractPotion makeCopy() {
        return new WitchBlessing();
    }
}