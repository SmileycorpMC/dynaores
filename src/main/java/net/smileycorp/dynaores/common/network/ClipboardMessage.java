package net.smileycorp.dynaores.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.dynaores.client.ClientCommandHandler;

public class ClipboardMessage implements IMessage {
    
    private String message = "";
    
    public ClipboardMessage() {}
    
    public ClipboardMessage(String message) {
        this.message = message;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
    }
    
    public IMessage execute(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> {
            ClientCommandHandler.copyToClipboard(message);
        });
        return null;
    }
    
}
