package shamaremod.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.relics.ShabbyDoll;

public class KindDoll extends AbstractImageEvent {
    public static final String ID = IdHelper.makePath("KindDoll");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0;

    public KindDoll() {
        super(NAME, DESCRIPTIONS[0], ImageHelper.get_event_img("KindDoll"));

        this.imageEventText.setDialogOption(OPTIONS[0]); // 获得40金币
        this.imageEventText.setDialogOption(OPTIONS[1], new Decay(),new ShabbyDoll()); // 获得ShabbyDoll遗物，并将1张腐朽置入你的牌组
        this.imageEventText.setDialogOption(OPTIONS[2]); // 什么都不做，离开
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractPlayer p = AbstractDungeon.player;
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        // 处理第一个选项：获得40金币
                        AbstractDungeon.player.gainGold(40);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        screenNum = 1;
                        break;

                    case 1:
                        // 处理第二个选项：获得ShabbyDoll遗物，并将1张腐朽置入你的牌组
                        AbstractRelic shabbyDoll = new ShabbyDoll();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, shabbyDoll);

                        // 检查并触发“御守”遗物
                        if (p.hasRelic(Omamori.ID)&&p.getRelic(Omamori.ID).counter > 0) {
                            Omamori omamori = (Omamori) p.getRelic(Omamori.ID);
                            if (omamori.counter > 0) {
                                ((Omamori) omamori).use();
                            } else {
                                AbstractCard decay = new Decay();
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(decay, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                            }
                        } else {
                            AbstractCard decay = new Decay();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(decay, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        }

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
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