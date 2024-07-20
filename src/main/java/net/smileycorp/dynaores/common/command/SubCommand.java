package net.smileycorp.dynaores.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public abstract class SubCommand {
    
    abstract void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] args) throws CommandException;
    
    public List<String> getTabCompletions(String[] args) {
        return Lists.newArrayList();
    }
    
}
