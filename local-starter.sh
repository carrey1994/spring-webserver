#!/bin/bash

# unlock .local-env
git-crypt unlock ~/.git-crypt-key

# Jar build
export $(cat .local-env | xargs)

./mvnw spotless:apply
./mvnw clean package -DskipTests=true

if [ $? != 0 ]; then
    exit 1
fi

# Name of the tmux session
SESSION_NAME="demo-webserver"

# Java command to be executed
JAVA_CMD="java -agentlib:jdwp=transport=dt_socket,address=5050,server=y,suspend=n \
        -jar -Dspring.profiles.active=supa \
        webserver/target/webserver-0.0.1-SNAPSHOT.jar"
        
# Delete the session if it already exists
tmux kill-session -t webserver-demo

# Start the tmux session
tmux new-session -d -s $SESSION_NAME

# Run the Java command in the tmux session
tmux send-keys -t $SESSION_NAME "$JAVA_CMD" C-m

# Attach to the tmux session
tmux attach -t $SESSION_NAME
