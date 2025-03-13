package me.theentropyshard.crloader.patch;
import javassist.*;
import me.theentropyshard.crloader.ClassName;

public class JoinServerPatch extends Patch {
    private static final ClassName MAIN_MENU = new ClassName("finalforeach", "cosmicreach", "gamestates", "MainMenu");
    private final String serverAddress;
    public JoinWorldServerPatch() {
        super("Join Server Patch", JoinWorldServerPatch.MAIN_MENU);
        this.serverAddress= System.getProperty("crloader.joinServerAddress");
    }
    @Override
    public boolean isActive() {
        return this.serverAddress!= null && !this.serverAddress.trim().isEmpty();
    }
    @Override
    public byte[] perform(CtClass clazz) throws Exception {
        CtMethod method = clazz.getDeclaredMethod("create");
        String injectedCode = "{\n" +
                "\tfinalforeach.cosmicreach.gamestates.GameState.switchToGameState(" +
                "new finalforeach.cosmicreach.gamestates.ConnectingScreen(\"" + this.serverAddress+ "\"));\n" +
                "};\n";
        System.out.println(injectedCode);
        method.insertAfter(injectedCode);
        byte[] bytecode = clazz.toBytecode();
        clazz.detach();
        return bytecode;
    }
}
