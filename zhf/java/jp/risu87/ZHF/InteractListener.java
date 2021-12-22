package jp.risu87.ZHF;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class InteractListener {
	
	public static volatile boolean ZHFState = false;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void interact(FMLNetworkEvent.ClientConnectedToServerEvent par1event) {
		ChannelPipeline pipeline = par1event.getManager().channel().pipeline();
		ChannelHandler outAdapter = (ChannelHandler)new ChannelOutboundHandlerAdapter() {
			@Override
			public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
				try {
					if (!InteractListener.ZHFState) {
						super.write(ctx, msg, promise);
						return;
					} 
					if (msg instanceof CPacketUseEntity) {
						if (((CPacketUseEntity)msg).getAction() == CPacketUseEntity.Action.ATTACK) {
							super.write(ctx, msg, promise);
							return;
						}
						World theWorld = Minecraft.getMinecraft().player.world;
						Entity clicked = ((CPacketUseEntity)msg).getEntityFromWorld(theWorld);
						//System.out.println(clicked.getClass());
						if (!(clicked instanceof net.minecraft.entity.item.EntityArmorStand)) {
							super.write(ctx, msg, promise);
							return;
						} 
						super.write(ctx, new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND), promise);
					} else {
						super.write(ctx, msg, promise);
					} 
				} catch (Exception e) {
					e.printStackTrace();
					super.write(ctx, msg, promise);
				} 
			}
        };
		pipeline.addLast(outAdapter);
	}
  
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent par1event) {
		if (Keybinds.toggleZHF.isPressed()) {
			ZHFState = !ZHFState;
			Minecraft.getMinecraft().player.sendMessage(
				(ITextComponent)new TextComponentString("Toggled ZHF to " + (ZHFState ? "on" : "off"))
			);
		} 
	}
  
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post par1event) {
		if (par1event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)
			return; 
		String str = "ZHF " + (ZHFState ? "on" : "off");
		FontRenderer fr = (Minecraft.getMinecraft()).fontRenderer;
		fr.drawStringWithShadow(
			str, 
			new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - fr.getStringWidth(str), 
			0f, 
			0xffff55
		);
	}
}
