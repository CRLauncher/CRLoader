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

public class MyClassTransformer implements ClassFileTransformer {
    private final Map<String, Patch> patches;

    public MyClassTransformer() {
        this.patches = new HashMap<>();

        this.addPatch(new SaveLocationPatch(System.getProperty("crloader.saveDirPath")));
        this.addPatch(new Lwjgl3LauncherPatch(System.getProperty("crloader.windowTitle")));
        this.addPatch(new AccountOfflinePatch(System.getProperty("crloader.offlineUsername")));

        if (Boolean.parseBoolean(System.getProperty("crloader.appendUsername"))) {
            this.addPatch(new AppendUsernamePatch());
        }

        Desc.useContextClassLoader = true;
    }

    private void addPatch(Patch patch) {
        this.patches.put(patch.getTarget(), patch);
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
