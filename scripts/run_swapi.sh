#!/bin/bash

git clone https://github.com/Commonfare-net/social-wallet-api.git
cd social-wallet-api
git checkout without-apikey
lein ring server-headless
