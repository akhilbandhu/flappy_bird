# Flappy Bird Multiplayer

This project is a multiplayer version of the classic Flappy Bird game, built with a Kotlin backend, Svelte frontend, and gRPC for communication.

## Project Structure

- `frontend/`: Svelte-based frontend
- `backend/`: Kotlin-based backend
- `proto/`: Protocol Buffer definitions

## Prerequisites

- Node.js and npm
- Kotlin and JDK
- Envoy proxy
- Protocol Buffer compiler (protoc)

## Setup

1. Clone the repository:
   ```
   git clone <repository-url>
   cd flappy-bird-multiplayer
   ```

2. Set up the frontend:
   ```
   cd frontend
   npm install
   ```

3. Set up the backend:
   ```
   cd backend
   ./gradlew build
   ```

4. Install Envoy proxy (follow instructions for your OS)

## Running the Application

1. Start the backend server:
   ```
   cd backend
   ./gradlew run
   ```

2. Start Envoy proxy:
   ```
   cd frontend
   envoy -c envoy.yaml
   ```

3. Start the frontend development server:
   ```
   cd frontend
   npm run dev
   ```

4. Open your browser and navigate to `http://localhost:8080`

## Development

- Frontend code is in `frontend/src/`
- Backend code is in `backend/src/main/kotlin/`
- Protocol Buffer definitions are in `frontend/src/flappybird.proto` and `backend/app/src/main/proto/flappybird.proto`

## Building for Production

1. Build the frontend:
   ```
   cd frontend
   npm run build
   ```

2. Build the backend:
   ```
   cd backend
   ./gradlew build
   ```

3. Deploy the built frontend, backend JAR, and configure Envoy in your production environment.

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.