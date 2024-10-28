package shamaremod.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class GhostlyEvent extends AbstractImageEvent {
    public static final String ID = IdHelper.makePath("GhostlyEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0;

    public GhostlyEvent() {
        super(NAME, DESCRIPTIONS[0], ImageHelper.get_event_img("GhostlyEvent"));

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.imageEventText.setDialogOption(OPTIONS[5]);// 失去16点生命值，移除2张牌
            this.imageEventText.setDialogOption(OPTIONS[6],new Normality());  // 添加1张凡庸和4张升级过后的灵体，进阶15额外添加1张凡庸
        } else {
            this.imageEventText.setDialogOption(OPTIONS[0]); // 失去10点生命值，移除2张牌 
            this.imageEventText.setDialogOption(OPTIONS[1],new Normality()); // 添加1张凡庸和3张升级过后的灵体
        }
        this.imageEventText.setDialogOption(OPTIONS[2]); // 什么都不做，离开此事件
    }

    
    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractPlayer p = AbstractDungeon.player;
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        // 处理第一个选项：失去生命值，移除2张牌
                        int hpLoss = 10;
                        if (AbstractDungeon.ascensionLevel >= 15) {
                            hpLoss = 16; // 进阶15扣血增多
                        }
                        p.damage(new DamageInfo(null, hpLoss, DamageInfo.DamageType.HP_LOSS));
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard card : p.masterDeck.group) {
                            group.addToTop(card);
                        }
                        AbstractDungeon.gridSelectScreen.open(group, 2, OPTIONS[4], false, false, false, true);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        screenNum = 1;
                        break;
     
                    case 1:

                            // 处理第二个选项：添加1张凡庸和3张升级过后的灵体
                        AbstractCard ghostly1 = new Apparition();
                        AbstractCard ghostly2 = new Apparition();
                        AbstractCard ghostly4 = new Apparition();
                        ghostly1.upgrade();
                        ghostly2.upgrade();
                        ghostly4.upgrade();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ghostly1, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ghostly2, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ghostly4, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        if (AbstractDungeon.ascensionLevel >= 15) {
                            AbstractCard ghostly3 = new Apparition();
                            ghostly3.upgrade();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ghostly3, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        }
                       
                        if (!p.hasRelic(Omamori.ID)||p.getRelic(Omamori.ID).counter==0) {
                            AbstractCard normality = new Normality();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(normality, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                                                    // 进阶15额外添加1张凡庸
                            if (AbstractDungeon.ascensionLevel >= 15) {
                                AbstractCard extraNormality = new Normality();
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(extraNormality, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                            }
                        } else {
                            if (p.hasRelic(Omamori.ID)) {
                                AbstractRelic omamori = p.getRelic(Omamori.ID);
                                ((Omamori) omamori).use();
                            }
                            
                        }
                        
                        
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        screenNum = 1;
                        break;
                    
                    case 2:
                        // 处理第三个选项：什么都不做，离开此事件
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
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

                   