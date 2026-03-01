# Pac-Man in Java

Ein klassisches Pac-Man-Spiel, implementiert in Java mit Swing.

## Features

- Klassisches Labyrinth (19 × 21 Tiles)
- Vier Geister mit unterschiedlichem KI-Verhalten (Blinky, Pinky, Inky, Clyde)
- Power-Pellets: Geister werden kurzzeitig essbar
- Tunnel-Wrap links/rechts
- Punktesystem, Lebensanzeige, Pause-Funktion
- Game-Over und Sieg-Bildschirm mit Neustart

## Steuerung

| Taste           | Aktion               |
|-----------------|----------------------|
| Pfeiltasten     | Pac-Man bewegen      |
| W / A / S / D   | Pac-Man bewegen      |
| P               | Pause ein/aus        |
| Enter           | Neustart (nach Ende) |

## Voraussetzungen

- Java JDK 11 oder höher

## Kompilieren & Starten

```bash
javac -d out src/main/java/pacman/*.java
java -cp out pacman.Main
```

## Projektstruktur

```
pacman-java/
├── src/main/java/pacman/
│   ├── Main.java
│   ├── GameWindow.java
│   ├── GamePanel.java
│   ├── GameConstants.java
│   ├── Maze.java
│   ├── PacMan.java
│   ├── Ghost.java
│   └── Direction.java
└── README.md
```

## Geister-KI

| Geist  | Farbe  | Verhalten                            |
|--------|--------|--------------------------------------|
| Blinky | Rot    | Verfolgt Pac-Man direkt              |
| Pinky  | Pink   | Zielt 4 Felder vor Pac-Man           |
| Inky   | Cyan   | Mischung aus Verfolgung und Zufall   |
| Clyde  | Orange | Mischung aus Verfolgung und Zufall   |
