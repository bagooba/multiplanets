tmux new-session -d -s one-dimension
tmux split-window -t one-dimension:1 -v
tmux rename-window main
tmux send-keys -t one-dimension:1.1 "vim src/one_dimension/core.clj" "Enter"
tmux send-keys -t one-dimension:1.2 "lein repl" "Enter"
tmux new-window -t one-dimension:2
tmux select-window -t one-dimension:2
tmux rename-window server
tmux select-window -t one-dimension:1
tmux attach -t one-dimension
