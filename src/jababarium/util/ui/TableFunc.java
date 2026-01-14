package jababarium.util.ui;

import static mindustry.Vars.tilesize;

import arc.Core;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.g2d.Lines;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;

public class TableFunc {
    public static final float LEN = 60f;
    private static Table pTable = new Table(), floatTable = new Table();
    private static final Vec2 ctrlVec = new Vec2();
    public static final float OFFSET = 12f;

    public static void selectPos(Table parentT, Cons<Point2> cons) {
        Prov<Touchable> original = parentT.touchablility;
        Touchable parentTouchable = parentT.touchable;

        parentT.touchablility = () -> Touchable.disabled;

        if (!pTable.hasParent())
            ctrlVec.set(Core.camera.unproject(Core.input.mouse()));

        if (!pTable.hasParent())
            pTable = new Table(Tex.clear) {
                {
                    update(() -> {
                        if (Vars.state.isMenu()) {
                            remove();
                        } else {
                            Vec2 v = Core.camera.project(World.toTile(ctrlVec.x) * tilesize,
                                    World.toTile(ctrlVec.y) * tilesize);
                            setPosition(v.x, v.y, 0);
                        }
                    });
                }

                @Override
                public void draw() {
                    super.draw();

                    Lines.stroke(9, Pal.gray);
                    drawLines();
                    Lines.stroke(3, Pal.accent);
                    drawLines();
                    // DrawFunc.overlayText("(" + World.unconv(ctrlVec.x) + ", " +
                    // World.unconv(ctrlVec.y) + ")", x + LEN * 1, y + OFFSET, 0, Pal.accent,
                    // false);
                }

                private void drawLines() {
                    Lines.square(x, y, 28, 45);
                    Lines.line(x - OFFSET * 4, y, 0, y);
                    Lines.line(x + OFFSET * 4, y, Core.graphics.getWidth(), y);
                    Lines.line(x, y - OFFSET * 4, x, 0);
                    Lines.line(x, y + OFFSET * 4, x, Core.graphics.getHeight());
                }
            };
    }
}
