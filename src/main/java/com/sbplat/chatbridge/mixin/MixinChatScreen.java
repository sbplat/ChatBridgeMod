package com.sbplat.chatbridge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sbplat.chatbridge.commands.CommandSendRawMessage;
import com.sbplat.chatbridge.events.*;

import net.minecraft.client.gui.screens.ChatScreen;

@Mixin(ChatScreen.class)
public class MixinChatScreen {
    @Inject(at = @At("HEAD"), method = "handleChatInput(Ljava/lang/String;Z)Z", cancellable = true)
    private void onHandleChatInput(String string, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        ClientChatEvent event = new ClientChatEvent(string);
        ClientChatEventHandler.onClientChat(event);
        if (event.isCanceled()) {
            cir.setReturnValue(true); // Return true to close the chat GUI.
        }
    }

    @ModifyVariable(
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/client/gui/components/ChatComponent;addRecentChat(Ljava/lang/String;)V",
                            shift = At.Shift.AFTER),
            method = "handleChatInput(Ljava/lang/String;Z)Z",
            ordinal = 0)
    private String modifyHandleChatInput(String string) {
        if (string.startsWith("/")) {
            String commandName = string.substring(1).split(" ")[0].toLowerCase();
            // Check if the command is the raw message command.
            if (commandName.equals(CommandSendRawMessage.getCommandName())
                    || CommandSendRawMessage.getCommandAliases().contains(commandName)) {
                // Strip out the command so it becomes the raw message.
                string = string.substring(commandName.length() + 1);
            }
        }
        return string;
    }
}
