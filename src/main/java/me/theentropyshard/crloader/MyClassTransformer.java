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

package me.theentropyshard.crloader;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import javassist.runtime.Desc;
import me.theentropyshard.crloader.logging.Log;
import me.theentropyshard.crloader.patch.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class MyClassTransformer implements ClassFileTransformer {
    private final Map<String, Patch> patches;

    public MyClassTransformer() {
        this.patches = new HashMap<>();

        this.addPatch(new SaveLocationPatch());
        this.addPatch(new Lwjgl3LauncherPatch());
        this.addPatch(new AccountOfflinePatch());
        this.addPatch(new AppendUsernamePatch());

        this.printPatches();

        Desc.useContextClassLoader = true;
    }

    private void addPatch(Patch patch) {
        this.patches.put(patch.getTarget(), patch);
    }

    private void printPatches() {
        for (Patch patch : this.patches.values()) {
            Log.log(patch.getName() + ": " + (patch.isActive() ? "Enabled" : "Disabled"));
        }
    }

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] bytecode) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(classLoader));

            Patch patch = this.patches.get(className);

            if (patch != null && patch.isActive()) {
                byte[] bytes = patch.perform(classPool);

                if (bytes == null) {
                    return bytecode;
                } else {
                    return bytes;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytecode;
    }
}
