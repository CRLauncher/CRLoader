/*
 * CRLoader - https://github.com/CRLauncher/CRLoader
 * Copyright (C) 2024 CRLauncher
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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import me.theentropyshard.crloader.ClassName;

/**
 * This patch allows to change save location for the game
 */
public class SaveLocationPatch implements Patch {
    private static final ClassName SAVE_LOCATION = new ClassName("finalforeach", "cosmicreach", "io", "SaveLocation");

    private final String saveDirPath;

    public SaveLocationPatch(String saveDirPath) {
        this.saveDirPath = saveDirPath;
    }

    @Override
    public String getTarget() {
        return SAVE_LOCATION.getJvmName();
    }

    @Override
    public byte[] perform(ClassPool classPool) throws Exception {
        if (this.saveDirPath == null) {
            throw new Exception("System property crloader.saveDirPath must be set!");
        }

        CtClass ctClass = classPool.get(SaveLocationPatch.SAVE_LOCATION.getJavaName());
        CtMethod method = ctClass.getDeclaredMethod("getSaveFolderLocation");
        method.setBody("{ return \"" + this.saveDirPath + "\"; }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
