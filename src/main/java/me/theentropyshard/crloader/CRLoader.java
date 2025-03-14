/*
 * CRLoader - https://github.com/CRLauncher/CRLoader
 * Copyright (C) 2024-2025 CRLauncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.crloader;

import me.theentropyshard.crloader.logging.Log;

import java.lang.instrument.Instrumentation;

public class CRLoader {
    public static String VERSION = "0.1.7";

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        Log.log("Version: " + CRLoader.VERSION);

        instrumentation.addTransformer(new MyClassTransformer());
    }
}
