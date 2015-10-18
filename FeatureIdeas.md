# Recent Brainstorming #

The engine could supply some different systems that create own areas of simulation, with interesting goals for players and NPC:s.

A game can pick suitable systems and parametrize them, as well as implementing own specialized systems or adding systems other people have created.


There's two approaches to looking at the systems - a use case view that looks at what goals and fun value a feature provides to a player, and a technical view that looks at how a feature is best implemented and how it fits together with other features to provide a consistent and robust simulation.


## Systems ##

Going from most general and re-usable towards more specific systems.

  * SpaceSystem - A framework that contains objects at locations.  Also takes care of simulating movement and detecting collisions, as well as providing perceptions to objects in the Space.
    * SingularSpace - A simple space implementation with only one coordinate, can be used for simulating various containers in a game (although a 1D GridSpace may also be used for WoW-style bags).  The contained items are kept in a list in insertion order.
    * GridSpace - An implementation of Space where objects are located at integer coordinates on a fixed (two dimensional) area.
    * OuterSpace - An implementation of Space for interplanetary space.
    * TerrainSpace - An implementation of a Space that has a ground plane with a height function.
    * IndoorSpace - An implementation of Space consisting of rooms connected with portals.
  * ModelSystem - Used to define parametrized, animated 3D models.
  * UiSystem - Used to create user interfaces that can invoke actions on Entities the user controls.
  * ChatSystem - Allows users to add timestamped comments to an ongoing conversation in some channel.  The channel could also be connected to some edited object, or possibly a specific comment, allowing threads.  See the WaveSystem below.
  * WaveSystem / CommunicationSystem - Powerful chat & communication & collaboration system with Google-Wave like features?  Share editing commands to a common document between people, and store documents in a searchable journal like structure with access control?  Combines chat with the other editing systems, while providing version history browsing if available.  Waves are usually hosted by some in-game object, e.g. one can sit down with a group at a table and annotate a map or chat or play a game of cards or design a machine.  More advanced game-worlds can provide ways for people to particiapte remotely, by hosting the wave on some in-game computer and connecting to it over networks or magical devices, or games can just choose to allow creation of waves and inviting friends without any in-game infrastructure.  (This actually has some similarities with the GameSystem below, could they be merged?).
  * GameSystem - Allows creation of games - simulations where players go through some setup / character creation process, a game phase where their avatars can perform various actions to affect the game state, and an optional scoring phase when the game ends, where the scores are displayed.  There could also be a game browsing system that allows players to create games, team up, and chat before starting, and that stores score statistics or keeps track of tournament matchings (it could be implemented as a game).
    * BoardGame - A game with a GridSpace based game board, where players take turns to do actions, and there are clear winning conditions and some scoring mechanism.
    * WorldGame - A persistent (multiplayer) world with simulation rules and relatively open-ended gameplay, usually without any end phase for the game, and where players can join and leave at any time.
  * ResourceSystem - Represent real resources as in-game resource chips - e.g. computer time and storage space.  They can be harvested or received as an incentive for something, or bought with real money from the server hoster.  Whenever players create or do something that consumes significant real resources, some of the chips are consumed.  That way, players pay for the actual resources they use, creating a very fair economy where the interests of the server maintainer and the playerbase are somewhat aligned.  This makes it expensive to create a swarm of self replicating grey goo (although still possible if the grey goo can harvest the chips from the environment - in competition with other animals), or to run scripts with hundreds of parallel figures to farm trivial resources, or to create a lot of very complicated drawings or other crafted items that take up a lot of storage space.  Even with some marigin for the server hoster using the chips for normal gameplay should be very inexpensive (at least with sufficient players), and normal players could get by with freely available chips or by trading in-game services / currency for chips.  Owning something might require paying chips for it in upkeep, if that is not done it falls back into public domain, and if e.g. left somewhere could be stolen by some garbage collector animal and destroyed / decomposed (transport it to some compost heap / dump, delete the object, and change the compost heap size and raw material composition percentages).  If information objects stored in character memory (or library) isn't paid for, the character simply forgets them, or they fade away from the library.  Players will probably want to set up some publically funded libraries to make sure to save the most valuable products of their culture!  Unmaintained (maintanenace cost = chips) buildings and other structures slowly fall into ruin and finally collapse in garbage heaps (garbage heaps are slowly also eroded away and absorbed into the soil).  Changes in the environment, if not maintained, will slowly fill in and erode away.
  * CraftingSystem - Allows creating tools and entities with various abilities (e.g. cut ability, pierce ability, sew ability, heat ability, etc), that are parametrized (precision, effect area, effect force, tool durability, tool ergonomy, tool weight, tool size, etc).  The tools can then be applied with some skill to various objects to effect them in some way.  E.g. any object with a cutting ability can be used with the woodcutting skill to shape a log, the speed to the work and resolution and quality of the work will depend on the tool parameters and user abilities, and to a small degree on chance.
  * OwnershipSystem - Allows tagging some land are, building area or items as belonging to some juridical person.  The person can then grant usage and other rights of them in a contract.  The gamesystem may be set up to protect property from modifications or entry by others.  It may also be set up to allow contesting the ownership in some situations, e.g. if the owners are at war with each other.  There can also be some method specified for how non-owned land and property can be claimed, and when property goes back to unclaimed status.
  * TradeSystem - Allow legal entities to buy and sell things they own (or carry).  Can also be extended to trading houses, where the assets traded can be shares and other fun stuff).
  * OrgSystem - Provide a system to create all kinds of organizations in-game, and set up roles with different rights to act in the name of the organization, as well as rules for how the roles are changed and how the rules themselves are changed.  The Organizations can then be used for anything from a small guild of friends to a nation.
  * ContractSystem - Allow legal entities to agree on game system enforced conditions (e.g. fee paid if some package is delivered within some time, a deposit moved to other party if some definable agreement is breached, etc).  Can also be used as basis for laws and taxation that are applied to some land controlled by some legal entity.
  * CreatureSystem
  * CombatSystem
  * AgentSystem - AI Agents
  * WarSystem - Enables specifying how PvP flags are set on and off, how fractions make war and peace, and so on.
  * BuildingSystem - For constructing houses and other structures from blueprints and rawmaterials.  Could also be used for creating roads and other infrastructure.
  * MachineSystem - Allows putting together machine parts with various simple functions and inputs and outputs of energy, matter, or information, to create factories, vehicles, and all kinds of fun toys.
  * MagicSystem - Allows constructing spells from spell components with different functions, and simulates the flow of magic energy between creatures, the environment, and the spell forms.
  * GraphicsSystem - for generating procedural textures, as well as for allowing players to collaboratively paint in-game.
  * SoundSystem - for generating procedural instrument sounds and then composing them together into parametrizable music.
  * TextSystem - for editing text with simple formatting and markup, maybe hyperlinks and embedded other objects.
  * ScriptSystem - for creating programs and in-game scripts, to e.g. control machines, or instruct NPC:s, or control player characters while offline, or as recipies / actions that can be shared between players (beware grief-using in this case - e.g. list used actions or something?, or provide ranking ability).  Could get a visual editor too at some point if it makes sense?  To make it easy to use for most players.
  * TableSystem - simple spreadsheet like view of properties in some Entities?
  * LibrarySystem - allows creating organized collections of information objects on some in-game host object (book, bookshelf, library, desk drawers, computer, pocket dimension), or without any in-game host.  Can contain various information based objects.  Could also provide tagging, scoring, and commenting system by users.  (Maybe this could be merged with the WaveDirectory / Game selection system to some degree?).  Also used for organizing and sharing assets during game world editing and creation by world designers (so should be easy to import / export / synchronize with other game servers or the client).

