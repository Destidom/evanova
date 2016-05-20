####View Components for Evanova

This module contains custom Android widgets (views) for displaying model objects.


#####Design Principles

* There must not be any user interaction code in the widgets. Providing interaction capabilities should be done through Listeners or callbacks.

* There is no data loading, handling or refreshing considerations. Data should be applied to widgets by their clients.

* Avoid complex or composite widgets that fill up a particular UI use case. Compositing such as assembling widgets into a pager should be done by clients.

