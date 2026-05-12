package me.alpha432.oyvey.features.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.manager.CommandManager;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static me.alpha432.oyvey.features.commands.MessageSignatures.GENERAL;
import static me.alpha432.oyvey.features.commands.MessageSignatures.SUCCESS;

public class ProfileCommand extends Command {
    public ProfileCommand() {
        super("profile", "profiles");
        setDescription("Manage config profiles");
    }

    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        builder.executes(ctx -> {
            String current = OyVey.configManager.getCurrentProfile();
            var profiles = OyVey.configManager.listProfiles();
            sendMessage("{gray}Current profile: {green}%s", GENERAL, current);
            sendMessage("{gray}Available: {white}%s", GENERAL, String.join(", ", profiles));
            return SINGLE_SUCCESS;
        });

        builder.then(literal("list").executes(ctx -> {
            var profiles = OyVey.configManager.listProfiles();
            sendMessage("{gray}Config profiles: {white}%s", GENERAL, String.join(", ", profiles));
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("set")
                .then(argument("name", StringArgumentType.word())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            if (OyVey.configManager.setProfile(name)) {
                                sendMessage("{green}Switched to profile: {white}%s", SUCCESS, name);
                            } else {
                                sendMessage("{red}Failed to switch to profile: {white}%s", SUCCESS, name);
                            }
                            return SINGLE_SUCCESS;
                        })));

        builder.then(literal("delete")
                .then(argument("name", StringArgumentType.word())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            if (OyVey.configManager.deleteProfile(name)) {
                                sendMessage("{green}Deleted profile: {white}%s", SUCCESS, name);
                            } else {
                                sendMessage("{red}Cannot delete profile: {white}%s", SUCCESS, name);
                            }
                            return SINGLE_SUCCESS;
                        })));
    }
}
