package com.xorinc.scalaplugin.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.command.{CommandSender, PluginCommand, Command, CommandExecutor}
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import collection.mutable

object Util {

  private val killCounts = mutable.Map.empty[String, Int].withDefaultValue(0)
  val prefix = RED + "[" + YELLOW + "SimpleScalaPlugin" + RED + "]" + GOLD + " "

  implicit class RichString(val s: String) extends AnyVal {

    def player: Option[Player] = Option(Bukkit.getPlayer(s))
  }

  implicit class RichMap[A, B](val map: mutable.Map[A, B]) extends AnyVal {

    def adjust(a: A)(f: B => B) = map(a) = f(map(a))
  }

  implicit class RichSender(val sender: CommandSender) extends AnyVal {

    @inline def message(s: String): Unit = sender.sendMessage(prefix + s)
  }

  implicit class RichPlayer(val p: Player) extends AnyVal {

    def incrementKills(i: Int): Unit = killCounts.adjust(p.getName)(_ + i)
    def decrementKills(i: Int): Unit = killCounts.adjust(p.getName)(_ - i)
    @inline def incrementKills(): Unit = incrementKills(1)
    @inline def decrementKills(): Unit = decrementKills(1)
    def resetKills(): Unit = killCounts -= p.getName
    def kills: Int = killCounts(p.getName)
    def kills_=(i: Int): Unit = killCounts(p.getName) = i

    def killCount = new PlayerKillCount(p)
    def killCount_=(i: Int) = p.kills = i
  }

  class PlayerKillCount private[Util] (val p: Player) extends AnyVal {
    @inline def -- = p.decrementKills()
    @inline def ++ = p.incrementKills()
    @inline def +=(i: Int) = p.incrementKills(i)
    @inline def -=(i: Int) = p.decrementKills(i)
    override def toString: String = p.kills.toString
    def * = p.kills
  }

  implicit class RichListener (val l: Listener) extends AnyVal {
    @inline def register(implicit plugin: Plugin) = Bukkit.getPluginManager.registerEvents(l, plugin)
  }

  implicit class RichExecutor (val e: CommandExecutor) extends AnyVal {
    @inline def register(cmd: PluginCommand)(implicit plugin: Plugin) = cmd.setExecutor(e)
  }

  implicit def toInt(count: PlayerKillCount): Int = count.p.kills

  private case class FunctionRunnable(f: () => Unit) extends BukkitRunnable {
    override def run(): Unit = f()
  }

  def sync(f: => Unit)(implicit plugin: Plugin) = FunctionRunnable(() => f).runTask(plugin)
  def async(f: => Unit)(implicit plugin: Plugin) = FunctionRunnable(() => f).runTaskAsynchronously(plugin)
}
