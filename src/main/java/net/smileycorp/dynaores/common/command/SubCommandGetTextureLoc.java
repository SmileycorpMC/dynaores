package net.smileycorp.dynaores.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.dynaores.common.data.OreHandler;
import net.smileycorp.dynaores.common.network.CommandExecuteMessage;
import net.smileycorp.dynaores.common.network.PacketHandler;

import java.util.List;
import java.util.Locale;

public class SubCommandGetTextureLoc extends SubCommand {
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new SyntaxErrorException("Missing ore argument");
        if (OreHandler.INSTANCE.getEntry(args[0]) == null)  throw new SyntaxErrorException(args[0] + " is not a valid ore entry");
        boolean block = false;
        if (args.length > 1 && args[1].toLowerCase(Locale.US).equals("true")) block = true;
        if (sender.getCommandSenderEntity() instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new CommandExecuteMessage((byte) (block ? 3 : 2), args[0]),
                (EntityPlayerMP) sender.getCommandSenderEntity());
        else throw new PlayerNotFoundException("Command sender is not a player");
    }
    
    @Override
    public List<String> getTabCompletions(String[] args) {
        return args.length < 1 ? Lists.newArrayList(OreHandler.INSTANCE.getOreNames()) : args.length < 2 ? Lists.newArrayList("TRUE", "FALSE") :  Lists.newArrayList();
    }
    
}
