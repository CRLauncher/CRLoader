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

public class AppendUsernamePatch implements Patch {
    private static final ClassName CHAT_SENDER = new ClassName("finalforeach", "cosmicreach", "networking",
        "client", "ChatSender");

    @Override
    public String getTarget() {
        return AppendUsernamePatch.CHAT_SENDER.getJvmName();
    }

    @Override
    public byte[] perform(ClassPool classPool) throws Exception {
        CtClass ctClass = classPool.get(AppendUsernamePatch.CHAT_SENDER.getJavaName());
        CtMethod method = ctClass.getDeclaredMethod("sendMessageOrCommand");
        method.insertBefore("{ if ($5 != null && $5.length() != 0 && $5.charAt(0) != '/' " +
            " && $4 != null" +
            ") { " +
            " $5 = \"[\" + $4.getDisplayName() + \"]\" + \" \" + $5;" +
            " } }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
