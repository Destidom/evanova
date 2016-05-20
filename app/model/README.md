###Addition model objects and data stores for Evanova

This module contains data-related objects and interfaces to data stores, as well as their ORMLite implementation.

#####Usage

Each data storage is represented with a high-level *Facade* interface which is used to interact with one or more data store.

Only facade and model objects should be used by client code. Anything beyond a facade implementation is subject to change without notice.
