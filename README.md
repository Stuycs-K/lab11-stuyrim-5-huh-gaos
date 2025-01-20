[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/KprAwj1n)
# APCS - Stuyrim

## Features

Make a clear list of features that work/dont work

:white_check_mark: This feature works.

:question: This feature works partially.

:ballot_box_with_check: This extra (beyond the things that the lab was supposed to do) feature works.

:x: This required feature does not work.

:beetle: This is a bug that affects the game.

:white_check_mark: generating enemies

> :white_check_mark: random classes and numbers

> :white_check_mark: boss class for one member enemy teams

:white_check_mark: generating player teams

> :white_check_mark: custom teams

> :white_check_mark: random teams

> :white_check_mark: player usernames

:white_check_mark: generating descriptions

> :white_check_mark: keeping track of turn messages

> :white_check_mark: terminal scrolling/division

:white_check_mark: screen layout + design

> :white_check_mark: colors, border

> :white_check_mark: win/lose/quit screens



### Extras

> :ballot_box_with_check: add function to convert "attack" / "support" etc to lowercase to ignorecase with/ StartsWith

> :ballot_box_with_check: validate attack/support / etc numerical input

> :ballot_box_with_check: add in a list of names + function to randomly select a name

> :ballot_box_with_check: add party summaries

### Changes to Adventure Class

- boolean status(): returns true if Adventurer is alive and false if Adventurer is dead

- applyDamage modified to floor at 0

- setHP modified to cap at maximumHP

### Changes to Text Class

- overload colorize to support rgb (foreground color only)

- overload clear to erase given box and keep background color

- wait to delay drawing

## Adventurer Subclasses

Adventurer | Special (initial, max) | Special Attack | Attack | Support | HP
--- | --- | --- | --- | --- | ---
Warrior | Shield (9, 9) | 10% damage on remaining HP per shield, costs 1 shield | 2HP damage, restores 4 shield | heals 3HP, restores 2 shield | 10HP
Pathfinder | Healing (5, 10) | 8 HP damage, costs 2 healing | 2HP damage | heals 5HP, uses 1 healing | 15HP
Boss | Reviving (2, 2) | no special attack | 5HP damage | heals 30HP, uses 1 revival | 30HP
