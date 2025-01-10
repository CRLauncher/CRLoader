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
 * This patch allows to set custom title for game window
 */
public class WindowSizePatch extends Patch {
    private static final ClassName LWJGL3_LAUNCHER = new ClassName("finalforeach", "cosmicreach", "lwjgl3", "Lwjgl3Launcher");

    private final int windowWidth;
    private final int windowHeight;
    private final boolean fullscreen;
    private final boolean maximized;

    public WindowSizePatch() {
        super("Window Size Patch", WindowSizePatch.LWJGL3_LAUNCHER);

        this.windowWidth = Integer.parseInt(System.getProperty("crloader.windowWidth", "-1"));
        this.windowHeight = Integer.parseInt(System.getProperty("crloader.windowHeight", "-1"));
        this.fullscreen = Boolean.getBoolean("crloader.windowFullscreen");
        this.maximized = Boolean.getBoolean("crloader.windowMaximized");
    }

    @Override
    public boolean isActive() {
        return (this.windowWidth > 0 && this.windowHeight > 0) || (this.fullscreen && !this.maximized) || (!this.fullscreen && this.maximized);
    }

    @Override
    public byte[] perform(CtClass ctClass) throws Exception {
        CtMethod method = ctClass.getDeclaredMethod("getDefaultConfiguration");

        if (this.fullscreen) {
            method.insertAfter("{ $_.setFullscreenMode(com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.getDisplayMode()); }");
        } else if (this.maximized) {
            method.insertAfter("{ $_.setMaximized(true); }");
        } else {
            method.insertAfter("{ $_.setWindowedMode(" + this.windowWidth + ", " + this.windowHeight + "); }");
        }

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
