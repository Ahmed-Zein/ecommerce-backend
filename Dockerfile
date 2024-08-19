FROM ubuntu:latest
LABEL authors="ava"

ENTRYPOINT ["top", "-b"]