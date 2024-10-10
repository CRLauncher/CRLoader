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
 * This patch allows to set custom title for game window
 */
public class Lwjgl3LauncherPatch implements Patch {
    private static final ClassName LWJGL3_LAUNCHER = new ClassName("finalforeach", "cosmicreach", "lwjgl3", "Lwjgl3Launcher");

    private final String windowTitle;

    public Lwjgl3LauncherPatch(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    @Override
    public String getTarget() {
        return Lwjgl3LauncherPatch.LWJGL3_LAUNCHER.getJvmName();
    }

    @Override
    public byte[] perform(ClassPool classPool) throws Exception {
        CtClass ctClass = classPool.get(Lwjgl3LauncherPatch.LWJGL3_LAUNCHER.getJavaName());
        CtMethod method = ctClass.getDeclaredMethod("getDefaultConfiguration");
        method.insertAfter("{ $_.setTitle(\"" + this.windowTitle + "\"); }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
