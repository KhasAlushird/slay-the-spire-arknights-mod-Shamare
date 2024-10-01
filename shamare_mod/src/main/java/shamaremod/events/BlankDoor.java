package shamaremod.events;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

import shamaremod.cards.attack.SorceryStrike;
import shamaremod.cards.skill.SorceryDefend;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class BlankDoor extends AbstractImageEvent {
    public static final String ID = IdHelper.makePath("BlankDoor");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0;

    public BlankDoor() {
        super(NAME, DESCRIPTIONS[0], ImageHelper.get_event_img("BlankDoor"));

        if(AbstractDungeon.ascensionLevel >= 15){
            this.imageEventText.setDialogOption(OPTIONS[2],new SorceryStrike()); // 减少最大生命值并替换卡牌
        }
        else{
            this.imageEventText.setDialogOption(OPTIONS[0],new SorceryStrike()); // 减少最大生命值并替换卡牌
        }

        this.imageEventText.setDialogOption(OPTIONS[1]); // 什么都不做，离开
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractPlayer p = AbstractDungeon.player;
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                       // 处理第一个选项：减少最大生命值并替换卡牌
                        int maxHpLossPercentage = AbstractDungeon.ascensionLevel >= 15 ? 65 : 55;
                        int maxHpLoss = p.maxHealth * maxHpLossPercentage / 100;
                        p.decreaseMaxHealth(maxHpLoss);

                        List<AbstractCard> cardsToRemove = new ArrayList<>();
                        List<AbstractCard> cardsToAdd = new ArrayList<>();

                        for (AbstractCard card : p.masterDeck.group) {
                            if (card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE)) {
                                AbstractCard newCard = new SorceryStrike();
                                if (card.upgraded) {
                                    newCard.upgrade();
                                }
                                cardsToRemove.add(card);
                                cardsToAdd.add(newCard);
                            } else if (card.tags.contains(AbstractCard.CardTags.STARTER_DEFEND)) {
                                AbstractCard newCard = new SorceryDefend();
                                if (card.upgraded) {
                                    newCard.upgrade();
                                }
                                cardsToRemove.add(card);
                                cardsToAdd.add(newCard);
                            }
                        }

                        p.masterDeck.group.removeAll(cardsToRemove);
                        p.masterDeck.group.addAll(cardsToAdd);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[2]);
                        screenNum = 1;
                        break;

                    case 1:
                        // 处理第二个选项：什么都不做，离开此事件
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[2]);
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                this.openMap();
                break;
        }
    }
}