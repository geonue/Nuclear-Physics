package org.halvors.nuclearphysics.client.render.block.machine;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends TileEntitySpecialRenderer<TileChemicalExtractor> {
    private static final OBJModelContainer modelPart = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"), Arrays.asList("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates"));
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"), Arrays.asList("CornerSupport1", "CornerSupport2", "CornerSupport3", "CornerSupport4", "EnergyPlug", "Keyboard", "KeyboardSupport", "MainBase", "SupportBeam1", "SupportBeam2"));

    @Override
    public void renderTileEntityAt(TileChemicalExtractor tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        RenderUtility.rotateBlockBasedOnDirection(tile.getFacing());

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.4375, 0.314388);
        GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 1, 0, 0);
        GlStateManager.translate(0, -0.4375, -0.314388);
        modelPart.render();
        GlStateManager.popMatrix();

        model.render();

        GlStateManager.popMatrix();
    }
}