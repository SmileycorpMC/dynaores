package net.smileycorp.dynaores.common.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.smileycorp.dynaores.common.network.ClipboardMessage;
import net.smileycorp.dynaores.common.network.PacketHandler;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class DynaOresCommand extends CommandBase {
    
    private Map<String, SubCommand> sub_commands = Maps.newHashMap();
    
    @Override
    public String getName() {
        return "dynaores";
    }
    
    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "dynaores <subcommand>";
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length <= 1) return Lists.newArrayList(sub_commands.keySet());
        if (sub_commands.containsKey(args[0])) sub_commands.get(args[0]).getTabCompletions(ArrayUtils.remove(args, 0));
        return Lists.newArrayList();
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new CommandNotFoundException();
        if (args[0].equals("copy") && args.length > 1 && sender.getCommandSenderEntity() instanceof EntityPlayerMP) {
            PacketHandler.NETWORK_INSTANCE.sendTo(new ClipboardMessage(String.join(" ", ArrayUtils.remove(args, 0))),
                    (EntityPlayerMP) sender.getCommandSenderEntity());
            return;
        }
        if (sub_commands.containsKey(args[0])) {
            sub_commands.get(args[0]).execute(server, sender, ArrayUtils.remove(args, 0));
            return;
        }
        throw new CommandNotFoundException();
    }
    
    public void register(String name, SubCommand subcommand) {
        sub_commands.put(name, subcommand);
    }
    
}
