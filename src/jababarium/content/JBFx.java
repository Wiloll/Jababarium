package jababarium.content;

import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.graphics.Color;
//import arc.math.Rand;
//import arc.math.geom.Vec2;
import arc.struct.IntMap;
import arc.util.Time;
import arc.math.geom.Position;
import arc.util.Tmp;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Trail;
import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;
import static arc.math.Angles.randLenVectors;

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
}