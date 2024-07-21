package net.smileycorp.dynaores.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.dynaores.client.ClientCommandHandler;

public class CommandExecuteMessage implements IMessage {
    
    private byte id;
    private String data = "";
    
    public CommandExecuteMessage() {}
    
    public CommandExecuteMessage(byte id) {
        this(id, "");
    }
    
    public CommandExecuteMessage(byte id, String data) {
        this.id = id;
        this.data = data;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readByte();
        data = ByteBufUtils.readUTF8String(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(id);
        ByteBufUtils.writeUTF8String(buf, data);
    }
    
    public IMessage execute(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> {
            switch (id) {
                case 0:
                    ClientCommandHandler.sendHeldItemColour();
                    break;
                case 1:
                    ClientCommandHandler.sendLangKey(data);
                    break;
                case 2:
                    ClientCommandHandler.sendTextureLocation(data, false);
                    break;
                case 3:
                    ClientCommandHandler.sendTextureLocation(data, true);
                    break;
            }
        });
        return null;
    }
    
}
