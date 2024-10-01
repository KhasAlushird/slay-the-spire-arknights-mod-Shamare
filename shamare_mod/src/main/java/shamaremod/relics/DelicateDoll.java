package shamaremod.relics;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import static com.megacrit.cardcrawl.events.AbstractEvent.logMetricRelicSwap;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import shamaremod.helpers.IdHelper;
import shamaremod.helpers.ImageHelper;
import shamaremod.powers.SorceryVulnerable;
import shamaremod.powers.SorceryWeak;

// 继承CustomRelic
public class DelicateDoll extends CustomRelic {
    // 遗物ID
    public static final String ID = IdHelper.makePath("DelicateDoll");
    // 图片路径
    private static final String IMG_PATH =ImageHelper.getOtherImgPath("relics", "DelicateDoll");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public DelicateDoll() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
       
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        AbstractCreature p = AbstractDungeon.player;
        super.atBattleStart();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        //add debuff to all enemies
        for( AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
            addToBot(new ApplyPowerAction(monster, p, new SorceryVulnerable(monster, 1), 1));
            addToBot(new ApplyPowerAction(monster, p, new SorceryWeak(monster, 2), 2));
        }


    }

    public void onEquip() {
            int relicAtIndex = -1;
            int relicsize=AbstractDungeon.player.relics.size();
        for (int i = 0; i < relicsize; i++) {
            if (AbstractDungeon.player.relics.get(i).relicId.equals("ShamareKhas:ShabbyDoll")) {
                relicAtIndex = i;
                break;
            }
        }
        if (relicAtIndex != -1) {
            AbstractDungeon.player.relics.get(relicAtIndex).onUnequip();
            AbstractRelic DelicateDoll = RelicLibrary.getRelic("ShamareKhas:DelicateDoll").makeCopy();
            DelicateDoll.instantObtain(AbstractDungeon.player, relicAtIndex, false);
            logMetricRelicSwap("DelicateDoll", "Swapped Relic", DelicateDoll, AbstractDungeon.player.relics.get(relicAtIndex));
        }         
          // 检查是否有多余的 DelicateDoll
        int exuberantPlantCount = 0;
        int lastExuberantPlantIndex = -1;
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if (AbstractDungeon.player.relics.get(i).relicId.equals("ShamareKhas:DelicateDoll")) {
                exuberantPlantCount++;
                lastExuberantPlantIndex = i;
            }
        }

        // 如果有多余的 DelicateDoll，将最后一个替换为 Circlet
        if (exuberantPlantCount > 1 && lastExuberantPlantIndex != -1) {
            AbstractDungeon.player.relics.get(lastExuberantPlantIndex).onUnequip();
            AbstractRelic circlet = RelicLibrary.getRelic("Circlet").makeCopy();
            circlet.instantObtain(AbstractDungeon.player, lastExuberantPlantIndex, false);
            logMetricRelicSwap("DelicateDoll", "Swapped Relic", circlet, AbstractDungeon.player.relics.get(lastExuberantPlantIndex));
        }
}  


    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("ShamareKhas:ShabbyDoll");
      }

    public AbstractRelic makeCopy() {
        return new DelicateDoll();
    }
}