# Older Notes #

This is mostly a braindump of a rough list of features

Important domains are **bolded**.  Importance is determined both based on what needs to be done to get a simple game up and running, as well as for things that potentially have large effects on the architecture that should be taken into account early.


## Core Domain Modules ##

### Accounts ###

  * **Login handling**. (handling incoming connections, allowing them to log in, and opeing   their account, or allowing them to create new accounts.
  * **User accounts**.  Keeps track of user characters, allows starting character creation.  Admin user can give tags to user accounts that allows characters controlled by them to e.g. alter the world (those rights should not automatically propagate everywhere, instead would have to be assigned manually to world editing objects/tools/domain objects inside the game, or the world editing tools/commands could ask for the privilegies (we should avoid accidental click through acceptance attacks and such though - maybe special right also for uploading tools/commands/scripts that requires special rights).

### Administration ###

  * **Applying content**.  A way to apply some info objects(?) describing some part of the world. (and unapply them?).  E.g. a game designer could design a house and paste it into the game world (without requiring in-game construction of it), or a game designer could edit the terrain directly.
  * Direct Editing.  Allow view to game objects and their fields, as well as editing and mass editing of them.
  * Shutdown.  A way to shutdown / restart the server.

### Base Game Objects ###

  * **Game Objects** (game objects, actions, perceptions, users, user access rights management)
  * **Tags** for game objects / persons.  Assigning properties for game objects by some other entity.  (Used for mini-game ranks, organization status, access rights, etc?).  A tag can be read only, and issued by some identified game object (player, organization, the server, etc).  Some kind of tag data structures can allow adding various comments etc (commenting - the comment stream should perhaps be separate model object thou).
  * **Component System** (add port concept to game objects, allow connecting game objects through ports)

### Information Objects and Networks ###

  * **Information Objects**.  Copyable, there can be several instances.  Maybe each info object could have its license (cc style) as metadata?.  Used for various player created content, as well as for game designer created terrains, NPC AI, etc.
  * **Info Object Libraries**.  Allows persons and organizations and such to collect categorized libraries of various information objects (designs, scripts, musical scores, layouts, paintings, etc.etc.  The libraries could be accessible with the right tags, or just be private.
  * Network concept, where nodes can be connected together to form a network where information objects / data packages / actions & perceptions (remote controlling an object) can be sent.

### Containment and Shapes ###

  * **Physical Object**.  Has position, velocity, acceleration, shape, appearance, material, mass, etc.  See Physics for simulating various physical forces.
  * **Basic item container system**.  Containers, inventories, spaces, moving items, portals, positions, velocities, accelerations.
  * **3D Space**.  Can have terrain, and handles collision detection between objects and between objects and the terrain.
  * **Item shape**.  Distinct items (simple references, or possibly generated on the fly when needed with a random seed), grouped items with varying properties (100 apples with size and color distribution), gaseous objects (trail of gas, expanding gas cloud (dissappears and merges with ambient atmosphere when large enough), liquid object (trail of falling liquid, liquid small pool on ground, liquid stream on ground, liquid sea on ground, rain of liquid), hot gas/fire?, roads and other things located along lines on ground, lakes, caves?, buildings?, etc.

### UI ###

  * **3D World Renderer**
  * **UI** (take game object proxies, create UI to show them and manipulate them, and allow the UI itself to be edited)
  * Renderer for some kind of page/text description format (subset of xhtml?) for allowing people to add content to their homepages, or to written documents.
  * **UI Editor** (use common editor framework?  save UI Layouts as one type of player created content/artifacts?)

## Content Domain Modules ##

### Physical Modelling ###

  * **Physics**.  gravitation, buoyancy, pressure, wind, densities, friction, collision handling(?) with ground and other objects, force fields.
  * **Materials**.  Properties of a material, and definitions of common materials?  Simulation of material reactions to heat, etc.

### Environment ###

  * **Gathering**.  Picking up things, picking berries, collecting stones, cutting lumber, digging ore, harvesting plants.
  * **Landscape**.  Defining, generating, editing, simulating.  Different terrain types.  Gatherable resources / plants / minerals.  River systems, roads, land ownage, etc.  Allow editing the map.
  * **Roads**.  Automatically created trails along much walked routes.  Player built roads.  Procedurally generated road networks, both rural and in cities.
  * Cities.  Generating cities based on parameters (cultures, neighbourhoods, etc).  Administration of cities?  Simulating people living in a city?  Designing (mostly game designers)
  * Buildings.  generating, designing, simulating (with inhabitants / users / customers / organizations).
  * Weather.  Changing weather, depending on geography and changing weather process, and time of year etc.  Could maybe also be affected by player activity (minimal: sky lit by ciy / fire, smoke, maximal: weather control spells, global warming, snowball planet, etc).
  * Ecology.  Plant and animal species, their distribution, meeting animals, patterns of animals, animal trails(?), change to populations through hunting, migration, introduced species, calamities, time of year, etc.
  * Water Systems.  Rivers, lakes, seas.  Model flow speed and water levels?  Create procedurally, first create big trunks, then smaller branches.

### Biology ###

  * **Body** (biological simulation)  Health, wounds, power output, running speed, endurance, sleepiness, hunger, thirst, etc.
  * **Species design**.  Evolving or engineering different species, using some common components / parametrized base forms.  Allows design of animals as well as plants (maybe somewhat different editors thou - animals have motions, as well as link to AI planner). (the choreography editor could be used for motion/animations).

### Engineering ###

  * **Crafting**.  Creating more complex objects from simpler raw materials, using some known form as goal, parametrized in some way.
    * Coating.  Effect of coating an item in some material / other wrapper item.  e.g. dipping a piece of cake in chocolate, falling into a lake, putting on a coat, painting a wall.  (related to crafting, maybe just one crafting technique).
  * **Machine system**.  Machine components connectible together, affecting the world.  Also allows player created UI:s to be attached to them
  * Services (and interactive products?).  Vending machines, or order systems, or virtual auctions, or virtual user created in-game networked board games, or weather report interfaces on the internal game net.  (Could also include products that present (complex) custom interfaces to the user?).  Can use custom interface components, or could be embedded on a gameweb page, and reachable through the in-game internet.


### Economy ###

  * **Contracts**.  Specifying exchange of goods or services or assets or roles/tags, and ensuring the transaction is executed in a secure way when the required conditions are met.
  * Economy & Trading.  Trading screen/interface, monetary systems, auctions, stocks (owning organizations), loans, insurances, etc.
  * Transport.  Package delivery service (server enforced sealed and return-on-timeout packages to prevent abusing the system?  Or delivery services that just have to earn trust?  Or sealed but breakable packages (locks, etc), and insurance system (how can it verify loss of property?)?)
  * Banking, currencies.  [[Ripple](Ripple.md)] could maybe be implemented in-game, allowing characters and organizations to grant credit to friends, and thus form a banking system / currency.  As Ripple develops, the in-game Ripple network could be connected to real world ripple networks also, allowing seamless integration and exchange of in-game and out-of-game money and credit.  As an aside, this would allow virtual NPC:s to participate in real world economics - quite an interesting thought. :-)

### Modeling People ###

  * **Character creation**. The process through which an user account can create a character in a game world.  Can use some interface that provides an UI, some actions and perceptions, and that returns / activates the resulting avatar when ready.
  * **AI**.  Knowledge base, planning, movement, etc..  Initially, create only the interfaces that allows AI to control characters.
  * **Skill system?**  Attributes and skills of a character, and how they affect performance (also mood affects it?)  (related to body system, at least basic attributes?)
  * Emotion system (model mood (and emotes?) of characters, allow changing it, etc.
  * Scripting / behaviour system (for controlling simple devices, up to complex NPC:s).
  * Cultures.  Define typical members of a culture, or typical values, beliefs, skills, appearances, etc. for a culture.  One person can belong to several cultures (with different weights) (cultures can overlap, smaller subcultures also).  Cultures also have city patters, etc.  Cultural simulation can be used for long term simulation of change (geopolitical & national).  Short term rumor / knowledge / meme system can keep track of how knowledge and practices spreads in a culture.
  * Memes / Information objects.  Ideas that can replicate by people telling them to other people (or writing them down in books, etc).  Can be any kind of information object, or observation.
  * Observation.  Observation should be recordable / memorable in some way, to allow people to relay observations, scouting, gossiping, etc.  Observations are memes / info objects.

### Organizing People ###

  * **Organizations**.  Members, roles, ranks, decisions / motions / laws / proposals, owned assets, rules for how decisions are made and the rules changed, relations to other organizations and individuals (issued tags?), etc.  Can have logos, maybe even issued currency, etc.
  * **Land ownership and Governance**.  How to claim land?  How to conquer land?  What does land ownage entitle to?  Taxation?
  * Rules / Instructions / Orders / Tasks for hired NPC:s  (also related to modelling people)

### Player Created Games ###

  * **Mini-games**, rules, signing up, play area (board, 'real' world area, etc), tournaments, ranking, etc.  From board games to sports, contests, competing for contracts, etc.  Games can also be observable (audience)
  * Scores.  High score tables, result tables, recordings / transcriptions of games, rankings of people, etc.  This is information objects, that can be published etc.


### Combat ###

  * **Fighting**.  How to enter fight state, and prevent griefing.  How to make fighting an interesting challenge, affected by player as well as character skills (about 50%-50% - no levels), as well as by character equipment (this can have a large effect too).  Duelling? (a form of sport = mini-game).
  * **Death and Resurrection**.  How to handle death / great injury of a character, and how to revive it?  Specific spawn points?  Penalties for death?  Different zones?  Taxation?  Specific rules?  Different realms?  Permadeath? (configurable server parameters probably)
  * War.  The rules of war - challenges, capturable bases?  Respawn points?  How to ensure wars can not be abused for griefing?  Truce and peace ageements.


### Socializing, Bookkeeping, History ###

  * **Chat Channels**.  Commenting on some object, or some general channel topic, used for channels, private communication, forums?, feedback, etc.   Can have a set topic, different polls, etc.  Allows posted links to various game concepts.
  * **Journal**.  In-game personal journal / blog (Journal sounds better, more fantasy-esque and less buzzworded).  Keep track of activities done, places visited, people met, documents created, etc.  Allow publication of some things.  Allow plugged in custom applications / scripts? (see facebook, one laptop per child, etc).  Also organizations, services, and various concepts can have journals.  Could be automatically updated, useful for looking what happened while one was offline, etc (stores incoming notes, allows direct chat, etc).  Give/calculate journal entries an inportance in percent, that can be used for filtering, as well as for deleting old entries when space is running low.  Could be implemented with the chat system.
  * (Home)pages for organizations / people / things + blogs(using Journal with Commenting)

### Fine Arts ###

  * **Architecture**.  Design a house, using rooms with variable shape and materials.  Add furniture.  Build house according to design.  Could also be used for any arrangement of physical objects and the terrain?  Like garden / landscape / road design?
  * **Choreography**.  Create movements for characters (could maybe even be functional moves, like fighting moves??), use some kind of IK system?  Allow parametrization.  Could be used for emotes, group animations (marching, dancing, handshake, etc).  Special editor, usable by players as well as content creators to create animations.  Can mix in actions and dialogue too (scripting).
  * Writing.  For blog of person / organization / service / thing? / concept / product / etc, or in-game book or document.  Allow wiki-like linking, allow collaborative editing.  Version control too?  In-game paper type / uses in game paper and writing implements?  Embed links to other in-game information objects (game objects, their blog/comment pages, statistics pages, etc?).  Could be used for in-game visible signposts too, as well as for notices, etc.
  * Painting.  Allow shared painting.  Different inks and brushes (based on parametrized, created in-game inks and brushes - a mixer board in the paint application for preparing (or at least mixing) them, parametrizing them, etc.)  Limited amount of ink based on in-game availability.  Can paint on separate canvases, or directly on walls, clothes, signposts, etc.  Pictures could be copied (requires artist & paint, or photocopier / press?).  Allow importing pictures, that can be rendered in-game on different objects using different techniques (murals, charcoal sketch, oil painting, colored flowers in flower bed).
  * Music.  Creating songs and tunes.  Tracker like interface.  Allow crafting different kinds of musical instruments, with parametrized sounds (both build time parameters, and playing parameters (volume, pitch, timbre?, etc).