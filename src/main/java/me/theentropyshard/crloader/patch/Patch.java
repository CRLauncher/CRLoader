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

import javassist.ClassPool;
import javassist.CtClass;
import me.theentropyshard.crloader.ClassName;

public abstract class Patch {
    private final String name;
    private final ClassName className;

    public Patch(String name, ClassName className) {
        this.name = name;
        this.className = className;
    }

    public abstract boolean isActive();

    public byte[] perform(ClassPool classPool) throws Exception {
        return this.perform(classPool.get(this.className.getJavaName()));
    }

    public abstract byte[] perform(CtClass clazz) throws Exception;

    public String getName() {
        return this.name;
    }

    public String getTarget() {
        return this.className.getJvmName();
    }
}
