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
import me.theentropyshard.crloader.logging.Log;

public class AppendUsernamePatch extends Patch {
    private static final ClassName CHAT_SENDER = new ClassName("finalforeach", "cosmicreach", "networking", "client", "ChatSender");

    private final String username;

    public AppendUsernamePatch() {
        super("Append Username Patch", AppendUsernamePatch.CHAT_SENDER);

        this.username = System.getProperty("crloader.offlineUsername");
    }

    @Override
    public boolean isActive() {
        return Boolean.getBoolean("crloader.appendUsername");
    }

    @Override
    public byte[] perform(CtClass ctClass) throws Exception {
        String username = this.username;

        if (username == null) {
            Log.log("[WARN: " + this.getName() +
                " is enabled, but username was not passed via crloader.offlineUsername. Defaulting to Player");

            username = "Player";
        }

        CtMethod method = ctClass.getDeclaredMethod("sendMessageOrCommand");
        method.insertBefore("{ if ($5 != null && $5.length() != 0 && $5.charAt(0) != '/' " +
            " && $4 != null" +
            ") { " +
            " $5 = \"[\" + \"" + username + "\" + \"]\" + \" \" + $5;" +
            " } }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
