package dev.creoii.greatbigworld.swordsandshields.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import dev.creoii.greatbigworld.swordsandshields.screen.EnchantmentScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;

public class EnchantmentScreen extends HandledScreen<EnchantmentScreenHandler> {
    private static final Identifier LEVEL_TEXTURE = Identifier.of(GreatBigWorld.NAMESPACE, "experience_orb");
    private static final Identifier LEVEL_DISABLED_TEXTURE = Identifier.of(GreatBigWorld.NAMESPACE, "experience_orb_disabled");
    private static final Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE = Identifier.of("container/enchanting_table/enchantment_slot_disabled");
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE = Identifier.of("container/enchanting_table/enchantment_slot_highlighted");
    private static final Identifier ENCHANTMENT_SLOT_TEXTURE = Identifier.of("container/enchanting_table/enchantment_slot");
    private static final Identifier TEXTURE = Identifier.of("textures/gui/container/enchanting_table.png");
    private static final Identifier BOOK_TEXTURE = Identifier.of("textures/entity/enchanting_table_book.png");
    private final Random random = Random.create();
    private BookModel BOOK_MODEL;
    public int ticks;
    public float nextPageAngle;
    public float pageAngle;
    public float approximatePageAngle;
    public float pageRotationSpeed;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    private ItemStack stack;
    private int scrollOffset;

    public EnchantmentScreen(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.stack = ItemStack.EMPTY;
    }

