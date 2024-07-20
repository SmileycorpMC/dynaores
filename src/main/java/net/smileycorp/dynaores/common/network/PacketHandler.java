package net.smileycorp.dynaores.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.dynaores.common.Constants;

public class PacketHandler {
    
    public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
    
    public static void init() {
        int id = 0;
        NETWORK_INSTANCE.registerMessage(ClipboardMessage::execute, ClipboardMessage.class, id++, Side.CLIENT);
        NETWORK_INSTANCE.registerMessage(CommandExecuteMessage::execute, CommandExecuteMessage.class, id++, Side.CLIENT);
    }
    
}
