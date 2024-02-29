# puzzled

## Intro
A open, real-time, online, distributed, collaborative, microservices-based puzzle game.

## Features
High-level puzzled's features:
- online, single-player puzzle resolution.
- online, multi-player puzzle resolution.
- dynamic user participation (join and leave) during the game.
- puzzle's tiles swap by sequential selection of them.
- real-time visualization of the other users' actions.

## Technologies
<div align="center">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/187070862-03888f18-2e63-4332-95fb-3ba4f2708e59.png" alt="websocket" title="websocket"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192107858-fe19f043-c502-4009-8c47-476fc89718ad.png" alt="REST" title="REST"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png" alt="Docker" title="Docker"/></code>
</div>

## How to deploy


### Gradle

#### Prerequisites
- Git (tested with version 2.30.1)


#### Steps
1. clone the repo into the desired folder:

```bash
    git clone https://github.com/danielegiulianini/puzzled
```

2. move inside the downloaded folder:

```bash
    cd puzzled
```

3. run the simple, 2-users application scenario.

```bash
    gradlew :system-test:run
```

5. use the app like show in the [demo section](#demo).


## Demo
This demo shows a simple 2-users scenario where a user completes the puzzle while the other does nothing but watching.
![](https://github.com/puzzled/puzzle.gif)
