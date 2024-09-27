package me.theentropyshard.crloader.patch;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import me.theentropyshard.crloader.ClassName;

/**
 * This patch allows to set custom username for offline account
 */
public class AccountOfflinePatch implements Patch {
    private static final ClassName ACCOUNT_OFFLINE = new ClassName("finalforeach", "cosmicreach", "accounts", "AccountOffline");

    private final String username;

    public AccountOfflinePatch(String username) {
        this.username = username;
    }

    @Override
    public String getTarget() {
        return AccountOfflinePatch.ACCOUNT_OFFLINE.getJvmName();
    }

    @Override
    public byte[] perform(ClassPool classPool) throws Exception {
        if (this.username == null || this.username.trim().isEmpty()) {
            return null;
        }

        CtClass ctClass = classPool.get(AccountOfflinePatch.ACCOUNT_OFFLINE.getJavaName());
        CtMethod method = ctClass.getDeclaredMethod("getDisplayName");
        method.setBody("{ return \"" + this.username + "\"; }");

        byte[] bytecode = ctClass.toBytecode();

        ctClass.detach();

        return bytecode;
    }
}
