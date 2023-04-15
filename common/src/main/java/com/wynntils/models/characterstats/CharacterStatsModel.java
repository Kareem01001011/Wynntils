/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.models.characterstats;

import com.wynntils.core.components.Handlers;
import com.wynntils.core.components.Model;
import com.wynntils.core.components.Models;
import com.wynntils.models.characterstats.actionbar.CoordinatesSegment;
import com.wynntils.models.characterstats.actionbar.HealthSegment;
import com.wynntils.models.characterstats.actionbar.ManaSegment;
import com.wynntils.models.characterstats.actionbar.PowderSpecialSegment;
import com.wynntils.models.characterstats.actionbar.SprintSegment;
import com.wynntils.models.elements.type.Powder;
import com.wynntils.utils.MathUtils;
import com.wynntils.utils.mc.McUtils;
import com.wynntils.utils.type.CappedValue;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CharacterStatsModel extends Model {
    private final CoordinatesSegment coordinatesSegment = new CoordinatesSegment(this::centerSegmentCleared);
    private final HealthSegment healthSegment = new HealthSegment();
    private final ManaSegment manaSegment = new ManaSegment();
    private final PowderSpecialSegment powderSpecialSegment = new PowderSpecialSegment();
    private final SprintSegment sprintSegment = new SprintSegment();

    public CharacterStatsModel(CombatXpModel combatXpModel) {
        super(List.of(combatXpModel));

        Handlers.ActionBar.registerSegment(coordinatesSegment);
        Handlers.ActionBar.registerSegment(healthSegment);
        Handlers.ActionBar.registerSegment(manaSegment);
        Handlers.ActionBar.registerSegment(powderSpecialSegment);
        Handlers.ActionBar.registerSegment(sprintSegment);
    }

    public CappedValue getHealth() {
        return healthSegment.getHealth();
    }

    public CappedValue getMana() {
        return manaSegment.getMana();
    }

    public CappedValue getSprint() {
        return sprintSegment.getSprint();
    }

    public float getPowderSpecialCharge() {
        return powderSpecialSegment.getPowderSpecialCharge();
    }

    public Powder getPowderSpecialType() {
        return powderSpecialSegment.getPowderSpecialType();
    }

    public void hideHealth(boolean shouldHide) {
        healthSegment.setHidden(shouldHide);
    }

    public void hideMana(boolean shouldHide) {
        manaSegment.setHidden(shouldHide);
    }

    /**
     * Return the maximum number of soul points the character can currently have
     */
    private int getMaxSoulPoints() {
        // FIXME: If player is veteran, we should always return 15
        int maxIfNotVeteran =
                10 + MathUtils.clamp(Models.CombatXp.getCombatLevel().current() / 15, 0, 5);
        if (getCurrentSoulPoints() > maxIfNotVeteran) {
            return 15;
        }
        return maxIfNotVeteran;
    }

    /**
     * Return the current number of soul points of the character, or -1 if unable to determine
     */
    private int getCurrentSoulPoints() {
        ItemStack soulPoints = McUtils.inventory().getItem(8);
        if (soulPoints.getItem() != Items.NETHER_STAR) {
            return -1;
        }

        return soulPoints.getCount();
    }

    public CappedValue getSoulPoints() {
        // FIXME: We should be able to cache this
        return new CappedValue(getCurrentSoulPoints(), getMaxSoulPoints());
    }

    /**
     * Return the time in game ticks (1/20th of a second, 50ms) until the next soul point is given
     *
     * Also check that {@code {@link #getMaxSoulPoints()} >= {@link #getSoulPoints()}},
     * in which case soul points are already full
     */
    public int getTicksToNextSoulPoint() {
        if (McUtils.mc().level == null) return -1;
        return 24000 - (int) (McUtils.mc().level.getDayTime() % 24000);
    }

    private void centerSegmentCleared() {
        powderSpecialSegment.replaced();
    }
}
