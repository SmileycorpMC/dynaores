package net.smileycorp.dynaores.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.dynaores.common.DynaOresLogger;
import net.smileycorp.dynaores.common.data.OreHandler;
import net.smileycorp.dynaores.common.network.CommandExecuteMessage;
import net.smileycorp.dynaores.common.network.PacketHandler;

import java.util.List;

public class SubCommandGetLang extends SubCommand {
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new SyntaxErrorException("Missing ore argument");
        if (sender.getCommandSenderEntity() instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new CommandExecuteMessage((byte)1, args[0]),
                (EntityPlayerMP) sender.getCommandSenderEntity());
        else throw new PlayerNotFoundException("Command sender is not a player");
    }
    
    @Override
    public List<String> getTabCompletions(String[] args) {
        return args.length < 1 ? Lists.newArrayList(OreHandler.INSTANCE.getOreNames()) : Lists.newArrayList();
    }
    
}
