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
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.runtime.Desc;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class MyClassTransformer implements ClassFileTransformer {
    private final String saveDirPath;

    public MyClassTransformer(String saveDirPath) {
        Desc.useContextClassLoader = true;
        this.saveDirPath = saveDirPath;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        byte[] byteCode = classfileBuffer;

        if (!className.equals("finalforeach/cosmicreach/io/SaveLocation")) {
            return byteCode;
        }

        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader));

            CtClass ctClass = classPool.get("finalforeach.cosmicreach.io.SaveLocation");
            CtMethod method = ctClass.getDeclaredMethod("getSaveFolderLocation");
            method.setBody("{ return \"" + this.saveDirPath + "\"; }");

            byteCode = ctClass.toBytecode();

            ctClass.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteCode;
    }
}
