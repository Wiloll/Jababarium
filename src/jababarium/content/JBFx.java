package jababarium.content;

import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.IntMap;
import arc.util.Time;
import arc.math.geom.Position;
import arc.util.Tmp;
import jababarium.util.graphic.DrawFunc;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Trail;

import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;
import static arc.math.Angles.randLenVectors;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import java.util.Arrays;

public class JBFx {
    public static final float EFFECT_MASK = Layer.effect + 0.0001f;
    public static final float EFFECT_BOTTOM = Layer.bullet - 0.11f;
    public static final IntMap<Effect> same = new IntMap<>();
    public static final float lightningAlign = 0.5f;
    private static final Rand rand = new Rand();
    // private static final Rand rand = new Rand();
    // private static final Rand rand2 = new Rand();
    // private static final Vec2 v = new Vec2();
    // private static final int[] oneArr = { 1 };
    public static Effect trailFadeFast = new Effect(600f, e -> {
        if (!(e.data instanceof Trail))
            return;
        Trail trail = e.data();
        // lifetime is how many frames it takes to fade out the trail
        e.lifetime = trail.length * 1.4f;

        if (!state.isPaused()) {
            trail.shorten();
            trail.shorten();
        }
        trail.drawCap(e.color, e.rotation * e.foutpow());
        trail.draw(e.color, e.rotation * e.foutpow());
    }),

            boolSelector = new Effect(0, 0, e -> {
            }),

            attackWarningPos = new Effect(120f, 2000f, e -> {
                if (!(e.data instanceof Position))
                    return;

                e.lifetime = e.rotation;
                Position pos = e.data();

                Draw.color(e.color);
                TextureRegion arrowRegion = JBContent.arrowRegion;
                float scl = Mathf.curve(e.fout(), 0f, 0.1f);
                Lines.stroke(2 * scl);
                Lines.line(pos.getX(), pos.getY(), e.x, e.y);
                Fill.circle(pos.getX(), pos.getY(), Lines.getStroke());
                Fill.circle(e.x, e.y, Lines.getStroke());
                Tmp.v1.set(e.x, e.y).sub(pos).scl(e.fin(Interp.pow2In)).add(pos);
                Draw.rect(arrowRegion, Tmp.v1.x, Tmp.v1.y, arrowRegion.width * scl * Draw.scl,
                        arrowRegion.height * scl * Draw.scl, pos.angleTo(e.x, e.y) - 90f);
            }),

