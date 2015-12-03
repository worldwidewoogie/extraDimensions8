# ExtraDimensions
Extra Dimensions Minecraft Mod

Allows dynamic creation and deletion of dimensions via commands (restricted to ops in multiplayer).  Adds the following commands:

```
/xdcreate <Dimension Name> <Game Type> <World Type> <World Options> <World Seed> 
  create a new ExtraDimension
     <Dimension Name> - required.  String with no spaces
     <Game Type>      - optional.  creative or survival (defaults to creative)
     <World Type>     - optional.  World type for new dimension:
                                          flat
                                          default
                                          largeBiomes
                                          amplified
                                          default_1_1
                                          custom
     <World Options>  - optional.  Options for the above world type (See below), 
                                   or '-' if you would like to provide a seed 
                                   without providing any customized options.
     <World Seed>     - optional.  The seed used in world generation.

/xddelete <Dimension Name>
  delete existing ExtraDimension
     <Dimension Name> - required.  Name or ID of dimension to delete

/xdlist
  list existing ExtraDimensions

/xdrename <Dimension Name> <New Dimension Name>
  rename existing ExtraDimension
     <Dimension Name> - required.  Name of dimension to rename
     <New Dimension Name>   - required.  New name for dimension

/xdtp <target player name> <destination dimension name>
  teleport to an ExtraDimension.
    If teleporting from survival to creative, the survival inventory 
       is saved, and the creative inventory (if it exists) is restored.
    If teleporting from creative to survival, the creative inventory 
       is saved, and the survival inventory (if it exists) is restored.
```

## World Options

World options can be of 2 forms:

* Minecraft option strings
  * The full option string used by Minecraft to generate the world
* Presets
  * A short string that ExtraDimensions translates into a Minecraft option strings

The 2 World Types that support options are

1. flat

The following presets are supported:
  * classicflat
  * overworld
  * dessert
  * tunnelersdream
  * snowykingdom
  * redstoneready
  * waterworld
  * bottomlesspit
  * thevoid

A tool for generating Minecraft option strings can be found here:
  * http://minecraft.tools/en/flat.php 

2. custom

The following presets are supported:
  * default
  * isleland
  * mountainmadness
  * caveofchaos
  * waterworld
  * caversdelight
  * drought
  * goodluck

A tool for generating Minecraft option strings can be found here:
  * http://minecraft.tools/en/custom.php 

**Note:** There is a limit to the length of commands you can enter in the Minecraft command line.  Minecraft option strings are almost guaranteed to go over this 
limit.  You can use command blocks to issue the commands to get around this limitation.

