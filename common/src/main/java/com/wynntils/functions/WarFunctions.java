/*
 * Copyright © Wynntils 2023-2024.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.functions;

import com.wynntils.core.components.Models;
import com.wynntils.core.consumers.functions.Function;
import com.wynntils.core.consumers.functions.arguments.FunctionArguments;

public class WarFunctions {
    public static class AuraTimerFunction extends Function<Double> {
        @Override
        public Double getValue(FunctionArguments arguments) {
            return Models.GuildWarTower.getRemainingTimeUntilAura() / 1000d;
        }
    }

    public static class VolleyTimerFunction extends Function<Double> {
        @Override
        public Double getValue(FunctionArguments arguments) {
            return Models.GuildWarTower.getRemainingTimeUntilVolley() / 1000d;
        }
    }
}
