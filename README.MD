# IL-2 Integrator

The IL-2 integrator aims to provide a common point of integration for the game series IL-2 Great
Battles that third party tools can use to improve the game experience.

## Features
* Career integration
* PWCG integration

## Installing
To install the integrator, simply go the the Releases, download the latest version and extract it on
the root directory of your game installation. Then, launch it whenever you want to use its
functionalities. It is not necessary to launch the game through the integrator as this is just a
utility. However, the application must be left running in the background.

## Integration into third party tools
Feel free to use the functionalities provided by the integrator in any third party tool that you
might be developing. As a starting point, read the README inside the core module to have a basic
understanding of the system. Then, read about each service that you intend to use and the
functionalities that they provide. Finally, read about the interface that you decide to use to learn
how you can connect to the integrator and the defined protocol.

## Available interfaces
* HTTP - available through port 8080
* UDP - available through port 25005

## Contributing
Contributions are welcome, specially with new services and interfaces. If you need any specific
functionality for your tool, feel free to open an issue or create a pull request with the
implementation.

When creating pull requests, always follow the style conventions that are found currently in the
project.

Tests are always welcome, even though they are not always easy to implement. 

## Building
To build the project, Maven is required. From the root folder simply run:

`mvn package`

The final binaries can be found inside the folder "target" of both the `cli` and `gui` modules.