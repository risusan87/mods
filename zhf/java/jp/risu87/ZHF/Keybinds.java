package jp.risu87.ZHF;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {
	public static KeyBinding toggleZHF;
  	
	public static void register() {
		toggleZHF = new KeyBinding("key.toggleZHF", 36, "key.categories.zhf");
		ClientRegistry.registerKeyBinding(toggleZHF);
	}
}