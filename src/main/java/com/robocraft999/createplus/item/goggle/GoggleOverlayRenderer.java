package com.robocraft999.createplus.item.goggle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.components.structureMovement.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.gui.GuiGameElement;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner.OutlineEntry;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

public class GoggleOverlayRenderer {

	private static final Map<Object, OutlineEntry> outlines = CreateClient.OUTLINER.getOutlines();

	public static void renderOverlay(MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay,
		float partialTicks) {
		RayTraceResult objectMouseOver = Minecraft.getInstance().objectMouseOver;

		if (!(objectMouseOver instanceof BlockRayTraceResult))
			return;

		for (OutlineEntry entry : outlines.values()) {
			if (!entry.isAlive())
				continue;
			Outline outline = entry.getOutline();
			if (outline instanceof ValueBox && !((ValueBox) outline).isPassive) 
				return;
		}

		BlockRayTraceResult result = (BlockRayTraceResult) objectMouseOver;
		Minecraft mc = Minecraft.getInstance();
		ClientWorld world = mc.world;
		BlockPos pos = result.getPos();
		ItemStack headSlot = mc.player.getItemStackFromSlot(EquipmentSlotType.HEAD);
		TileEntity te = world.getTileEntity(pos);

		boolean wearingGoggles = 
				   ItemList.goggle_chainmail_helmet == headSlot.getItem()
				|| ItemList.goggle_diamond_helmet == headSlot.getItem()
				|| ItemList.goggle_golden_helmet == headSlot.getItem()
				|| ItemList.goggle_iron_helmet == headSlot.getItem()
				|| ItemList.goggle_leather_helmet == headSlot.getItem()
				|| ItemList.goggle_turtle_helmet == headSlot.getItem()
				|| ItemList.goggle_netherite_helmet == headSlot.getItem();
		
		//if(AllItems.GOGGLES.isIn(headSlot))wearingGoggles = true;
		
		ModLoadedCondition curiosloaded = new ModLoadedCondition("curios");
		if(curiosloaded.test()) {
			LazyOptional<IItemHandlerModifiable> test = CuriosApi.getCuriosHelper().getEquippedCurios(mc.player);
			IItemHandlerModifiable test2 = test.orElse(null);
			if(test2 != null) {
				for(int i = 0; i < test2.getSlots();i++) {
					ItemStack curiosSlot = test2.getStackInSlot(i);
					if(curiosSlot.getItem() == AllItems.GOGGLES.get())wearingGoggles = true;
				}
			}
			
		
		}
		/*	
		ModLoadedCondition mekloaded = new ModLoadedCondition("mekanism");
		if(mekloaded.test()) {
			if(headSlot.getItem() == MekanismItems.MEKASUIT_HELMET.asItem()) {
				ItemMekaSuitArmor helmet = (ItemMekaSuitArmor)headSlot.getItem();
				if(helmet.hasModule(headSlot, Modules.VISION_ENHANCEMENT_UNIT)) {//TODO add own Module
					if(helmet.isModuleEnabled(headSlot, Modules.VISION_ENHANCEMENT_UNIT)) {//TODO add own Module
						wearingGoggles = true;
					}
				}
			}
		}*/

		boolean hasGoggleInformation = te instanceof IHaveGoggleInformation;
		boolean hasHoveringInformation = te instanceof IHaveHoveringInformation;

		boolean goggleAddedInformation = false;
		boolean hoverAddedInformation = false;

		List<ITextComponent> tooltip = new ArrayList<>();
		/*
		if (hasGoggleInformation && wearingGoggles) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isSneaking());
		}

		if (hasHoveringInformation) {
			if (!tooltip.isEmpty())
				tooltip.add(StringTextComponent.EMPTY);
			IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
			hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isSneaking());

			if (goggleAddedInformation && !hoverAddedInformation)
				tooltip.remove(tooltip.size() - 1);
		}

		if (te instanceof IDisplayAssemblyExceptions) {
			boolean exceptionAdded = ((IDisplayAssemblyExceptions) te).addExceptionToTooltip(tooltip);
			if (exceptionAdded) {
				hasHoveringInformation = true;
				hoverAddedInformation = true;
			}
		}*/
		
		if (hasGoggleInformation && wearingGoggles) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			if(hasHoveringInformation) {
				IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
				hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isSneaking());
			}
			if(!tooltip.isEmpty())tooltip.add(StringTextComponent.EMPTY);
			goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isSneaking());
		}

		// break early if goggle or hover returned false when present
		if ((hasGoggleInformation && !goggleAddedInformation) && (hasHoveringInformation && !hoverAddedInformation))
			return;

		// check for piston poles if goggles are worn
		BlockState state = world.getBlockState(pos);
		if (wearingGoggles && AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			Direction[] directions = Iterate.directionsInAxis(state.get(PistonExtensionPoleBlock.FACING)
				.getAxis());
			int poles = 1;
			boolean pistonFound = false;
			for (Direction dir : directions) {
				int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get()
					.attachedPoles(world, pos, dir);
				poles += attachedPoles;
				pistonFound |= world.getBlockState(pos.offset(dir, attachedPoles + 1))
					.getBlock() instanceof MechanicalPistonBlock;
			}

			if (!pistonFound)
				return;
			if (!tooltip.isEmpty())
				tooltip.add(StringTextComponent.EMPTY);

			tooltip.add(IHaveGoggleInformation.componentSpacing.copy()
				.append(Lang.translate("gui.goggles.pole_length"))
				.append(new StringTextComponent(" " + poles)));
		}

		if (tooltip.isEmpty())
			return;

		ms.push();
		Screen tooltipScreen = new TooltipScreen(null);
		tooltipScreen.init(mc, mc.getWindow()
			.getScaledWidth(),
			mc.getWindow()
				.getScaledHeight());

		int titleLinesCount = 1;
		int tooltipTextWidth = 0;
		for (ITextProperties textLine : tooltip) {
			int textLineWidth = mc.fontRenderer.getStringWidth(textLine.getString());
			if (textLineWidth > tooltipTextWidth)
				tooltipTextWidth = textLineWidth;
		}

		int tooltipHeight = 8;
		if (tooltip.size() > 1) {
			tooltipHeight += (tooltip.size() - 1) * 10;
			if (tooltip.size() > titleLinesCount)
				tooltipHeight += 2; // gap between title lines and next lines
		}

		int posX = tooltipScreen.width / 2 + AllConfigs.CLIENT.overlayOffsetX.get();
		int posY = tooltipScreen.height / 2 + AllConfigs.CLIENT.overlayOffsetY.get();
		
		posX = Math.min(posX, tooltipScreen.width - tooltipTextWidth - 20);
		posY = Math.min(posY, tooltipScreen.height - tooltipHeight - 20);
		
		tooltipScreen.renderTooltip(ms, tooltip, posX, posY);

		ItemStack item = AllItems.GOGGLES.asStack();
		GuiGameElement.of(item)
			.at(posX + 10, posY - 16, 450)
			.render(ms);
		ms.pop();
	}

	public static final class TooltipScreen extends Screen {
		public TooltipScreen(ITextComponent text) {
			super(text);
		}

		@Override
		public void init(Minecraft mc, int width, int height) {
			this.client = mc;
			this.itemRenderer = mc.getItemRenderer();
			this.textRenderer = mc.fontRenderer;
			this.width = width;
			this.height = height;
		}
	}

}
