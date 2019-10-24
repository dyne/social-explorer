# social explorer

A generic blockchain explorer UI which uses the social-wallet-api for backend and [sawroom](https://github.com/DECODEproject/Sawroom) as blockchain.

<a href="https://www.dyne.org"><img
src="https://decode-sawroom-explorer.dyne.org/static/img/dyne.png"
width="460px"
title="software by Dyne.org"></a>

## Getting Started
To run the social explorer you need an instance of [sawroom](https://github.com/DECODEproject/Sawroom) up and running as well as an instance of [SWAPI](https://github.com/Commonfare-net/social-wallet-api) connected to it.

### Sawroom
A docker image of sawromm can be found here https://github.com/dyne/docker-dyne-software/tree/master/sawroom
Build and run the image with `docker-compose up`

### SWAPI
⚠️ Currently sawroom abstraction is under development.

Sawroom implementation can be found on `feature/block-explorer` branch.

### Social explorer
- Configure the `config.yaml` file 
- `lein run`

## License

This project is licensed under the AGPL 3 License - see the [LICENSE](LICENSE) file for details

#### Additional permission under GNU AGPL version 3 section 7.

If you modify social-explorer, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.


