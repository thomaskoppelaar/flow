some sort of flow controlled language based around scoping?

all code is written in `(`cells`)` , and connected via `->` flow markers.


parsing:

scala?

=>(a)=>(b)=>

should turn into
FlowEq(Operation(a, FlowType(Operation(b, FlowType()))))

What does FlowEq need to know?
- What data it is carrying (via interpreter function call? pass along environment?)
- What it is going into (via object attribute)

What does Operation need to know?
- What data is being inputted (via interpreter)
- What it needs to do on the input (via operations attribute)
- Where it needs to output (via next attribute)

How does Operation reference the input?
- Option A: Define input at start of cell: `(a| <operation>)`, `(a,b| <operation>)`
  - Pro: clear definition 
  - Con: 
- Option B: Infer argument count from operation itself: `(<operation that includes a varible named "a">)`
  - Pro: Less to write
  - Con: Ordering of arguments/interp is tricky
  - Con: If variable storage is going to be a thing, this might be an issue
- Option C: Have hardcoded names for the first/second/third argument


How do you print something?
- Option A: special cell type to output: (2)=>{}
  - Pro: would mean that file output later will be easier
  - Con: Early reserving of cell type
- Option B: function call, or syntax: `(a | print <operation>)`
  - Pro: pretty clear
  - Con: boring



Example program:
```cpp
(2) => (a | * (+ 1 a) 2) => {}
```

`(2)` is the starting cell, returns/outputs 2
`(a |` means the cell takes in 1 input
`* (+ 1 a) 2` returns `(1+a)*2`, which in this case means 6
`=> {}` prints the result to the console.


```cpp
{} => (a | + a 1) => {}
```
`{} =>` is the starting cell, takes in an argument
`(a |` means the cell takes in 1 input
`+ a 1` means the cell returns a + 1
`=> {}` prints the result to the console.


scoping:

for now:
- a single cell type
- 1 input unit thing
- 1 output thing
- only working with numbers



future:
- more cell types
- N input support
- N output support
- more data types
- piping into different files
- "wormhole" flow marker? ~label~> acts as a GOTO operation?
- scoping via -> and +>


syntax:

cell types:

( ) { } [ ] | | ! !

drawbacks: 


- readability / nested cells

you don't want to have something like ((abc) + (a(v))), because its hard to a. parse and b. read.

so you restrict cells to not contain cells...?
below example:
`=> ( (a) -> (b) ) => `
does look pretty cool.

- branching paths


```
() -> ()
   \-> ()
```

