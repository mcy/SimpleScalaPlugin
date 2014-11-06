package com.xorinc.scalaplugin

import org.bukkit.event.block.Action._
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.{PlayerInteractEvent, PlayerQuitEvent}
import org.bukkit.event.{EventHandler, Listener, EventPriority}
import EventPriority._
import util.Util._
import ScalaPlugin._


object PlayerListener extends Listener {

  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onPlayerQuit(event: PlayerQuitEvent): Unit = event.getPlayer.killCount = 0

  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onEntityDeath(event: EntityDeathEvent): Unit = event.getEntity.getKiller match {
    case null =>
    case killer =>
      killer.killCount++;
      killer.message(s"You have killed ${killer.killCount} things.")
  }

  @EventHandler(priority = MONITOR, ignoreCancelled = true)
  def onPlayerInteract(event: PlayerInteractEvent) = event.getAction match {
    case RIGHT_CLICK_BLOCK =>
      val mat = event.getPlayer.getItemInHand.getType
      async {
        Thread.sleep(500)
        sync {
          event.getPlayer.message(s"You have a $mat.")
        }
      }
    case _ =>
  }

}