    protected void init() {
        super.init();
        this.BOOK_MODEL = new BookModel(this.client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        this.doTick();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        for (int k = 0; k < handler.getEnchantmentsCount(); ++k) {
            double d = mouseX - (double) (i + 60);
            double e = mouseY - (double) (j + 14 + 19 * k);
            if (d >= 0.0 && e >= 0.0 && d < 108.0 && e < 19.0 && this.handler.onButtonClick(this.client.player, k)) {
                this.client.interactionManager.clickButton(this.handler.syncId, k);
                return true;
            }
        }

        double d = mouseX - (double) (i + 60);
        double e = mouseY - (double) (j + 14);
        if (EnchantmentHelper.hasEnchantments(stack) && d >= 0.0 && e >= 0.0 && d < 108.0 && e < 19.0 && this.handler.onButtonClick(this.client.player, 0)) {
            this.client.interactionManager.clickButton(this.handler.syncId, 0);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawBook(context, i, j, delta);
        int k = this.handler.getLapisCount();

        int m = i + 60;
        int n = m + 20;
        if (EnchantmentHelper.hasEnchantments(stack)) {
            int r = mouseX - (i + 60);
            int s = mouseY - (j + 14);
            RenderSystem.enableBlend();
            if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                context.drawGuiTexture(ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, j + 14 + scrollOffset, 108, 19);
            } else {
                context.drawGuiTexture(ENCHANTMENT_SLOT_TEXTURE, m, j + 14 + scrollOffset, 108, 19);
            }
            context.drawTextWithShadow(textRenderer, "Learn Enchantments", m + 2, j + 19 + scrollOffset, 8453920);
            RenderSystem.disableBlend();
        } else if (handler.getEnchantmentsCount() > 0) {
            System.out.println("drawBackground 1: " + handler.getEnchantmentsCount());
            for (int l = 0; l < handler.getEnchantmentsCount(); ++l) {
                int y1 = j + 14 + 19 * l + scrollOffset;
                System.out.println(l + " y: " + y1);
                if (y1 > 89)
                    continue;
                int y2 = y1 + 2;
                int y3 = y1 + 3;
                int y4 = y1 + 5;

                int o = this.handler.enchantmentPower[l];
                if (o == 0) {
                    RenderSystem.enableBlend();
                    context.drawGuiTexture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, y1, 108, 19);
                    RenderSystem.disableBlend();
                } else {
                    String string = "" + o;
                    int p = 86 - this.textRenderer.getWidth(string);
                    EnchantingPhrases.getInstance().setSeed(this.handler.getSeed());
                    StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, p);
                    int q = 6839882;
                    if ((k < l + 1 || this.client.player.experienceLevel < o) && !this.client.player.getAbilities().creativeMode) {
                        RenderSystem.enableBlend();
                        context.drawGuiTexture(ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, y1, 108, 19);
                        context.drawGuiTexture(LEVEL_DISABLED_TEXTURE, m + 6, y3 - 1, 16, 16);
                        context.drawTextWithShadow(textRenderer, String.valueOf(l + 1), m + 2, y4, 4226832);
                        RenderSystem.disableBlend();
                        context.drawTextWrapped(this.textRenderer, stringVisitable, n, y2, p, (q & 16711422) >> 1);
                        q = 4226832;
                    } else {
                        int r = mouseX - (i + 60);
                        int s = mouseY - y1;
                        RenderSystem.enableBlend();
                        if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                            context.drawGuiTexture(ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, y1, 108, 19);
                            q = 16777088;
                        } else {
                            context.drawGuiTexture(ENCHANTMENT_SLOT_TEXTURE, m, y1, 108, 19);
                        }

                        context.drawGuiTexture(LEVEL_TEXTURE, m + 6, y3 - 1, 16, 16);
                        context.drawTextWithShadow(textRenderer, String.valueOf(l + 1), m + 2, y4, 8453920);
                        RenderSystem.disableBlend();
                        context.drawTextWrapped(this.textRenderer, stringVisitable, n, y2, p, q);
                        q = 8453920;
                    }

                    context.drawTextWithShadow(this.textRenderer, string, n + 86 - this.textRenderer.getWidth(string), y2 + 7, q);
                }
            }
        }
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    private void drawBook(DrawContext context, int x, int y, float delta) {
        float f = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
        float g = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle);
        DiffuseLighting.method_34742();
        context.getMatrices().push();
        context.getMatrices().translate((float)x + 33.0F, (float)y + 31.0F, 100.0F);
        float h = 40.0F;
        context.getMatrices().scale(-40.0F, 40.0F, 40.0F);
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25.0F));
        context.getMatrices().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
        float i = -(1.0F - f) * 90.0F - 90.0F;
        context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        float j = MathHelper.clamp(MathHelper.fractionalPart(g + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
        float k = MathHelper.clamp(MathHelper.fractionalPart(g + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
        this.BOOK_MODEL.setPageAngles(0.0F, j, k, f);
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(this.BOOK_MODEL.getLayer(BOOK_TEXTURE));
        this.BOOK_MODEL.render(context.getMatrices(), vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
        context.draw();
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        boolean bl = this.client.player.getAbilities().creativeMode;
        int i = this.handler.getLapisCount();

        System.out.println("render 1: " + handler.getEnchantmentsCount());
        for (int j = 0; j < handler.getEnchantmentsCount(); ++j) {
            int k = this.handler.enchantmentPower[j];
            Optional<RegistryEntry.Reference<Enchantment>> optional = client.world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(handler.enchantmentId[j]); // null
            if (optional.isPresent()) {
                int l = this.handler.enchantmentLevel[j]; // -1
                int m = j + 1;
                if (this.isPointWithinBounds(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0 && l >= 0) {
                    List<Text> list = Lists.newArrayList();
                    list.add(Text.translatable("container.enchant.clue", Enchantment.getName(optional.get(), l)).formatted(Formatting.WHITE));
                    if (!bl) {
                        list.add(ScreenTexts.EMPTY);
                        if (this.client.player.experienceLevel < k) {
                            list.add(Text.translatable("container.enchant.level.requirement", this.handler.enchantmentPower[j]).formatted(Formatting.RED));
                        } else {
                            MutableText mutableText;
                            if (m == 1) {
                                mutableText = Text.translatable("container.enchant.lapis.one");
                            } else {
                                mutableText = Text.translatable("container.enchant.lapis.many", m);
                            }

                            list.add(mutableText.formatted(i >= m ? Formatting.GRAY : Formatting.RED));
                            MutableText mutableText2;
                            if (m == 1) {
                                mutableText2 = Text.translatable("container.enchant.level.one");
                            } else {
                                mutableText2 = Text.translatable("container.enchant.level.many", m);
                            }

                            list.add(mutableText2.formatted(Formatting.GRAY));
                        }
                    }

                    context.drawTooltip(this.textRenderer, list, mouseX, mouseY);
                    break;
                }
            }
        }
    }

    public void doTick() {
        ItemStack itemStack = this.handler.getSlot(0).getStack();
        if (!ItemStack.areEqual(itemStack, this.stack)) {
            this.stack = itemStack;

            do {
                this.approximatePageAngle += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while(this.nextPageAngle <= this.approximatePageAngle + 1.0F && this.nextPageAngle >= this.approximatePageAngle - 1.0F);
        }

        ++this.ticks;
        this.pageAngle = this.nextPageAngle;
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        boolean bl = false;

        for (int i = 0; i < handler.getEnchantmentsCount(); ++i) {
            if (this.handler.enchantmentPower[i] != 0) {
                bl = true;
                break;
            }
        }

        if (bl) {
            this.nextPageTurningSpeed += 0.2F;
        } else this.nextPageTurningSpeed -= 0.2F;

        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        float f = (this.approximatePageAngle - this.nextPageAngle) * 0.4F;
        float g = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.pageRotationSpeed += (f - this.pageRotationSpeed) * 0.9F;
        this.nextPageAngle += this.pageRotationSpeed;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (handler.getEnchantmentsCount() > 3)
            scrollOffset = (int) Math.clamp(scrollOffset + verticalAmount, handler.getEnchantmentsCount() * -13, 0);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
