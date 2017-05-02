FROM maven:3.5.0-jdk-8
WORKDIR /qa-wagerplayer
COPY . .

ENTRYPOINT ["./ts.sh"]