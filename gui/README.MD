# IL-2 Integrator GUI
The GUI is main distributable binary of the integrator. It provides a user interface that displays
the IP address of the local network and offers two buttons: one to launch the IL-2 game and another
one to launch PWCG. Each button will be enabled only if the corresponding service is registered.

When minimized, the application goes to the system's tray (if available), where it will reside until
closed or the application is reopened.

Currently, the GUI offers all available services and interfaces. The interfaces are bound to the
following ports:
* HTTP Interface: 8080
* UDP Interface: 25005

## Planned features
In the future, the following features are planned to be added:
* Options - to allow the users to control properties of both the interfaces and the services. One
example is allows the interfaces' ports to be customized
* System status - display which services and interfaces are running and if any of them failed to
initialize