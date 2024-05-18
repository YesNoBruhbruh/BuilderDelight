package me.maanraj514.builderdelight.util

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage

fun Audience.sendColorizedMessage(msg: String) {
    sendMessage(MiniMessage.miniMessage().deserialize(msg))
}
class MessageUtil {
}