            lightningSpark = new Effect(Fx.chainLightning.lifetime, e -> {
                color(Color.white, e.color, e.fin() + 0.25f);

                stroke(0.65f + e.fout());

                randLenVectors(e.id, 3, e.fin() * e.rotation + 6f,
                        (x, y) -> lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 4 + 2f));

                Fill.circle(e.x, e.y, 2.5f * e.fout());
            }),

            lightningHitLarge = new Effect(50f, 180f, e -> {
                color(e.color);
                Drawf.light(e.x, e.y, e.fout() * 90f, e.color, 0.7f);
                e.scaled(25f, t -> {
                    stroke(3f * t.fout());
                    circle(e.x, e.y, 3f + t.fin(Interp.pow3Out) * 80f);
                });
                Fill.circle(e.x, e.y, e.fout() * 8f);
                randLenVectors(e.id + 1, 4, 1f + 60f * e.finpow(),
                        (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 5f));

                color(Color.gray);
                Angles.randLenVectors(e.id, 8, 2.0F + 30.0F * e.finpow(),
                        (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4.0F + 0.5F));
            }),

            hitSparkHuge = new Effect(70, e -> {
                color(e.color, Color.white, e.fout() * 0.3f);
                stroke(e.fout() * 1.6f);

                rand.setSeed(e.id);
                randLenVectors(e.id, 26, e.finpow() * 65f, (x, y) -> {
                    float ang = Mathf.angle(x, y);
                    lineAngle(e.x + x, e.y + y, ang, e.fout() * rand.random(6, 9) + 3f);
                });
            }),

            lightningHitSmall = new Effect(Fx.chainLightning.lifetime, e -> {
                color(Color.white, e.color, e.fin() + 0.25f);

                e.scaled(7f, s -> {
                    stroke(0.5f + s.fout());
                    Lines.circle(e.x, e.y, s.fin() * (e.rotation + 12f));
                });

                stroke(0.75f + e.fout());

                randLenVectors(e.id, 6, e.fin() * e.rotation + 7f,
                        (x, y) -> lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 4 + 2f));

                Fill.circle(e.x, e.y, 2.5f * e.fout());
            }),

            // chainLightningFadeReversed = new Effect(220f, 500f, e -> {
            // if (!(e.data instanceof Position))
            // return;
            // Position p = e.data();
            // float tx = e.x, ty = e.y, dst = Mathf.dst(p.getX(), p.getY(), tx, ty);
            // Tmp.v1.set(e.x, e.y).sub(p).nor();

            // e.lifetime = dst * 0.3f;
            // float normx = Tmp.v1.x, normy = Tmp.v1.y;
            // float range = e.rotation;
            // int links = Mathf.ceil(dst / range);
            // float spacing = dst / links;

            // Lines.stroke(2.5f * Mathf.curve(e.fout(), 0, 0.7f));
            // color(e.color, Color.white, e.fout() * 0.6f);

            // Lines.beginLine();

            // Fill.circle(p.getX(), p.getY(), Lines.getStroke() / 2);
            // Lines.linePoint(p);

            // rand.setSeed(e.id);

            // float fin = Mathf.curve(e.fin(), 0, lightningAlign);
            // int i;
            // float nx = p.getX(), ny = p.getY();
            // for (i = 0; i < (int) (links * fin); i++) {
            // if (i == links - 1) {
            // nx = tx;
            // ny = ty;
            // } else {
            // float len = (i + 1) * spacing;
            // Tmp.v1.setToRandomDirection(rand).scl(range / 2f);
            // nx = p.getX() + normx * len + Tmp.v1.x;
            // ny = p.getY() + normy * len + Tmp.v1.y;
            // }

            // linePoint(nx, ny);
            // }

            // Lines.endLine();
            // }).followParent(false),

            attackWarningRange = new Effect(120f, 2000f, e -> {
                Draw.color(e.color);
                Lines.stroke(2 * e.fout());
                Lines.circle(e.x, e.y, e.rotation);

                for (float i = 0.75f; i < 1.5f; i += 0.25f) {
                    Lines.spikes(e.x, e.y, e.rotation / i, e.rotation / 10f, 4, e.time);
                    Lines.spikes(e.x, e.y, e.rotation / i / 1.5f, e.rotation / 12f, 4, -e.time * 1.25f);
                }

                TextureRegion arrowRegion = JBContent.arrowRegion;
                float scl = Mathf.curve(e.fout(), 0f, 0.1f);

                for (int l = 0; l < 4; l++) {
                    float angle = 90 * l;
                    float regSize = e.rotation / 150f;
                    for (int i = 0; i < 4; i++) {
                        Tmp.v1.trns(angle, (i - 4) * tilesize * e.rotation / tilesize / 4);
                        float f = (100 - (Time.time - 25 * i) % 100) / 100;

                        Draw.rect(arrowRegion, e.x + Tmp.v1.x, e.y + Tmp.v1.y, arrowRegion.width * regSize * f * scl,
                                arrowRegion.height * regSize * f * scl, angle - 90);
                    }
                }
            });

    public static Effect square(Color color, float lifetime, int num, float range, float size) {
        return new Effect(lifetime, e -> {
            Draw.color(color);
            rand.setSeed(e.id);
            randLenVectors(e.id, num, range * e.finpow(), (x, y) -> {
                float s = e.fout(Interp.pow3In) * (size + rand.range(size / 3f));
                Fill.square(e.x + x, e.y + y, s, 45);
                Drawf.light(e.x + x, e.y + y, s * 2.25f, color, 0.7f);
            });
        });
    }

    public static int hash(String m, Color c) {
        return Arrays.hashCode(new int[] { m.hashCode(), c.hashCode() });
    }

    public static Effect get(String m, Color c, Effect effect) {
        int hash = hash(m, c);
        Effect or = same.get(hash);
        if (or == null)
            same.put(hash, effect);
        return or == null ? effect : or;
    }

    public static Effect crossBlast(Color color) {
        return get("crossBlast", color, crossBlast(color, 72));
    }

    public static Effect crossBlast(Color color, float size) {
        return crossBlast(color, size, 0);
    }

    public static Effect crossBlast(Color color, float size, float rotate) {
        return new Effect(Mathf.clamp(size / 3f, 35f, 240f), size * 2, e -> {
            color(color, Color.white, e.fout() * 0.55f);
            Drawf.light(e.x, e.y, e.fout() * size, color, 0.7f);

            e.scaled(10f, i -> {
                stroke(1.35f * i.fout());
                circle(e.x, e.y, size * 0.7f * i.finpow());
            });

            rand.setSeed(e.id);
            float sizeDiv = size / 1.5f;
            float randL = rand.random(sizeDiv);

            for (int i = 0; i < 4; i++) {
                DrawFunc.tri(e.x, e.y, size / 20 * (e.fout() * 3f + 1) / 4 * (e.fout(Interp.pow3In) + 0.5f) / 1.5f,
                        (sizeDiv + randL) * Mathf.curve(e.fin(), 0, 0.05f) * e.fout(Interp.pow3), i * 90 + rotate);
            }
        });
    }

    public static Effect blast(Color color, float range) {
        float lifetime = Mathf.clamp(range * 1.5f, 90f, 600f);
        return new Effect(lifetime, range * 2.5f, e -> {
            color(color);
            Drawf.light(e.x, e.y, e.fout() * range, color, 0.7f);

            e.scaled(lifetime / 3, t -> {
                stroke(3f * t.fout());
                circle(e.x, e.y, 8f + t.fin(Interp.circleOut) * range * 1.35f);
            });

            e.scaled(lifetime / 2, t -> {
                Fill.circle(t.x, t.y, t.fout() * 8f);
                // if (NHSetting.enableDetails())
                Angles.randLenVectors(t.id + 1, (int) (range / 13), 2 + range * 0.75f * t.finpow(), (x, y) -> {
                    Fill.circle(t.x + x, t.y + y, t.fout(Interp.pow2Out) * Mathf.clamp(range / 15f, 3f, 14f));
                    Drawf.light(t.x + x, t.y + y, t.fout(Interp.pow2Out) * Mathf.clamp(range / 15f, 3f, 14f), color,
                            0.5f);
                });
            });

            // if (!NHSetting.enableDetails()) return;
            Draw.z(Layer.bullet - 0.001f);
            color(Color.gray);
            alpha(0.85f);
            float intensity = Mathf.clamp(range / 10f, 5f, 25f);
            for (int i = 0; i < 4; i++) {
                rand.setSeed(((long) e.id << 1) + i);
                float lenScl = rand.random(0.4f, 1f);
                int fi = i;
                e.scaled(e.lifetime * lenScl, eIn -> {
                    randLenVectors(eIn.id + fi - 1, eIn.fin(Interp.pow10Out), (int) (intensity / 2.5f), 8f * intensity,
                            (x, y, in, out) -> {
                                float fout = eIn.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                                Fill.circle(eIn.x + x, eIn.y + y, fout * ((2f + intensity) * 1.8f));
                            });
                });
            }
        });
    }

    public static Effect circleOut(float lifetime, float radius, float thick) {
        return new Effect(lifetime, radius * 2f, e -> {
            Draw.color(e.color, Color.white, e.fout() * 0.7f);
            Lines.stroke(thick * e.fout());
            Lines.circle(e.x, e.y, radius * e.fin(Interp.pow3Out));
        });
    }

    public static Effect circleOut(Color color, float range) {
        return new Effect(Mathf.clamp(range / 2, 45f, 360f), range * 1.5f, e -> {
            rand.setSeed(e.id);

            Draw.color(Color.white, color, e.fin() + 0.6f);
            float circleRad = e.fin(Interp.circleOut) * range;
            Lines.stroke(Mathf.clamp(range / 24, 4, 20) * e.fout());
            Lines.circle(e.x, e.y, circleRad);
            // if (NHSetting.enableDetails()) for (int i = 0; i < Mathf.clamp(range / 12, 9,
            // 60); i++)
            {
                Tmp.v1.set(1, 0).setToRandomDirection(rand).scl(circleRad);
                DrawFunc.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, rand.random(circleRad / 16, circleRad / 12) * e.fout(),
                        rand.random(circleRad / 4, circleRad / 1.5f) * (1 + e.fin()) / 2, Tmp.v1.angle() - 180);
            }
        });
    }

    public static Effect polyTrail(Color fromColor, Color toColor, float size, float lifetime) {
        return new Effect(lifetime, size * 2, e -> {
            color(fromColor, toColor, e.fin());
            Fill.poly(e.x, e.y, 6, size * e.fout(), e.rotation);
            Drawf.light(e.x, e.y, e.fout() * size, fromColor, 0.7f);
        });
    }
}