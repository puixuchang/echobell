package com.echobell.echobell;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 右键摇铃：半径内的生物获得发光效果，同时播放一声钟响。
 * 服务端处理逻辑，客户端只负责动画/音效的常规同步。
 */
public class EchoBellItem extends Item {

    /** 作用半径（格） */
    public static final double RADIUS = 16.0D;
    /** 发光持续时间（tick），200 = 10 秒 */
    public static final int GLOW_TICKS = 200;
    /** 冷却时间（tick），100 = 5 秒 */
    public static final int COOLDOWN_TICKS = 100;

    public EchoBellItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

        if (!level.isClientSide) {
            BlockPos pos = player.blockPosition();
            level.playSound(null, pos, SoundEvents.BELL_BLOCK.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(RADIUS),
                    entity -> entity != player && entity.isAlive())) {
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_TICKS, 0, false, false), player);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
