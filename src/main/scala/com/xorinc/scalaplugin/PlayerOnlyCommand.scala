package com.xorinc.scalaplugin

import org.bukkit.command.{Command, CommandSender, CommandExecutor}
import org.bukkit.entity.Player

trait PlayerOnlyCommand extends CommandExecutor {

  final override def onCommand(sender: CommandSender, cmd: Command, label: String, args: Array[String]): Boolean = {
    sender match {
      case p: Player => run(p, cmd, label, args)
      case x => x.sendMessage("Player-only command!")
    }; true }

  def run(sender: Player, cmd: Command, label: String, args: Array[String]): Unit
}
