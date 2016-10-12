#!/bin/sh
set -euo pipefail

brew install grafana
brew install telegraf
brew install influxdb

grafana-cli --pluginsDir "/usr/local/var/lib/grafana/plugins"  plugins install
