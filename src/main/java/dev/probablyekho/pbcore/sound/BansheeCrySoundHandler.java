package dev.probablyekho.pbcore.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.EnderMan;

public class BansheeCrySoundHandler extends AbstractTickableSoundInstance {
    private final EnderMan enderMan;
    private int fadeDirection = 1;
    private int fade;

    public BansheeCrySoundHandler(EnderMan enderMan, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.enderMan = enderMan;
        this.looping = true;
        this.delay = 0;
        this.relative = false;
        this.volume = 0.01F;
    }

    @Override
    public void tick() {
        if (!enderMan.isAlive() || !enderMan.isCreepy()) {
            fadeOut();
        }
        this.x = enderMan.getX();
        this.y = enderMan.getY();
        this.z = enderMan.getZ();
        if (this.fade < 0) {
            this.stop();
            return;
        }
        this.fade = this.fade + this.fadeDirection;
        this.volume = Mth.clamp((float)this.fade / 40.0F, 0.0F, 1.0F);
    }

    public void fadeOut() {
        this.fade = Math.min(this.fade, 40);
        this.fadeDirection = -1;
    }

    public void fadeIn() {
        this.fade = Math.max(0, this.fade);
        this.fadeDirection = 1;
    }
}
