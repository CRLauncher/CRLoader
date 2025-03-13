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

package me.theentropyshard.crloader.patch;

import javassist.CtClass;
import javassist.CtMethod;
import me.theentropyshard.crloader.ClassName;

public class JoinWorldPatch extends Patch {
    private static final ClassName CLIENT_SINGLETONS = new ClassName("finalforeach", "cosmicreach", "ClientSingletons");

    private final String worldName;

    public JoinWorldPatch() {
        super("Join World Patch", JoinWorldPatch.CLIENT_SINGLETONS);

        this.worldName = System.getProperty("crloader.joinWorldName");
    }

    @Override
    public boolean isActive() {
        return this.worldName != null && !this.worldName.trim().isEmpty();
    }

    @Override
    public byte[] perform(CtClass clazz) throws Exception {
        CtMethod method = clazz.getDeclaredMethod("create");

        method.insertAfter("{ finalforeach.cosmicreach.rendering.GameTexture.load(\"base:textures/entities/sun.png\"); }");
        method.insertAfter("{ finalforeach.cosmicreach.rendering.GameTexture.load(\"base:textures/entities/moon.png\"); }");

        method.insertAfter("{ finalforeach.cosmicreach.gamestates.GameState.switchToGameState(new finalforeach.cosmicreach.gamestates.LoadingGame()); }");
        method.insertAfter("{ finalforeach.cosmicreach.gamestates.GameState.IN_GAME.loadWorld(\"" + this.worldName + "\"); }");

        byte[] bytecode = clazz.toBytecode();

        clazz.detach();

        return bytecode;
    }
}
