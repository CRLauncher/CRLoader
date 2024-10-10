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

import javassist.CtClass;
import javassist.CtMethod;
import me.theentropyshard.crloader.ClassName;

/**
 * This patch allows to set custom username for offline account
 */
public class AccountOfflinePatch extends Patch {
    private static final ClassName ACCOUNT_OFFLINE = new ClassName("finalforeach", "cosmicreach", "accounts", "AccountOffline");

    private final String username;

    public AccountOfflinePatch() {
        super("Offline Account Patch", AccountOfflinePatch.ACCOUNT_OFFLINE);

        this.username = System.getProperty("crloader.offlineUsername");
    }

    @Override
    public boolean isActive() {
        return this.username != null && !this.username.trim().isEmpty();
    }

    @Override
    public byte[] perform(CtClass ctClass) throws Exception {
        CtMethod method = ctClass.getDeclaredMethod("getDisplayName");
        method.setBody("{ return \"" + this.username + "\"; }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
