package net.smileycorp.dynaores.common.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.dynaores.common.network.CommandExecuteMessage;
import net.smileycorp.dynaores.common.network.PacketHandler;

public class SubCommandClearCache extends SubCommand {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getCommandSenderEntity() instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new CommandExecuteMessage((byte)4),
                (EntityPlayerMP) sender.getCommandSenderEntity());
        else throw new PlayerNotFoundException("Command sender is not a player");
    }

}
