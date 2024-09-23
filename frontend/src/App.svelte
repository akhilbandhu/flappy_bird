<script>
  import { onMount } from 'svelte';
  import { FlappyBirdServiceClient } from './Flappybird_grpc_web_pb';
  import { Empty, PlayerInput } from './Flappybird_pb';

  const client = new FlappyBirdServiceClient('http://localhost:8080');

  let birdPositionY = 0.5;
  let pipePositionsX = [];
  let pipePositionsY = [];
  let score = 0;
  let gameOver = false;
  let gameStarted = false;

  function getGameState() {
    const request = new Empty();
    client.getGameState(request, {}, (err, response) => {
      if (err) {
        console.error(err);
        return;
      }
      birdPositionY = response.getBirdpositiony();
      pipePositionsX = response.getPipepositionsxList();
      pipePositionsY = response.getPipepositionsyList();
      score = response.getScore();
      gameOver = response.getGameover();
    });
  }

  function sendInput(flap) {
  const request = new PlayerInput();
  request.setFlap(flap);
  client.sendInput(request, {}, (err, response) => {
    if (err) {
      console.error(err);
    }
  });
}


  function handleKeyDown(event) {
    if (event.code === 'Space') {
      sendInput(true);
    }
  }

  function startGame() {
    gameStarted = true;
    gameOver = false;
    score = 0;
    // Add any other initialization logic here
  }

  onMount(() => {
    window.addEventListener('keydown', handleKeyDown);
    const interval = setInterval(getGameState, 100);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
      clearInterval(interval);
    };
  });
</script>

<style>
  main {
    text-align: center;
    margin: 0 auto;
  }
  .bird {
    position: absolute;
    left: 50px;
    width: 30px;
    height: 30px;
    background-color: yellow;
    border-radius: 50%;
  }
  .pipe {
    position: absolute;
    width: 50px;
    background-color: green;
  }
  .start-screen {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
  }
</style>

<main>
  {#if !gameStarted}
    <div class="start-screen">
      <h1>Flappy Bird</h1>
      <button on:click={startGame}>Start Game</button>
    </div>
  {:else}
    <h1>Flappy Bird</h1>
    <h2>Score: {score}</h2>
    {#if !gameOver}
      <div
        class="bird"
        style="top: calc({birdPositionY} * 100%)"
      ></div>
      {#each pipePositionsX as pipeX, index}
        <div
          class="pipe"
          style="
            left: calc({pipeX} * 100%);
            top: 0;
            height: calc({pipePositionsY[index]} * 100% - 100px);
          "
        ></div>
        <div
          class="pipe"
          style="
            left: calc({pipeX} * 100%);
            bottom: 0;
            height: calc((1 - {pipePositionsY[index]}) * 100% - 100px);
          "
        ></div>
      {/each}
    {:else}
      <h2>Game Over</h2>
      <button on:click={() => location.reload()}>Restart</button>
    {/if}
  {/if}
</main>