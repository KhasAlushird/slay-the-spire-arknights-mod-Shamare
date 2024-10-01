package shamaremod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.OnPlayerTurnStartPostDrawSubscriber;
import shamaremod.actions.TheFoolAction;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;

public class TheFoolPower extends AbstractPower implements OnPlayerTurnStartPostDrawSubscriber {
    public static final String POWER_ID = IdHelper.makePath("TheFoolPower");
    
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    public static final String NAME = powerStrings.NAME;
    
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static byte power_applyed = 0;
    private byte power_uuid = 0;

    public TheFoolPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.power_uuid = power_applyed;
        power_uuid++;
        power_applyed++;
        updateDescription();
        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = true;

        // 添加一大一小两张能力图
        String path128 = ImageHelper.getOtherImgPath("powers", "TheFoolPower_96");
        String path48 = ImageHelper.getOtherImgPath("powers", "TheFoolPower_35");
        this.region128 = new AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 96, 96);
        this.region48 = new AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 35, 35);
        
        // 注册钩子
        basemod.BaseMod.subscribe(this);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }
    
    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount); // 这样，%d就被替换成能力的层数
    }

    @Override
    public void receiveOnPlayerTurnStartPostDraw() {
        if(AbstractDungeon.player.hasPower(this.ID)){
            if(this.power_uuid==power_applyed){
                flash();
                addToBot(new TheFoolAction(AbstractDungeon.player, this.amount));
            }

        }

    }


    @Override
    public void onRemove() {
        // 取消注册钩子
        basemod.BaseMod.unsubscribeLater(this);
    }

    public static void reset_power_applyed(){
        power_applyed = 0;
    }
}