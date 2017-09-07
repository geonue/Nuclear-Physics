package org.halvors.nuclearphysics.client.render.block.machine;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderGasCentrifuge extends TileEntitySpecialRenderer<TileGasCentrifuge> {
    private static final OBJModelContainer modelPart = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "gas_centrifuge.obj"), Arrays.asList("C", "JROT", "KROT", "LROT", "MROT"));
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "gas_centrifuge.obj"), Arrays.asList("A", "B", "D", "E", "F", "G", "H", "I"));

    @Override
    public void renderTileEntityAt(TileGasCentrifuge tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        RenderUtility.rotateBlockBasedOnDirection(tile.getFacing());

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPart.render();
        GlStateManager.popMatrix();

        model.render();

        GlStateManager.popMatrix();
    }
}