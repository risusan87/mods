package jp.risu87.ZHF;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
	modid = ZombieHologramFixerCore.MODID,
	name = ZombieHologramFixerCore.NAME,
	version = ZombieHologramFixerCore.VERSION,
	acceptedMinecraftVersions = "1.12.2",
	canBeDeactivated = true,
	clientSideOnly = true
)
public class ZombieHologramFixerCore {
	public static final String MODID = "zombies_hologrambug_fixer";
	public static final String NAME = "Zombies Hologram Fixer";
	public static final String VERSION = "1.4.1";
  
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Keybinds.register();
		MinecraftForge.EVENT_BUS.register(new InteractListener());
	}
}