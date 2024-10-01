package shamaremod.actions;

import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import shamaremod.cards.curse.BlackBile;

//no animation !! abondoned!!!!

public class RandomCurseAction extends AbstractGameAction {

    public RandomCurseAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> curses = new ArrayList<>();
        curses.add(new AscendersBane());
        curses.add(new Clumsy());
        curses.add(new Decay());
        curses.add(new Doubt());
        curses.add(new Injury());
        curses.add(new Normality());
        curses.add(new Pain());
        curses.add(new Parasite());
        curses.add(new Regret());
        curses.add(new Shame());
        curses.add(new Writhe());
        curses.add(new BlackBile());

        Collections.shuffle(curses);
        AbstractCard randomCurse = curses.get(0);

       // 设置诅咒牌的位置和动画效果
       randomCurse.current_x = -1000.0F * Settings.xScale;
       randomCurse.current_y = Settings.HEIGHT / 2.0F;
       randomCurse.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.xScale;
       randomCurse.target_y = Settings.HEIGHT / 2.0F;

       AbstractDungeon.player.drawPile.addToRandomSpot(randomCurse);

       this.isDone = true;
    }
}