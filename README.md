# Sequence-Dice

Sequence-Dice is a digital version of the Sequence-Dice board game developed in Java and Android. The project demonstrates clean architecture, SOLID principles, and reuse of a single game model across multiple user interfaces.

## Game Overview

2–4 players (including team-based play)

6×6 board with values from 2–12

Two six-sided dice

Goal: form a winning sequence of tokens in a row, column, or diagonal

## Architecture

Pure Java game model (SequenceDice)

Contains all game data and rules

Completely independent of any UI

Uses the Observer pattern to notify game events

The same model is used by:

A Java console application

An Android pass-and-play application

Android App Features

Splash screen with game branding

Main menu: Start Game, Rules, Leaderboard

Player setup: names, number of players, team colours

Visual board with images, coloured tokens, and dice

End-of-game report with game statistics

Persistent top-10 leaderboard

Rules screen with a YouTube tutorial link (via intent)

Console Version

Text-based menu-driven gameplay

Demonstrates full decoupling of game logic from UI

## Technologies Used

Java

Android SDK

SOLID design principles

Observer pattern

Local data persistence
