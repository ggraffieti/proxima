package org.gammf.proxima.dns.hierarchy.util

import akka.actor.{Actor, Props}
import org.gammf.proxima.dns.hierarchy.messages.{HierarchyNode, HierarchyNodesMessage}

/**
  * This is an actor that deals with prints on the output console.
  * @param name the name of this actor.
  */
class PrinterActor(val name: String) extends Actor {
  override def receive: Receive = {
    case HierarchyNodesMessage(list) => handleHierarchy(list)
    case msg :Any => println(msg)
  }

  private[this] def handleHierarchy(nodes: List[HierarchyNode]): Unit = {
    println(); println("[" + name + "] " + nodes.size + " actors found, CURRENT HIERARCHY {"); println()
    nodes.foreach(printNode); println(); println("} END CURRENT HIERARCHY"); println()
    def printNode(n: HierarchyNode): Unit = {
      println(getIndent(n.level) + n.name + " => INFO[" + n.role + ", " + n.service + ", " + n.reference + "]")
      def getIndent(level: Int): String = "    " * (level - nodes.map(_.level).min)
    }
  }
}

object PrinterActor {
  /**
    * Factory methods that returns a Props to create a printer actor.
    * @param name the name of this actor.
    * @return the Props to use to create a printer actor.
    */
  def printerProps(name: String = "Printer"): Props =
    Props(new PrinterActor(name = name))
}