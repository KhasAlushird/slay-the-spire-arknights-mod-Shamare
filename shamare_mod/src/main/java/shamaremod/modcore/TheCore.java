package shamaremod.modcore;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.BaseMod;
import static basemod.BaseMod.logger;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import shamaremod.cards.status.FireDisaster;
import shamaremod.cards.status.HolyDisaster;
import shamaremod.cards.status.PoisonDisaster;
import shamaremod.cards.status.ShadowDisaster;
import shamaremod.character.Shamare;
import static shamaremod.character.Shamare.PlayerColorEnum.SHAMARE_CHARACTER;
import static shamaremod.character.Shamare.PlayerColorEnum.SHAMARE_COLOR;
import shamaremod.events.BlankDoor;
import shamaremod.events.GhostlyEvent;
import shamaremod.events.KindDoll;
import shamaremod.helpers.ImageHelper;
import shamaremod.potions.LivingEssence;
import shamaremod.potions.ViciousPotion;
import shamaremod.potions.WitchBlessing;
import shamaremod.relics.DelicateDoll;
import shamaremod.relics.ExonerationCertificate;
import shamaremod.relics.FabricApple;
import shamaremod.relics.GreedCoin;
import shamaremod.relics.ShabbyDoll;
import shamaremod.relics.SirenKiss;
import shamaremod.relics.SpiralHorn;


@SpireInitializer
public class TheCore implements EditCardsSubscriber,EditStringsSubscriber,EditCharactersSubscriber,EditRelicsSubscriber,EditKeywordsSubscriber,PostBattleSubscriber,OnStartBattleSubscriber,PostDungeonInitializeSubscriber,PostInitializeSubscriber{
    // 实现接口
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON =   ImageHelper.getOtherImgPath("character","Character_Button" );
    private static final String MY_CHARACTER_PORTRAIT = ImageHelper.getOtherImgPath("character","Character_Portrait" );
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_attack_512");
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_power_512");
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_skill_512");
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = ImageHelper.getOtherImgPath("character","small_orb");
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_attack_1024");
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_power_1024");
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = ImageHelper.getImgPathWithSubType("character", "cardback","bg_skill_1024");
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = ImageHelper.getOtherImgPath("character","big_orb");
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = ImageHelper.getOtherImgPath("character","energy_orb");
    public static final Color MY_COLOR = new Color(102.0F / 255.0F, 0.0F / 255.0F, 102.0F / 255.0F, 1.0F);

