# Simple Version Control System (SVCS)

SVCS is a simple command-line version control system implemented in Kotlin. It allows users to track changes in their project files, commit those changes, and view commit logs.

## Features
Basic Version Control: SVCS provides basic version control functionalities such as file tracking, committing changes, and viewing commit logs.
User-Friendly Commands: SVCS offers simple and intuitive commands for users to interact with the version control system.
Efficient File Tracking: SVCS efficiently tracks changes in project files using cryptographic hashing algorithms.
Secure Commit Logs: Commit logs are securely stored and include information about the author and commit message.
Usage

## SVCS can be used via command-line interface (CLI) with the following commands:

- config: Get and set a username for commit authorship.

- add: Add a file to the index for tracking changes.

- commit: Save changes to tracked files with a commit message.

- log: View commit logs.

- checkout: Restore a file to its state at a specific commit.

For detailed usage instructions and examples, refer to the SVCS Documentation.

## Getting Started
To get started with SVCS, follow these steps:

Clone the SVCS repository to your local machine.
Build the project using Kotlin compiler.
Run the SVCS executable and start tracking changes in your project files.
Contributing
Contributions to SVCS are welcome! If you find a bug or have a feature request, please open an issue on the GitHub 
repository. Pull requests are also encouraged.


    config 
Allows the user to set their own name or output an existing one. If a user wants to set a new name, the program 
overwrites the old one. The program also validates the input name for correctness and informs the user in case of an error.

    add
allow the user to set the name of a file that they want to track or output the names of tracked files. If the file does 
not exist, the program informs a user that the file does not exist.

    commit
Must be invoked with a message. Saves all changes and assigns each commit a unique ID. If there are no changes since the
last commit, a new commit is not created. The storage of changes is not optimized; all staged files are copied to the 
commit folder every time.

    log
Displays all commits in reverse order.

    checkout 
command must be passed to the program together with the commit ID to indicate which commit to use.
If a commit with the given ID exists, the contents of the tracked file are restored according to this commit.
