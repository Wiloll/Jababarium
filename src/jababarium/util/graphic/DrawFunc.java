package jababarium.util.graphic;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import arc.util.Tmp;
import arc.util.pooling.Pools;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
//import arc.Core;
//import arc.func.Cons;
//import arc.func.Floatc2;
//import arc.graphics.Color;
//import arc.graphics.g2d.*;
//import arc.math.Angles;
//import arc.math.Interp;
//import arc.math.Mathf;
//import arc.math.Rand;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
//import arc.math.geom.Vec2;
//import arc.scene.ui.layout.Scl;
//import arc.struct.Seq;
//import arc.util.Align;
import arc.util.Time;
//import arc.util.Tmp;
//import arc.util.pooling.Pools;
//import mindustry.Vars;
//import mindustry.gen.Building;
//import mindustry.gen.Groups;
//import mindustry.graphics.Drawf;
//import mindustry.graphics.Layer;
//import mindustry.graphics.Pal;
//import mindustry.ui.Fonts;
import jababarium.Jababarium;
//import jababarium.util.func.NHFunc;
//import jababarium.util.func.NHInterp;

//import static mindustry.Vars.tilesize;

public class DrawFunc {
    public static final Color bottomColor = Pal.gray;
    public static final int[] oneArr = { 1 };
    public static final float sinScl = 1f;
    private static final Vec2 vec1 = new Vec2(),
            vec2 = new Vec2();
    // vec3 = new Vec2(),
    // vec4 = new Vec2();

    public static void posSquare(Color color, float stroke, float size, float x1, float y1, float x2, float y2) {

    }

    public static void posSquareLinkArr(Color color, float stroke, float size, boolean drawBottom, boolean linkLine,
            Position... pos) {
        if (pos.length < 2 || (!linkLine && pos[0] == null))
            return;

        for (int c : drawBottom ? Mathf.signs : oneArr) {
            for (int i = 1; i < pos.length; i++) {
                if (pos[i] == null)
                    continue;
                Position p1 = pos[i - 1], p2 = pos[i];
                Lines.stroke(stroke + 1 - c, c == 1 ? color : bottomColor);
                if (linkLine) {
                    if (p1 == null)
                        continue;
                    Lines.line(p2.getX(), p2.getY(), p1.getX(), p1.getY());
                } else {
                    Lines.line(p2.getX(), p2.getY(), pos[0].getX(), pos[0].getY());
                }
                Draw.reset();
            }

            for (Position p : pos) {
                if (p == null)
                    continue;
                Draw.color(c == 1 ? color : bottomColor);
                Fill.square(p.getX(), p.getY(), size + 1 - c / 1.5f, 45);
                Draw.reset();
            }
        }
    }

    public static void posSquareLink(Color color, float stroke, float size, boolean drawBottom, float x, float y,
            float x2, float y2) {
        posSquareLink(color, stroke, size, drawBottom, vec1.set(x, y), vec2.set(x2, y2));
    }

    public static void posSquareLink(Color color, float stroke, float size, boolean drawBottom, Position from,
            Position to) {
        posSquareLinkArr(color, stroke, size, drawBottom, false, from, to);
    }

    public static void drawConnected(float x, float y, float size, Color color) {
        Draw.reset();
        float sin = Mathf.absin(Time.time * sinScl, 8f, 1.25f);

        for (int i = 0; i < 4; i++) {
            float length = size / 2f + 3 + sin;
            Tmp.v1.trns(i * 90, -length);
            Draw.color(Pal.gray);
            Draw.rect(Jababarium.name("linked-arrow-back"), x + Tmp.v1.x, y + Tmp.v1.y, i * 90);
            Draw.color(color);
            Draw.rect(Jababarium.name("linked-arrow"), x + Tmp.v1.x, y + Tmp.v1.y, i * 90);
        }
        Draw.reset();
    }

    public static void overlayText(Font font, String text, float x, float y, float offset, float offsetScl, float size,
            Color color, boolean underline, boolean align) {
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(size / Scl.scl(1.0f));
        layout.setText(font, text);
        font.setColor(color);

        float dy = offset + 3.0F;
        font.draw(text, x, y + layout.height / (align ? 2 : 1) + (dy + 1.0F) * offsetScl, 1);
        --dy;

        if (underline) {
            Lines.stroke(2.0F, Color.darkGray);
            Lines.line(x - layout.width / 2.0F - 2.0F, dy + y, x + layout.width / 2.0F + 1.5F, dy + y);
            Lines.stroke(1.0F, color);
            Lines.line(x - layout.width / 2.0F - 2.0F, dy + y, x + layout.width / 2.0F + 1.5F, dy + y);
            Draw.color();
        }

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1.0F);
        Draw.reset();
        Pools.free(layout);
    }

    public static void overlayText(String text, float x, float y, float offset, Color color, boolean underline) {
        overlayText(Fonts.outline, text, x, y, offset, 1, 0.25f, color, underline, false);
    }

    public static void tri(float x, float y, float width, float length, float angle) {
        float wx = Angles.trnsx(angle + 90, width), wy = Angles.trnsy(angle + 90, width);
        Fill.tri(x + wx, y + wy, x - wx, y - wy, Angles.trnsx(angle, length) + x, Angles.trnsy(angle, length) + y);
    }
}