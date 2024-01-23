## PotionSplashDecoration
A nukkit plugin that shows potion splash effect to a certain set of points

## Commands
| Command                   | Description                                      |
|---------------------------|--------------------------------------------------|
| potionsplash reload       | Reload Configuration                             |
| potionsplash add effectID | Add a potion splash effect to where you stand    |
| potionsplash remove       | Remove a potion splash effect to where you stand |

## config.yml
```yaml
interval_ticks: 20
potion_spawns:
  - potion_id: 1
    x: 1.0
    y: 1.0
    z: 1.0
    world: "world"
```

## Notes
1. To change command name, you should restart the server!
2. This plugin is specially for the rewards of insiders who have helped me finished many non-profit plugins.
3. No snapshot provided. It's currently in beta phase now.