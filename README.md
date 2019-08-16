# Social explorer

A generic social explorer UI which uses the social-wallet-api for a backend and sawroom as ledger

<a href="https://www.dyne.org"><img
src="https://zenroom.dyne.org/img/software_by_dyne.png"
alt="software by Dyne.org"
title="software by Dyne.org" class="pull-right"></a>

## Getting Started
To run the social explorer you need an instance of [sawroom](https://github.com/DECODEproject/Sawroom) up and running as well as an instance of [SWAPI](https://github.com/Commonfare-net/social-wallet-api) connected to it.

### Sawroom
A docker image of sawromm can be found here https://github.com/dyne/docker-dyne-software/tree/master/sawroom
Build and run the image with `docker-compose up`

### SWAPI
⚠️ Currently sawroom abstraction is under heavy development.

Sawroom implementation can be found on `feature/sawtooth` branch.
Some tweaks are needed to connect swapi to sawroom atm.
- Create a `checkouts` folder at the source SWAPI directory
- Clone and Symlink the `freecoin-lib` on `checkouts`
- Create a `maven_repository` directory at the source freecoin-lib directory
- Build and release locally `sawtooth-sdk-signing` dependency
- `git checkout 0a95835`
- `lein ring server`

### Social explorer
- Configure the `config.yaml` file 
- `lein run`

## License

This project is licensed under the AGPL 3 License - see the [LICENSE](LICENSE) file for details

#### Additional permission under GNU AGPL version 3 section 7.

If you modify social-wallet, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.


