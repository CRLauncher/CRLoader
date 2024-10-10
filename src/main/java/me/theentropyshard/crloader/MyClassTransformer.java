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
import me.theentropyshard.crloader.patch.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MyClassTransformer implements ClassFileTransformer {
    private final Map<String, Patch> patches;

    public MyClassTransformer() {
        this.patches = new HashMap<>();

        this.addPatchFromProperty("crloader.saveDirPath", SaveLocationPatch::new);
        this.addPatchFromProperty("crloader.windowTitle", Lwjgl3LauncherPatch::new);
        this.addPatchFromProperty("crloader.offlineUsername", AccountOfflinePatch::new);
        this.addPatchFromProperty("crloader.appendUsername", v -> Boolean.parseBoolean(v) ? new AppendUsernamePatch() : null);

        Desc.useContextClassLoader = true;
    }

    private void addPatch(Patch patch) {
        this.patches.put(patch.getTarget(), patch);
    }

    private void addPatchFromProperty(String propertyName, Function<String, Patch> instantiate) {
        String propertyValue = System.getProperty(propertyName);

        if (propertyValue == null) {
            return;
        }

        Patch patch = instantiate.apply(propertyValue);

        if (patch == null) {
            return;
        }

        this.addPatch(patch);
    }

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] bytecode) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(classLoader));

            if (this.patches.containsKey(className)) {
                byte[] bytes = this.patches.get(className).perform(classPool);

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
