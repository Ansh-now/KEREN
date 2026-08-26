package com.keren.control.ui.screens.nervous

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.domain.model.*
import com.keren.control.ui.theme.*

/**
 * KEREN Nervous System Live View
 * Signature feature of V0.6
 *
 * This is NOT a static diagram.
 * Every pulse and packet must eventually be driven by real Event Bus events.
 */
@Composable
fun NervousSystemScreen() {
    // Demo snapshot (will be replaced by real ViewModel + WebSocket)
    val snapshot = remember { createDemoSnapshot() }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Continuous pulse animation for active nodes
    val infiniteTransition = rememberInfiniteTransition(label = "nervous")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Packet travel progress (demo loop — later driven by real packets)
    val packetProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "packet"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "NERVOUS",
                color = KerenBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "LIVE • REAL EVENTS",
                color = KerenGreen,
                fontSize = 11.sp
            )
        }

        // Graph area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.5f, 2.5f)
                        offset += pan
                    }
                }
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val w = size.width
                val h = size.height

                fun nodePos(node: KerenNode): Offset {
                    return Offset(
                        x = node.x * w * scale + offset.x,
                        y = node.y * h * scale + offset.y
                    )
                }

                // Draw edges first
                snapshot.edges.forEach { edge ->
                    val from = snapshot.nodes.find { it.id == edge.fromId } ?: return@forEach
                    val to = snapshot.nodes.find { it.id == edge.toId } ?: return@forEach
                    val start = nodePos(from)
                    val end = nodePos(to)

                    val edgeColor = when {
                        edge.isActive -> KerenBlue.copy(alpha = 0.75f)
                        else -> KerenBorder.copy(alpha = 0.45f)
                    }

                    drawLine(
                        color = edgeColor,
                        start = start,
                        end = end,
                        strokeWidth = if (edge.isActive) 2.8f else 1.2f,
                        pathEffect = if (!edge.isActive) PathEffect.dashPathEffect(floatArrayOf(8f, 6f)) else null
                    )
                }

                // Draw traveling packets on active edges
                snapshot.edges.filter { it.isActive }.forEach { edge ->
                    val from = snapshot.nodes.find { it.id == edge.fromId } ?: return@forEach
                    val to = snapshot.nodes.find { it.id == edge.toId } ?: return@forEach
                    val start = nodePos(from)
                    val end = nodePos(to)

                    val t = packetProgress
                    val packetPos = Offset(
                        x = start.x + (end.x - start.x) * t,
                        y = start.y + (end.y - start.y) * t
                    )

                    // Glow
                    drawCircle(
                        color = KerenBlue.copy(alpha = 0.22f),
                        radius = 16f,
                        center = packetPos
                    )
                    // Core packet
                    drawCircle(
                        color = KerenBlueGlow,
                        radius = 6.5f,
                        center = packetPos
                    )
                }

                // Draw nodes
                snapshot.nodes.forEach { node ->
                    val pos = nodePos(node)
                    val baseRadius = when (node.type) {
                        NodeType.CORE -> 24f
                        NodeType.USER -> 17f
                        else -> 15f
                    }

                    val nodeColor = when (node.state) {
                        NodeState.ERROR -> KerenRed
                        NodeState.OFFLINE -> KerenGrey
                        NodeState.PROCESSING, NodeState.SENDING -> KerenBlueGlow
                        NodeState.RECEIVING -> KerenGreen
                        else -> when (node.type) {
                            NodeType.CORE -> KerenGreen
                            NodeType.ROUTER -> KerenAmber
                            NodeType.DEVICE, NodeType.WORKER -> KerenBlue
                            NodeType.APP -> KerenBlueGlow
                            else -> KerenGrey
                        }
                    }

                    // Outer pulse for active nodes
                    if (node.state == NodeState.PROCESSING ||
                        node.state == NodeState.SENDING ||
                        node.state == NodeState.RECEIVING
                    ) {
                        drawCircle(
                            color = nodeColor.copy(alpha = 0.18f),
                            radius = baseRadius * pulse,
                            center = pos
                        )
                    }

                    // Main node body
                    drawCircle(
                        color = KerenSurface,
                        radius = baseRadius,
                        center = pos
                    )
                    drawCircle(
                        color = nodeColor,
                        radius = baseRadius,
                        center = pos,
                        style = Stroke(width = 2.6f)
                    )

                    // Inner core
                    drawCircle(
                        color = nodeColor,
                        radius = baseRadius * 0.32f,
                        center = pos
                    )
                }
            }

            // Zoom controls
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ZoomButton("−") { scale = (scale - 0.15f).coerceAtLeast(0.5f) }
                Text(
                    text = "${(scale * 100).toInt()}%",
                    color = KerenGrey,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                ZoomButton("+") { scale = (scale + 0.15f).coerceAtMost(2.5f) }
            }
        }

        // Node labels (readable)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            snapshot.nodes.take(7).forEach { node ->
                Text(
                    text = node.label,
                    color = if (node.state != NodeState.IDLE) KerenBlueGlow else KerenGrey,
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }
        }

        // Event stream (bottom)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                .background(KerenSurface)
                .border(1.dp, KerenBorder)
                .padding(12.dp)
        ) {
            Text(
                text = "EVENT STREAM",
                color = KerenBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyColumn {
                items(snapshot.events) { event ->
                    val color = when (event.level) {
                        EventLevel.SUCCESS -> KerenGreen
                        EventLevel.ERROR -> KerenRed
                        EventLevel.WARNING -> KerenAmber
                        else -> KerenText
                    }
                    Text(
                        text = "${event.timestamp}  ${event.message}",
                        color = color,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoomButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(KerenSurfaceVariant, RoundedCornerShape(4.dp))
            .border(1.dp, KerenBorder, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = KerenText, fontSize = 14.sp)
    }
}

private fun createDemoSnapshot(): NervousSnapshot {
    val nodes = listOf(
        KerenNode("user", "USER / PC", NodeType.USER, NodeState.IDLE, 0.50f, 0.07f),
        KerenNode("core", "KEREN CORE", NodeType.CORE, NodeState.PROCESSING, 0.50f, 0.26f),
        KerenNode("planner", "PLANNER", NodeType.PLANNER, NodeState.IDLE, 0.25f, 0.40f),
        KerenNode("router", "ROUTER", NodeType.ROUTER, NodeState.SENDING, 0.75f, 0.40f),
        KerenNode("phone", "PHONE-01", NodeType.DEVICE, NodeState.RECEIVING, 0.75f, 0.58f),
        KerenNode("worker", "ANDROID WORKER", NodeType.WORKER, NodeState.PROCESSING, 0.75f, 0.74f),
        KerenNode("spotify", "SPOTIFY", NodeType.APP, NodeState.IDLE, 0.75f, 0.90f),
        KerenNode("memory", "MEMORY", NodeType.MEMORY, NodeState.IDLE, 0.25f, 0.58f)
    )

    val edges = listOf(
        KerenEdge("e1", "user", "core", isActive = true),
        KerenEdge("e2", "core", "planner", isActive = false),
        KerenEdge("e3", "core", "router", isActive = true),
        KerenEdge("e4", "router", "phone", isActive = true),
        KerenEdge("e5", "phone", "worker", isActive = true),
        KerenEdge("e6", "worker", "spotify", isActive = false),
        KerenEdge("e7", "core", "memory", isActive = false)
    )

    val events = listOf(
        NervousEvent("14:52:31", "TASK RECEIVED", EventLevel.INFO, "task_1842"),
        NervousEvent("14:52:31", "PLANNER → TASK_GRAPH", EventLevel.INFO),
        NervousEvent("14:52:32", "ROUTER → PHONE-01", EventLevel.INFO),
        NervousEvent("14:52:33", "PHONE-01 → APP.OPEN", EventLevel.INFO),
        NervousEvent("14:52:34", "APP.OPEN → SUCCESS", EventLevel.SUCCESS),
        NervousEvent("14:52:35", "UI.INSPECT", EventLevel.INFO),
        NervousEvent("14:52:36", "UI.FIND → Search", EventLevel.INFO),
        NervousEvent("14:52:37", "UI.CLICK", EventLevel.INFO)
    )

    return NervousSnapshot(
        nodes = nodes,
        edges = edges,
        events = events
    )
}
