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

import javassist.ClassPool;
import javassist.LoaderClassPath;
import javassist.runtime.Desc;
import me.theentropyshard.crloader.logging.Log;
import me.theentropyshard.crloader.patch.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClassTransformer implements ClassFileTransformer {
    private final Map<String, List<Patch>> patches;

    public MyClassTransformer() {
        this.patches = new HashMap<>();

        this.addPatches(
            new SaveLocationPatch(),
            new WindowTitlePatch(),
            new AccountOfflinePatch(),
            new AppendUsernamePatch(),
            new WindowSizePatch(),
            new JoinServerPatch()
        );

        this.printPatches();

        Desc.useContextClassLoader = true;
    }

    private void addPatches(Patch... patches) {
        for (Patch patch : patches) {
            List<Patch> patchList = this.patches.get(patch.getTarget());

            if (patchList == null) {
                List<Patch> newPatchList = new ArrayList<>();
                newPatchList.add(patch);
                this.patches.put(patch.getTarget(), newPatchList);
            } else {
                patchList.add(patch);
            }
        }
    }

    private void printPatches() {
        for (List<Patch> patches : this.patches.values()) {
            for (Patch patch : patches) {
                Log.log(patch.getName() + ": " + (patch.isActive() ? "Enabled" : "Disabled"));
            }
        }
    }

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] bytecode) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(classLoader));

            List<Patch> patches = this.patches.get(className);

            if (patches == null) {
                return bytecode;
            }

            for (Patch patch : patches) {
                if (patch != null && patch.isActive()) {
                    byte[] bytes = patch.perform(classPool);

                    if (bytes == null) {
                        return bytecode;
                    } else {
                        return bytes;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytecode;
    }
}
