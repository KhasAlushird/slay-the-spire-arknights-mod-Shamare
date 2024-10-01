package shamaremod.powers;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class DefilePower extends AbstractPower {
    public static final String POWER_ID = IdHelper.makePath("DefilePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DefilePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = true;

        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "DefilePower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "DefilePower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount,this.amount,this.amount*3); // 这样，%d就被替换成能力的层数
    }

    @Override
    public void atEndOfRound() {
        flash();
        // 随机选择一个 debuff
        Random rand = new Random();
        int debuff = rand.nextInt(3);
        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (randomMonster != null) {
            AbstractPower power;
            switch (debuff) {
                case 0:
                    power = new SorceryVulnerable(randomMonster, this.amount);
                    addToBot(new ApplyPowerAction(randomMonster, this.owner, power, this.amount));
                    break;
                case 1:
                    power = new SorceryWeak(randomMonster, this.amount);
                    addToBot(new ApplyPowerAction(randomMonster, this.owner, power, this.amount));
                    break;
                case 2:
                    power = new SorceryPoison(randomMonster, this.amount*3);
                    addToBot(new ApplyPowerAction(randomMonster, this.owner, power, this.amount*3));
                    break;
                default:
                    break;
            }
            
        }
    }
}