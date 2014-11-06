package com.xorinc.scalaplugin

import org.bukkit.command.{CommandSender, CommandExecutor, Command}
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import util.Util._
import ScalaPlugin._

class ScalaPlugin extends JavaPlugin {

  override def onEnable(): Unit = {

    ScalaPlugin.myself = Some(this)
    PlayerListener.register
    HelloCommand.register(getCommand("hello"))
    KillCountCommand.register(getCommand("killcount"))
  }
}

object ScalaPlugin {

  implicit lazy val * = myself getOrElse {throw new RuntimeException("Plugin not enabled yet!")}
  private[JavaPlugin] var myself: Option[JavaPlugin] = None
}

object HelloCommand extends PlayerOnlyCommand {
  override def run(sender: Player, cmd: Command, label: String, args: Array[String]): Unit =
    sender.message("Hello " + args.applyOrElse(0, "World") + "!")
}

object KillCountCommand extends CommandExecutor {
  override def onCommand(sender: CommandSender, cmd: Command, label: String, args: Array[String]): Boolean =
    if(args.length < 0){
      sender.message("Please provide a player")
      true
    } else {
      val player = args(0).player
      player match {
        case Some(p) => sender.message(s"${p.getName} has ${p.killCount} kills.")
        case None => sender.message(s"${args(0)} is offline!")
      }
      true
    }
}
