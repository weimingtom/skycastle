Much functionality will be implemented dynamically as dynamic actions, trigger listeners, and so on.  Being able to specify it with a text based domain specific language will speed up development.

Later a graphical interface might be created for the language, allowing easy use for scripting by all players.

## Functionality ##

  * Calling actions, asynchronously.
  * Reacting to action calls
  * Reacting to property change triggers
  * Reading and setting properties synchronously
  * Expressions
  * Local functions
  * Property / Parameter construction
  * Entity creation? (assuming enough privileges)
  * Direct synchronous action calling? (assuming sufficient privileges)
  * Basic control structures, looping.  Infinite loops timeout though.
  * Some restrictions on memory usage

## Other features ##
  * Simple clear syntax
  * Limited type checking at 'compile' time.  Implement better later maybe.
  * Probably interpreted.
  * Maybe count used function points and memory, to interrupt too long functions and for microcharging (to avoid armies of programmed self-replicating zombiebots etc eating up all server capacity).