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

/**
 * This patch allows to change save location for the game
 */
public class SaveLocationPatch extends Patch {
    private static final ClassName SAVE_LOCATION = new ClassName("finalforeach", "cosmicreach", "io", "SaveLocation");
    private static final String SAVE_DIR_PATH = "crloader.saveDirPath";

    private final String saveDirPath;

    public SaveLocationPatch() {
        super("Save Location Patch", SaveLocationPatch.SAVE_LOCATION);

        this.saveDirPath = System.getProperty(SaveLocationPatch.SAVE_DIR_PATH);
    }

    @Override
    public boolean isActive() {
        return this.saveDirPath != null && !this.saveDirPath.trim().isEmpty();
    }

    @Override
    public byte[] perform(CtClass ctClass) throws Exception {
        CtMethod method = ctClass.getDeclaredMethod("getSaveFolderLocation");
        method.setBody("{ return \"" + this.saveDirPath + "\"; }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
