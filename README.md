# Project Red

A Fabric mod for Minecraft 26.2. Adds carmine, a red metal made from redstone, iron and netherite,
with a full block set, three weapons and three enchantments to go with them.

<p>
  <img src="art/source/carmine%20longsword/carmine%20longsword%2032x.png" width="96" alt="Carmine Longsword">
  <img src="art/source/carmine%20leviathan%20axe/carmine%20leviathan%20axe%2032x.png" width="96" alt="Carmine Leviathan Axe">
  <img src="art/source/carmine%20scythe/Carmine%20Scythe%2032x.png" width="96" alt="Carmine Scythe">
</p>

## Requirements

| | |
| --- | --- |
| Minecraft | 26.2 |
| Fabric Loader | 0.20.1 or newer |
| Fabric API | 0.161.0+26.2 |
| Java | 25 |

## Carmine

Shapeless: 4 redstone, 2 iron ingots and 2 netherite ingots, which gives 2 carmine ingots. Nine
nuggets make an ingot, nine ingots make a block. The block set is a block, stairs, slab and wall,
all needing a diamond pickaxe or better.

## The weapons

**Carmine Longsword** — netherite damage plus one block of reach, and it is held like a crossbow.
Each hit that lands adds a charge, up to three, drawn above the durability bar. Right clicking
spends a charge on a dash. At three charges, right clicking with Detonation detonates instead: an
8 block blast that leaves the wielder untouched.

**Carmine Leviathan Axe** — 10 damage, and Strength I while it is held in either hand. Right click
the ground while standing on it to slam. Everything within about four blocks takes 8 damage and is
thrown outward and up, and the wielder is launched clear of the crater. The screen shakes.

**Carmine Scythe** — 10 damage at 1.3 attack speed, two more than a netherite sword. Critical hits
heal half a heart and drag the target toward you.

## Enchantments

All three are single level. Books for them are in the Carmine creative tab.

- **Detonation** — only does anything on the longsword. Unlocks the full charge blast.
- **Pressure Point** — +4 damage against a player wearing no netherite armour.
- **Prey** — scythe only. Right click to mark a random player within 100 blocks with Glowing for
  10 seconds, with a 30 second cooldown.

## Building

Needs JDK 25.

```
./gradlew build
```

The jar ends up in `build/libs`.

## Art

`art/source` holds the textures at both 16x and 32x and the model files the item models are built
from.

## Authors

- Pheyx
- Asher

## License

CC0.
