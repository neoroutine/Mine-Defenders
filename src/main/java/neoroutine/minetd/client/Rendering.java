package neoroutine.minetd.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.generators.minigenerator.MiniGeneratorScreen;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseScreen;
import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import neoroutine.minetd.common.blocks.towers.pawn.PawnScreen;
import neoroutine.minetd.common.blocks.towers.rook.RookScreen;
import neoroutine.minetd.common.items.TowerAnalyzer;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MineTD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Rendering
{

    @SubscribeEvent
    public static void renderLast(RenderLevelLastEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (!(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof TowerAnalyzer))
        {
            return;
        }

        //Beginning
        PoseStack matrix = event.getPoseStack();
        matrix.pushPose();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        VertexConsumer bufferConsumer = bufferSource.getBuffer(RenderType.lines());

        PoseStack.Pose transform = matrix.last();
        Matrix4f pose = transform.pose();
        Matrix3f normal = transform.normal();

        var cameraInfo = minecraft.gameRenderer.getMainCamera();
        var camera = cameraInfo.getPosition();
        var viewer = cameraInfo.getEntity();
        if (viewer == null)
            return;

        var targeted = viewer.pick(20.0D, 0.0F, false);
        BlockPos targetedPos = null;

        if (TowerAnalyzer.cachedBlockPos == null)
        {
            targetedPos = targeted.getType() != HitResult.Type.BLOCK ? viewer.blockPosition() : ((BlockHitResult) targeted).getBlockPos();
        }
        else
        {
            targetedPos = TowerAnalyzer.cachedBlockPos;
        }

        var targetedBlockEntity = viewer.level.getBlockEntity(targetedPos);


        int r = 255;
        int g = 0;
        int b = 0;

        var start = camera;
        var end = camera.add(10, 0, 0);

        matrix.translate(-camera.x, -camera.y, -camera.z);
        //matrix.translate(targetedPos.getX(), targetedPos.getY(), targetedPos.getZ());

        if (targetedBlockEntity != null)
        {
            //Later will be instanceof Tower tower
            if (targetedBlockEntity instanceof TowerBlockEntity tower)
            {
                tower.grandmaster.setPlayer(player);

                r = 0;
                g = 255;
                b = 0;


                var bb = tower.getReachBox();


                float offsetX = 0f;
                float offsetY = 0f;
                float offsetZ = 0f;

                //X gives your distance east of the origin, and Z gives the distance south
                Direction facing = tower.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
                switch(facing)
                {
                    case NORTH:
                        offsetZ = -tower.towerProperties.getReach() - 1;
                        break;

                    case SOUTH:
                        offsetZ = tower.towerProperties.getReach() + 1;
                        break;

                    case WEST:
                        offsetX = -tower.towerProperties.getReach() - 1;
                        break;

                    case EAST:
                        offsetX = tower.towerProperties.getReach() + 1;
                        break;

                    default:
                }

                //traceXYZAxis(bufferConsumer, matrix, targetedPos, Direction.NORTH, 0, 0, -1, 0, 255, 0);
                //traceXYZAxis(bufferConsumer, matrix, targetedPos, Direction.SOUTH, 0, 0, 2, 0, 255, 0);
                //traceXYZAxis(bufferConsumer, matrix, targetedPos, Direction.EAST, 2, 0, 0, 0, 255, 0);
                //traceXYZAxis(bufferConsumer, matrix, targetedPos, Direction.WEST, -1, 0, 0, 0, 255, 0);

                traceBlockOutline(bufferConsumer, matrix, new BlockPos(bb.getCenter()), facing, 0, 0, 0, 0, 255, 0);
                traceAABBOutline(bufferConsumer, matrix, bb, facing, camera, 255, 255, 255);
                traceAABBCross(bufferConsumer, matrix, bb, facing, 0, 0, 0, 255, 0, 0);
            }
        }



        //End
        bufferSource.endBatch(RenderType.lines());
        matrix.popPose();

    }

    private static void traceXYZAxis(VertexConsumer bufferConsumer, PoseStack matrix, BlockPos origin, Direction facing, float offsetX, float offsetY, float offsetZ, int r, int g, int b)
    {
        matrix.pushPose();
        matrix.translate(origin.getX(), origin.getY(), origin.getZ());

        Matrix4f pose = matrix.last().pose();

        float coefX = 0f;
        float coefZ = 0f;

        //X gives your distance east of the origin, and Z gives the distance south
        switch(facing)
        {
            case NORTH:
                coefX = 1f;
                coefZ = -1f;
                break;

            case SOUTH:
                coefX = 1f;
                coefZ = 1f;
                break;

            case EAST:
                coefX = 1f;
                coefZ = 1f;
                break;

            case WEST:
                coefX = -1f;
                coefZ = 1f;
                break;
        }


        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        matrix.popPose();
    }

    private static void traceBlockOutline(VertexConsumer bufferConsumer, PoseStack matrix, BlockPos origin, Direction facing, float offsetX, float offsetY, float offsetZ, int r, int g, int b)
    {
        matrix.pushPose();
        matrix.translate(origin.getX(), origin.getY(), origin.getZ());

        float coefX = 0f;
        float coefZ = 0f;

        //X gives your distance east of the origin, and Z gives the distance south
        switch(facing)
        {
            case NORTH:
                coefX = 1f;
                coefZ = 1f;
                break;

            case SOUTH:
                coefX = 1f;
                coefZ = 1f;
                break;

            case EAST:
                coefX = 1f;
                coefZ = 1f;
                break;

            case WEST:
                coefX = 1f;
                coefZ = 1f;
                break;
        }

        Matrix4f pose = matrix.last().pose();
        //Face 1
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f, offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+1f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f, offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+0f, offsetY+1f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        //Face 2
        bufferConsumer.vertex(pose, offsetX+0f,         offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f,         offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        //Face 3
        bufferConsumer.vertex(pose, offsetX+0f,         offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+0f,         offsetY+1f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+1f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+1f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        //Face 4
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+0f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+1f, offsetZ+0f).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, offsetX+(1f*coefX), offsetY+1f, offsetZ+(1f*coefZ)).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        matrix.popPose();
    }

    private static void traceAABBOutline(VertexConsumer bufferConsumer, PoseStack matrix, AABB box, Direction facing, Vec3 camera, int r, int g, int b)
    {
        var center = box.getCenter();

        matrix.pushPose();
        matrix.translate(center.x, center.y, center.z);
        Matrix4f pose = matrix.last().pose();


        float xRadius = (float)((box.getXsize() - 1f)/2f)+0.5f;
        float yRadius = (float)((box.getYsize() - 1f)/2f)+0.5f;
        float zRadius = (float)((box.getZsize() - 1f)/2f)+0.5f;


        float coefX = 0f;
        float coefZ = 0f;

        //X gives your distance east of the origin, and Z gives the distance south
        switch(facing)
        {
            case NORTH:
                coefX = -1f;
                coefZ = 1f;
                break;

            case SOUTH:
                coefX = -1f;
                coefZ = -1f;
                break;

            case EAST:
                coefX = -1f;
                coefZ = -1f;
                break;

            case WEST:
                coefX = 1f;
                coefZ = -1f;
                break;
        }

        //Face 1
        //System.out.println(String.format("(%f, %f, %f) to (%f, %f, %f)", coefX*xRadius, -yRadius, coefZ*zRadius, (-coefX*xRadius)+(coefX*1f), -yRadius, coefZ*zRadius));
        bufferConsumer.vertex(pose, coefX*xRadius,  -yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, -yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, coefX*xRadius, -yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, coefX*xRadius, yRadius,  coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, coefX*xRadius,   yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius,  yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, -coefX*xRadius, -yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, yRadius,  coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        //Face 2
        bufferConsumer.vertex(pose, coefX*xRadius, -yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, coefX*xRadius, -yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, coefX*xRadius, yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, coefX*xRadius, yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, coefX*xRadius, -yRadius,   -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, coefX*xRadius, yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        //Face 3
        bufferConsumer.vertex(pose, -coefX*xRadius, -yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, -yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, -coefX*xRadius, yRadius, coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        //Face 4
        bufferConsumer.vertex(pose, coefX*xRadius,  -yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, -yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, coefX*xRadius,  yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        bufferConsumer.vertex(pose, -coefX*xRadius, -yRadius,   -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();
        bufferConsumer.vertex(pose, -coefX*xRadius, yRadius, -coefZ*zRadius).color(r, g, b, 255).normal(0, 0, 0).endVertex();

        matrix.popPose();
    }

    private static void traceAABBCross(VertexConsumer bufferConsumer, PoseStack matrix, AABB box, Direction facing, float offsetX, float offsetY, float offsetZ, int r, int g, int b)
    {
        var center = box.getCenter();

        //Size 1
        float xRadius = (float)((box.getXsize() - 1)/2);
        float yRadius = (float)((box.getYsize() - 1)/2);
        float zRadius = (float)((box.getZsize() - 1)/2);

        for (float i = -xRadius; i < xRadius+1; i++)
        {
            traceBlockOutline(bufferConsumer, matrix, new BlockPos(center), facing,offsetX+i, offsetY, offsetZ, r, g, b);
        }

        for (float i = -yRadius; i < yRadius+1; i++)
        {
            traceBlockOutline(bufferConsumer, matrix, new BlockPos(center), facing, offsetX, offsetY+i, offsetZ, r, g, b);
        }

        for (float i = -zRadius; i < zRadius+1; i++)
        {
            traceBlockOutline(bufferConsumer, matrix, new BlockPos(center), facing, offsetX, offsetY, offsetZ+i, r, g, b);
        }

    }
}
