package net.smileycorp.dynaores.common;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.brackets.BracketHandlerItem;
import crafttweaker.mc1120.events.ScriptRunEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CraftTweakerIntegration {
    
    private static boolean isRunning;
    
    @SubscribeEvent
    public void scriptStart(ScriptRunEvent.Pre event) {
        isRunning = true;
        CraftTweakerAPI.ENABLE_SEARCH_TREE_RECALCULATION = true;
    }
    
    @SubscribeEvent
    public void scriptStart(ScriptRunEvent.Post event) {
        isRunning = false;
        CraftTweakerAPI.ENABLE_SEARCH_TREE_RECALCULATION = false;
    }
    
    public static void refreshItems() {
        BracketHandlerItem.rebuildItemRegistry();
    }
    
    public static boolean isRunning() {
        return isRunning;
    }
    
}
