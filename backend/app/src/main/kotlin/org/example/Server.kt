package org.example

import flappybird.*
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.abs

class FlappyBirdServiceImpl : FlappyBirdServiceGrpc.FlappyBirdServiceImplBase() {

    private var birdPositionY = 0.5f
    private val pipePositionsX = mutableListOf(1.0f, 1.5f, 2.0f)
    private val pipePositionsY = mutableListOf(0.3f, 0.5f, 0.7f)
    private var score = 0
    private var gameOver = false

    override fun getGameState(request: Empty, responseObserver: StreamObserver<GameState>) {
        val gameState = GameState.newBuilder()
            .setBirdPositionY(birdPositionY)
            .addAllPipePositionsX(pipePositionsX)
            .addAllPipePositionsY(pipePositionsY)
            .setScore(score)
            .setGameOver(gameOver)
            .build()
        responseObserver.onNext(gameState)
        responseObserver.onCompleted()
    }

    override fun sendInput(request: PlayerInput, responseObserver: StreamObserver<Ack>) {
        if (request.flap) {
            // Update bird's position upwards
            birdPositionY += 0.1f
        }
        val ack = Ack.newBuilder().setSuccess(true).build()
        responseObserver.onNext(ack)
        responseObserver.onCompleted()
    }

    // Game loop to update game state
    init {
        Timer().scheduleAtFixedRate(0, 100) {
            updateGameState()
        }
    }

    private fun updateGameState() {
        if (!gameOver) {
            // Update bird position (gravity effect)
            birdPositionY -= 0.02f

            // Move pipes
            for (i in pipePositionsX.indices) {
                pipePositionsX[i] -= 0.01f
                if (pipePositionsX[i] < -0.1f) {
                    pipePositionsX[i] = 1.0f
                    // Randomize pipe Y position using a random float between 0.2 and 0.8
                    pipePositionsY[i] = (0.2f + (0.8f - 0.2f) * kotlin.random.Random.nextFloat())
                    score++
                }
            }

            // Collision detection (simplified)
            for (i in pipePositionsX.indices) {
                if (abs(pipePositionsX[i]) < 0.05f && abs(birdPositionY - pipePositionsY[i]) > 0.2f) {
                    gameOver = true
                    break
                }
            }

            // Check if bird is out of bounds
            if (birdPositionY < 0 || birdPositionY > 1) {
                gameOver = true
            }
        }
    }
}

fun main() {
    val server = ServerBuilder.forPort(50051)
        .addService(FlappyBirdServiceImpl())
        .build()
        .start()
    println("Server started on port 50051")
    server.awaitTermination()
}
