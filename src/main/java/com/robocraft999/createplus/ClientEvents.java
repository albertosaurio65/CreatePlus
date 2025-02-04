package com.robocraft999.createplus;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.robocraft999.createplus.item.goggle.GoggleOverlayRenderer;
import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.content.contraptions.base.IRotate.SpeedLevel;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent; 
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import static net.minecraft.util.text.TextFormatting.GRAY;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {
	
	private static final String itemPrefix = "item." + Create.ID;
	private static final String blockPrefix = "block." + Create.ID;
	
	@SubscribeEvent
	public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
		MatrixStack ms = event.getMatrixStack();
		Impl buffers = Minecraft.getInstance()
			.getBufferBuilders()
			.getEntityVertexConsumers();
		int light = 0xF000F0;
		int overlay = OverlayTexture.DEFAULT_UV;
		float pt = event.getPartialTicks();

		if (event.getType() != ElementType.HOTBAR)
			return;

		GoggleOverlayRenderer.renderOverlay(ms, buffers, light, overlay, pt);
	}

	//@SubscribeEvent
	public static void addToItemTooltipAlt(ItemTooltipEvent event){
		
			//System.out.println(TooltipHelper.cachedTooltips);
			ItemStack stack = event.getItemStack();
			String translationKey = stack.getItem()
					.getTranslationKey(stack);
			if (/*!translationKey.startsWith(itemPrefix) && */!translationKey.startsWith(blockPrefix))
				return;
			if(stack == null)return;
			if (TooltipHelper.hasTooltip(stack, event.getPlayer())) {
				List<ITextComponent> itemTooltip = event.getToolTip();
				List<ITextComponent> toolTip = new ArrayList<>();
				toolTip.add(itemTooltip.remove(0));
				//TooltipHelper.getTooltip(stack).addInformation(toolTip);
				
				Block itemblock = Block.getBlockFromItem(stack.getItem());
				if(itemblock == null || itemblock instanceof AirBlock)return;
				boolean isEngine = itemblock instanceof EngineBlock;
				//System.out.println("isnengine: "+isEngine);
				SpeedLevel minimumRequiredSpeedLevel = isEngine ? SpeedLevel.NONE : ((IRotate) itemblock).getMinimumRequiredSpeedLevel();
				int speedReqindex = 1 + itemTooltip.indexOf(Lang.translate("tooltip.speedRequirement"));//.setStyle(GRAY));
				System.out.println(itemTooltip);
				//if(speedReqindex == 0)return;
				System.out.println("index: "+speedReqindex);
				
				StringTextComponent levelline = null;
				if(ItemList.goggle_chainmail_helmet == event.getPlayer().getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()) {
					//System.out.println("helm in slot");
					ITextComponent rpmUnit = Lang.translate("generic.unit.rpm");
					IFormattableTextComponent level = null;
					levelline = (StringTextComponent) itemTooltip.get(speedReqindex);
					level = new StringTextComponent(" (").append(""+minimumRequiredSpeedLevel.getSpeedValue()).append(rpmUnit).append("+)");
					if(!levelline.getString().contains(level.getString())) {
						levelline.append(level);//level.appendString(" (" + minimumRequiredSpeedLevel.getSpeedValue()).append(rpmUnit).appendString("+)");
					}
					if(speedReqindex > 0) {
						toolTip.set(speedReqindex, levelline);
						itemTooltip.set(speedReqindex, levelline);
					}
					//toolTip.add(new StringTextComponent("test"));
				}
				//if(level != null)
				//itemTooltip.addAll(0, toolTip);
			}
			//: {block.create.crushing_wheel.tooltip=com.simibubi.create.foundation.item.ItemDescription@62e099d7}
	}
	
	//@SubscribeEvent
	public static void addToItemTooltip(ItemTooltipEvent event) {
		if (!AllConfigs.CLIENT.tooltips.get())
			return;
		if (event.getPlayer() == null)
			return;
		
		System.out.println(event.getListenerList().getListeners(2));
		ItemStack stack = event.getItemStack();
		String translationKey = stack.getItem()
			.getTranslationKey(stack);
		
		if (/*!translationKey.startsWith(itemPrefix) && */!translationKey.startsWith(blockPrefix))
			return;
		
		//if (TooltipHelper.hasTooltip(stack, event.getPlayer())) {
			List<ITextComponent> itemTooltip = event.getToolTip();
			List<ITextComponent> toolTip = new ArrayList<>();
			toolTip.addAll(itemTooltip);
			//toolTip.remove(0);
			
			Block itemblock = Block.getBlockFromItem(stack.getItem());
			if(itemblock == null || itemblock instanceof AirBlock)return;
			boolean isEngine = itemblock instanceof EngineBlock;
			//System.out.println("isnengine: "+isEngine);
			SpeedLevel minimumRequiredSpeedLevel = isEngine ? SpeedLevel.NONE : ((IRotate) itemblock).getMinimumRequiredSpeedLevel();
			int speedReqindex = 1 + itemTooltip.indexOf(Lang.translate("tooltip.speedRequirement").formatted(GRAY));
			for(ITextComponent t : event.getToolTip()) {
				System.out.println(t.getString());
			}
			//if(speedReqindex == 0)return;
			System.out.println("index: "+speedReqindex);
			
			StringTextComponent levelline = null;
			if(ItemList.goggle_chainmail_helmet == event.getPlayer().getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()) {
				//System.out.println("helm in slot");
				ITextComponent rpmUnit = Lang.translate("generic.unit.rpm");
				IFormattableTextComponent level = null;
				levelline = (StringTextComponent) itemTooltip.get(speedReqindex).copy();
				level = new StringTextComponent(" (").append(""+minimumRequiredSpeedLevel.getSpeedValue()).append(rpmUnit).append("+)");
				if(!levelline.getString().contains(level.getString())) {
					levelline.append(level);//level.appendString(" (" + minimumRequiredSpeedLevel.getSpeedValue()).append(rpmUnit).appendString("+)");
				}
				if(speedReqindex > 0) {
					toolTip.set(speedReqindex, levelline);
					itemTooltip.add(speedReqindex, levelline);
					//System.out.println("test");
				}
			}
		//}
		/*
		if (stack.getItem() instanceof BlockItem) {
			BlockItem item = (BlockItem) stack.getItem();
			if (item.getBlock() instanceof IRotate || item.getBlock() instanceof EngineBlock) {
				List<ITextComponent> kineticStats = ItemDescription.getKineticStats(item.getBlock());
				if (!kineticStats.isEmpty()) {
					event.getToolTip()
						.add(new StringTextComponent(""));
					event.getToolTip()
						.addAll(kineticStats);
				}
			}
		}*/
		

		//PonderTooltipHandler.addToTooltip(event.getToolTip(), stack);
		//SequencedAssemblyRecipe.addToTooltip(event.getToolTip(), stack);
	}
	
}
