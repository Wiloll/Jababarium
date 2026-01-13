package jababarium.content;

import arc.scene.ui.layout.Table;
import arc.audio.Sound;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import java.util.Random;

public class Artillery {

    public static Block manualArtillery;
    private static final Random rand = new Random();

    private static final float OPEN_SOUND_COOLDOWN = 60f;
    private static final float FIRE_SOUND_COOLDOWN = 75f;

    public static void load() {

        manualArtillery = new Block("tochka-u") {{
            size = 3;
            solid = true;
            update = true;
            configurable = true;

            hasItems = true;
            itemCapacity = 50;

            requirements(
                    Category.turret,
                    ItemStack.with(
                            Items.copper, 300,
                            Items.lead, 200,
                            Items.silicon, 150
                    )
            );
        }};

        manualArtillery.buildType = ManualArtilleryBuild::new;
    }

    public static class ManualArtilleryBuild extends Building {

        private float openSoundCooldown = 0f;
        private float fireSoundCooldown = 0f;

        @Override
        public void updateTile() {
            if (openSoundCooldown > 0f) openSoundCooldown -= delta();
            if (fireSoundCooldown > 0f) fireSoundCooldown -= delta();
        }

        @Override
        public void buildConfiguration(Table table) {
            table.margin(6f);

            playRandomOpenMenuSound();

            table.button("Выберите точку", () -> {
                configure(1);
            }).width(220f).height(50f);

            table.row();

            table.button("Огонь", () -> {
                playRandomFireSound();
                configure(2);
            }).width(220f).height(50f);
        }

        public void configured(Object value) {
            if (!(value instanceof Integer action)) return;

            switch (action) {
                case 1 -> {
                    // TODO TargetingMode
                }
                case 2 -> {
                    // TODO Launch strike
                }
            }
        }


        private void playRandomOpenMenuSound() {
            if (openSoundCooldown > 0f) return;

            Sound[] sounds = {
                    ModSounds.artilleryOpen1,
                    ModSounds.artilleryOpen2
            };

            sounds[rand.nextInt(sounds.length)].at(x, y);
            openSoundCooldown = OPEN_SOUND_COOLDOWN;
        }

        private void playRandomFireSound() {
            if (fireSoundCooldown > 0f) return;

            Sound[] sounds = {
                    ModSounds.artilleryFire1,
                    ModSounds.artilleryFire2,
                    ModSounds.artilleryFire3
            };

            sounds[rand.nextInt(sounds.length)].at(x, y);
            fireSoundCooldown = FIRE_SOUND_COOLDOWN;
        }
    }
}
