package shamaremod.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class SirenKiss extends CustomRelic {
    public static final String ID = IdHelper.makePath("SirenKiss");
    private static final String IMG_PATH = ImageHelper.getOtherImgPath("relics", "SirenKiss");
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    private boolean usedThisCombat = false;

    public SirenKiss() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        this.grayscale = false;
        this.pulse = true;
        beginPulse();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (!usedThisCombat && (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS)) {
            flash();
            this.pulse = false;
            addToTop(new DrawCardAction(AbstractDungeon.player, 3));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            usedThisCombat = true;
            this.grayscale = true;
        }
    }

    @Override
    public void atBattleStart() {
        AbstractCreature p = AbstractDungeon.player;
        super.atBattleStart();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        usedThisCombat = false;
        this.grayscale = false;

        


    }

    @Override
    public void onVictory() {
        this.pulse = false;
        usedThisCombat = false;
        this.grayscale = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SirenKiss();
    }
}