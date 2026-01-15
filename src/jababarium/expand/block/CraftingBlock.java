package jababarium.expand.block;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Time;
import jababarium.util.graphic.DrawFunc;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.draw.*;
import jababarium.content.*;
import jababarium.expand.block.drawer.*;
import jababarium.expand.block.production.factory.MultiBlockCrafter;

public class CraftingBlock {
    public static Color stampingArc, processorBlue;

    public static Block
        feroniumFactory, nectroneDissociator, cryostalCompressor;

    public static void loadColors(){
        stampingArc = JBColor.lightSkyBack.cpy().lerp(Color.lightGray, 0.3f);
        processorBlue = Color.valueOf("cee5ed");
    }

    public static void load(){
        loadColors();
        feroniumFactory = new MultiBlockCrafter("feronium-factory"){{
            requirements(Category.crafting, ItemStack.with(
                JBItems.copper, 200,
                    JBItems.graphite, 35,
                    JBItems.silicon, 30
            ));

            size = 2;
            itemCapacity = 15;
            scaledHealth = 50;
            craftTime = 60;
            outputItems = ItemStack.with(JBItems.feronium, 2);

            consumeItems(ItemStack.with(JBItems.silicon, 2, JBItems.thorium, 2));
            consumePower(6f);

            drawer = new DrawMulti(
                    new DrawBaseRegion("-2x2"),
                    new DrawRegion(),
                    new DrawPistons() {{
                        sinScl = 8f;
                        sinMag = 2f;
                        sinOffset = 0;
                        lenOffset = -1f;
                    }},
                    new DrawRegion("-top")
            );

            craftEffect = JBFx.hugeSmokeGray;
            updateEffect = new Effect(80f, e -> {
                Fx.rand.setSeed(e.id);
                Draw.color(Color.lightGray, Color.gray, e.fin());
                Angles.randLenVectors(e.id, 4, 2.0F + 12.0F * e.fin(Interp.pow3Out), (x, y) -> {
                    Fill.circle(e.x + x, e.y + y, e.fout() * Fx.rand.random(1, 2.5f));
                });
            }).layer(Layer.blockOver + 1);
        }};

        nectroneDissociator = new MultiBlockCrafter("nectrone-dissociator"){{
            requirements(Category.crafting, ItemStack.with(
                    JBItems.feronium, 300,
                    JBItems.cryostal, 350,
                    JBItems.silicon, 400,
                    JBItems.thorium, 600,
                    JBItems.phaseFabric, 500
            ));

            size = 3;
            itemCapacity = 15;
            scaledHealth = 70;
            armor = 4;

            outputLiquids = LiquidStack.with(JBLiquids.nectron, 0.5f);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawRegion("-base"),
                    new DrawGlowRegion("-glow") {{color = JBColor.nectrone;}}
            );
            craftEffect = updateEffect = JBFx.square(JBColor.nectrone, 60, 6, 16, 3);
            consumePower(18f);
            consumeItems(ItemStack.with(JBItems.thorium, 4, JBItems.adamantium, 3, JBItems.sergium, 3));
        }};

        cryostalCompressor = new MultiBlockCrafter("cryostal-compressor"){{
            requirements(Category.crafting, ItemStack.with(
                    JBItems.feronium, 275,
                    JBItems.phaseFabric, 250,
                    JBItems.metaglass, 300,
                    JBItems.surgeAlloy, 150
            ));
            size = 3;
            itemCapacity = 20;
            scaledHealth = 40;
            hasLiquids = true;
            craftTime = 120f;
            liquidCapacity = 40;

            craftEffect = JBFx.crossBlast(JBColor.cryostal, 45f, 45f);
            craftEffect.lifetime *= 1.5f;
            updateEffect = JBFx.squareRand(JBColor.cryostal, 5f, 15f);

            outputItems =  ItemStack.with(JBItems.cryostal, 1f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(JBLiquids.cryofluid), new DrawRegion("-bottom-2"),
                    new DrawCrucibleFlame() {
                        {
                            flameColor = JBColor.cryostal;
                            midColor = Color.valueOf("2e2f34");
                            circleStroke = 1.05f;
                            circleSpace = 2.65f;
                        }

                        @Override
                        public void draw(Building build) {
                            if (build.warmup() > 0f && flameColor.a > 0.001f) {
                                Lines.stroke(circleStroke * build.warmup());

                                float si = Mathf.absin(flameRadiusScl, flameRadiusMag);
                                float a = alpha * build.warmup();

                                Draw.blend(Blending.additive);
                                Draw.color(flameColor, a);

                                float base = (Time.time / particleLife);
                                rand.setSeed(build.id);
                                for (int i = 0; i < particles; i++) {
                                    float fin = (rand.random(1f) + base) % 1f, fout = 1f - fin;
                                    float angle = rand.random(360f) + (Time.time / rotateScl) % 360f;
                                    float len = particleRad * particleInterp.apply(fout);
                                    Draw.alpha(a * (1f - Mathf.curve(fin, 1f - fadeMargin)));
                                    Fill.square(
                                            build.x + Angles.trnsx(angle, len),
                                            build.y + Angles.trnsy(angle, len),
                                            particleSize * fin * build.warmup(), 45
                                    );
                                }

                                Draw.blend();

                                Draw.color(midColor, build.warmup());
                                Lines.square(build.x, build.y, (flameRad + circleSpace + si) * build.warmup(), 45);

                                Draw.reset();
                            }
                        }
                    },
                    new DrawDefault(),
                    new DrawGlowRegion() {{
                        color = JBColor.cryostal;
                        layer = -1;
                        glowIntensity = 1.1f;
                        alpha = 1.1f;
                    }},
                    new DrawRotator(1f, "-top") {
                        @Override
                        public void draw(Building build) {
                            Drawf.spinSprite(rotator, build.x + x, build.y + y, DrawFunc.rotator_90(DrawFunc.cycle(build.totalProgress() * rotateSpeed, 0, craftTime), 0.15f));
                        }
                    }
            );
            consumePower(16f);
            consumeItems(ItemStack.with(JBItems.titanium, 3, JBItems.metaglass, 2, JBItems.feronium, 2));
            consumeLiquids(LiquidStack.with(JBLiquids.cryofluid, 0.03f));

        }};
    }
}