    public TheCore() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 这里注册颜色
        BaseMod.addColor(SHAMARE_COLOR, MY_COLOR,
         MY_COLOR, MY_COLOR,
          MY_COLOR, MY_COLOR,
           MY_COLOR, MY_COLOR,
           BG_ATTACK_512,BG_SKILL_512,
           BG_POWER_512,ENEYGY_ORB,
           BG_ATTACK_1024,BG_SKILL_1024,
           BG_POWER_1024,BIG_ORB,
           SMALL_ORB);
    }

    public static void initialize() {
        new TheCore();
    }


    @Override
    public void receivePostBattle(AbstractRoom room) {
        resetMagicNumberBonuses();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        resetMagicNumberBonuses();
    }

    @Override
    public void receivePostDungeonInitialize() {
        resetMagicNumberBonuses();
    }


    private void resetMagicNumberBonuses() {
        PoisonDisaster.resetMagicNumberBonus();
        FireDisaster.resetMagicNumberBonus();
        ShadowDisaster.resetMagicNumberBonus();
        HolyDisaster.resetMagicNumberBonus();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {

        //(shortcut : shift alt down)
        //attack
        basemod.BaseMod.addCard(new shamaremod.cards.attack.Strike());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.PoisonShot());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.CurseStrike());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.ArousingFlame());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.SacrificialStab());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.DarkAttack());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.WitherStrike());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.SoulSiphon());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.ViolentPleasure());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.InsultToInjury());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.BodyFluidReconcile());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.SuperNatural());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.Necrocytosis());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.ChoirOfPain());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.SinOutbreak());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.Betrayal());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.ScarletTide());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.Hospitable());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.ForbiddenWords());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.FireThornblade());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.CursedNova());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.Surgery());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.WitchBrew());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.DepartedEcho());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.SerialDisaster());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.NightmareGaze());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.Dressmaker());
        basemod.BaseMod.addCard(new shamaremod.cards.attack.SorceryStrike());

        //skill
        basemod.BaseMod.addCard(new shamaremod.cards.skill.Defend());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.Shadow());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.PoisonSeed());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.ShadowStep());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.Mercy());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.BloodCeremony());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.FinalVoodoo());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.SoulWell());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.SoulScorch());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.NecromanticPoison());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.BrokenImage());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.FireHeart());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.NightScentedTea());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.DarkAffine());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.FireWaltz());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.RedForm());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.IncurableDisease());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.EvilBoding());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.BloodBloom());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.OmenOfDefeat());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.SuspiciousApple());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.PoisonBarrier());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.NoxiousFlow());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.ThoughtSteal());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.InferiorReagent());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.ConcentratedSulfuricAcid());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.BlazeFurnace());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.DrinkingPoisonToQuenchThirst());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.TortureTreatment());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.AmuletOfUndying());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.DoomsdayProphecy());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.Whisper());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.SorceryDefend());
        basemod.BaseMod.addCard(new shamaremod.cards.skill.CursedApparition());
        //basemod.BaseMod.addCard(new shamaremod.cards.skill.MindControl());    abondoned

        //status
        basemod.BaseMod.addCard(new shamaremod.cards.status.PoisonDisaster());
        basemod.BaseMod.addCard(new shamaremod.cards.status.FireDisaster());
        basemod.BaseMod.addCard(new shamaremod.cards.status.ShadowDisaster());
        basemod.BaseMod.addCard(new shamaremod.cards.status.HolyDisaster());

        //powers
        basemod.BaseMod.addCard(new shamaremod.cards.power.Salvation());
        basemod.BaseMod.addCard(new shamaremod.cards.power.PeaceAltar());
        basemod.BaseMod.addCard(new shamaremod.cards.power.CalamityGrasp());
        basemod.BaseMod.addCard(new shamaremod.cards.power.HirudoTherapy());
        basemod.BaseMod.addCard(new shamaremod.cards.power.PlagueScholar());
        basemod.BaseMod.addCard(new shamaremod.cards.power.EdgeRunner());
        basemod.BaseMod.addCard(new shamaremod.cards.power.DimBud());
        basemod.BaseMod.addCard(new shamaremod.cards.power.Defile());
        basemod.BaseMod.addCard(new shamaremod.cards.power.Aruspicy());
        basemod.BaseMod.addCard(new shamaremod.cards.power.TheFool());
        basemod.BaseMod.addCard(new shamaremod.cards.power.Deal());

        //Curses
        basemod.BaseMod.addCard(new shamaremod.cards.curse.BlackBile());
        basemod.BaseMod.addCard(new shamaremod.cards.curse.FallenBlood());



    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "zhs"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "eng"; // 如果没有相应语言的版本，默认加载英语
        }

        logger.info("Loading localization files for language: " + lang);

        try {
            String cardsPath = "shamaremod/localization/" + lang + "/cards.json";
            if (Gdx.files.internal(cardsPath).exists()) {
                BaseMod.loadCustomStringsFile(CardStrings.class, cardsPath);
                logger.info("Loaded cards.json");
            } else {
                logger.error("File not found: " + cardsPath);
            }

            String charactersPath = "shamaremod/localization/" + lang + "/characters.json";
            if (Gdx.files.internal(charactersPath).exists()) {
                BaseMod.loadCustomStringsFile(CharacterStrings.class, charactersPath);
                logger.info("Loaded characters.json");
            } else {
                logger.error("File not found: " + charactersPath);
            }

            String powersPath = "shamaremod/localization/" + lang + "/powers.json";
            if (Gdx.files.internal(powersPath).exists()) {
                BaseMod.loadCustomStringsFile(PowerStrings.class, powersPath);
                logger.info("Loaded powers.json");
            } else {
                logger.error("File not found: " + powersPath);
            }

            String relicsPath = "shamaremod/localization/" + lang + "/relics.json";
            if (Gdx.files.internal(relicsPath).exists()) {
                BaseMod.loadCustomStringsFile(RelicStrings.class, relicsPath);
                logger.info("Loaded relics.json");
            } else {
                logger.error("File not found: " + relicsPath);
            }

            String potionsPath = "shamaremod/localization/" + lang + "/potions.json";
            if (Gdx.files.internal(potionsPath).exists()) {
                BaseMod.loadCustomStringsFile(PotionStrings.class, potionsPath);
                logger.info("Loaded potions.json");
            } else {
                logger.error("File not found: " + potionsPath);
            }

            String eventsPath = "shamaremod/localization/" + lang + "/events.json";
            if (Gdx.files.internal(eventsPath).exists()) {
                BaseMod.loadCustomStringsFile(EventStrings.class,eventsPath);
                logger.info("Loaded events.json");
            } else {
                logger.error("File not found: " + eventsPath);
            }
        } catch (Exception e) {
            logger.error("Failed to load localization files for language: " + lang, e);
        }
    }

    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new Shamare(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, SHAMARE_CHARACTER);
    }

    @Override
    public void receivePostInitialize() {
        // 注册自定义事件
        BaseMod.addEvent(GhostlyEvent.ID, GhostlyEvent.class,TheBeyond.ID);
        BaseMod.addEvent(KindDoll.ID, KindDoll.class,Exordium.ID);
        BaseMod.addEvent(BlankDoor.ID, BlankDoor.class,TheCity.ID);
    }


    @Override
    public void receiveEditRelics() {

        //register relics here
        // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        //BaseMod.addRelicToCustomPool(new EcoSpecimen(),MuelSyse.PlayerColorEnum.MUEL_COLOR);
        BaseMod.addRelicToCustomPool(new ShabbyDoll(),Shamare.PlayerColorEnum.SHAMARE_COLOR);
        BaseMod.addRelicToCustomPool(new DelicateDoll(),Shamare.PlayerColorEnum.SHAMARE_COLOR);
        BaseMod.addRelicToCustomPool(new ExonerationCertificate(),Shamare.PlayerColorEnum.SHAMARE_COLOR);
        BaseMod.addRelicToCustomPool(new FabricApple(),Shamare.PlayerColorEnum.SHAMARE_COLOR);
        BaseMod.addRelicToCustomPool(new SpiralHorn(),Shamare.PlayerColorEnum.SHAMARE_COLOR);
        BaseMod.addRelicToCustomPool(new GreedCoin(),Shamare.PlayerColorEnum.SHAMARE_COLOR);
        BaseMod.addRelic(new SirenKiss(), RelicType.SHARED);

        //register potions here
        //BaseMod.addPotion(CushionPotion.class, Color.GREEN, Color.YELLOW, Color.CLEAR, "MuelSyseKhas:CushionPotion", MY_CHARACTER);
        BaseMod.addPotion(ViciousPotion.class, Color.CLEAR, null, null, "ShamareKhas:ViciousPotion", SHAMARE_CHARACTER);
        BaseMod.addPotion(WitchBlessing.class, Color.CLEAR, null, null, "ShamareKhas:WitchBlessing", SHAMARE_CHARACTER);
        BaseMod.addPotion(LivingEssence.class, null,null,null, "ShamareKhas:LivingEssence", SHAMARE_CHARACTER);


}


@Override
public void receiveEditKeywords() {
    Gson gson = new Gson();
    String lang = "eng";
    if (Settings.language == Settings.GameLanguage.ZHS) {
        lang = "zhs";
    }
    logger.info("Loading keywords for language: " + lang);

    String json = Gdx.files.internal("shamaremod/localization/" + lang + "/keywords.json")
            .readString(String.valueOf(StandardCharsets.UTF_8));
    Keyword[] keywords = gson.fromJson(json, Keyword[].class);
    if (keywords != null) {
        for (Keyword keyword : keywords) {
            // 这个id要全小写
            BaseMod.addKeyword("shamarekhas", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
           logger.info("Loaded keyword: " + keyword.NAMES[0]);
        }
    } else {
         logger.warn("No keywords found for language: " + lang);
     }
}
}
