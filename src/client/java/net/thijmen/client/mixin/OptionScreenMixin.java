package net.thijmen.client.mixin;


import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.thijmen.client.screens.KeySettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionScreenMixin extends Screen {
    protected OptionScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init")
    private void inject(CallbackInfo ci) {
        Button button = Button.builder(Component.literal("K"), (btn)-> {
            minecraft.gui.setScreen(new KeySettingsScreen(Component.empty()));
        }).bounds((int) (width * 0.05), (int) (height * 0.05), (int) (width * 0.05), (int) (width * 0.05)).build();
        this.addRenderableWidget(button);
    }
}
