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

public class ClassName {
    private static final String JAVA_DELIMITER = ".";
    private static final String JVM_DELIMITER = "/";

    private final String javaName;
    private final String jvmName;

    public ClassName(String... parts) {
        if (parts.length == 0) {
            throw new IllegalArgumentException("Class name parts must not be empty!");
        }

        if (parts.length == 1) {
            this.javaName = parts[0];
            this.jvmName = parts[0];

            return;
        }

        this.javaName = String.join(ClassName.JAVA_DELIMITER, parts);
        this.jvmName = String.join(ClassName.JVM_DELIMITER, parts);
    }

    public String getJavaName() {
        return this.javaName;
    }

    public String getJvmName() {
        return this.jvmName;
    }
}
